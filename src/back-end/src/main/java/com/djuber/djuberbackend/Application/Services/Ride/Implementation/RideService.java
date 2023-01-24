package com.djuber.djuberbackend.Application.Services.Ride.Implementation;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Application.Services.Ride.Mapper.RideMapper;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageResult;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageStatus;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IRideRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.ICoordinatesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideService implements IRideService {
    final IRideRepository rideRepository;
    final IIdentityRepository identityRepository;
    final IClientRepository clientRepository;
    final IDriverRepository driverRepository;
    final ICoordinatesRepository coordinatesRepository;
    final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public void getClosestFittingDriver(RideRequest rideRequest) {
        Ride ride = RideMapper.map(rideRequest);
        Identity clientIdentity = identityRepository.findByEmail(rideRequest.getClientEmail());
        Client client = clientRepository.findByIdentityId(clientIdentity.getId());
        ride.getClients().add(client);

        Coordinate startCoordinate = ride.getRoute().getStartCoordinate();
        List<Driver> sortedAvailableDrivers = driverRepository.findAvailableDriversSortedByDistanceFromCoordinate(startCoordinate);
        Driver closestFittingDriver = getClosestFittingDriver(sortedAvailableDrivers, rideRequest.getCarType(), rideRequest.getAdditionalServices());

        if (closestFittingDriver == null) {
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_DECLINED, null);
            simpMessagingTemplate.convertAndSend("/topic/ride/" + clientIdentity.getId(), result);

        } else {
            ride.setDriver(closestFittingDriver);
            ride = rideRepository.save(ride);
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_DRIVER_OFFER, ride.getId());

            Identity driverIdentity = closestFittingDriver.getIdentity();
            simpMessagingTemplate.convertAndSend("/topic/ride/" + driverIdentity.getId(), result);
        }
    }

    @Override
    public RideResponse getRideResponse(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        List<Coordinate> coordinates = coordinatesRepository.findByRouteId(ride.getRoute().getId());
        return RideMapper.mapResponse(ride, coordinates);
    }

    @Override
    public void acceptRideOffer(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        ride.setRideStatus(RideStatus.ON_THE_WAY);
        RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_ACCEPTED, ride.getId());
        for (Client client : ride.getClients()) {
            simpMessagingTemplate.convertAndSend("/topic/ride/" + client.getIdentity().getId(), result);
        }
    }

    @Override
    public void declineRideOffer(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }

        Coordinate startCoordinate = ride.getRoute().getStartCoordinate();
        List<Driver> sortedAvailableDrivers = driverRepository.findAvailableDriversSortedByDistanceFromCoordinate(startCoordinate);
        String carType = ride.getDriver().getCar().getCarType().toString();
        Driver nextFittingDriver = getNextFittingDriver(sortedAvailableDrivers, carType, ride.getRequestedServices(), ride.getDriver().getId());

        if (nextFittingDriver == null) {
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_DECLINED, null);
            for (Client client : ride.getClients()) {
                simpMessagingTemplate.convertAndSend("/topic/ride/" + client.getIdentity().getId(), result);
            }

        } else {
            ride.setDriver(nextFittingDriver);
            ride = rideRepository.save(ride);
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_DRIVER_OFFER, ride.getId());

            Identity driverIdentity = nextFittingDriver.getIdentity();
            simpMessagingTemplate.convertAndSend("/topic/ride/" + driverIdentity.getId(), result);
        }
    }

    private static Driver getClosestFittingDriver(List<Driver> sortedAvailableDrivers, String carType, Set<String> additionalServices) {
        if (sortedAvailableDrivers == null) {
            return null;
        }
        Driver closestFittingDriver = null;

        for (Driver driver : sortedAvailableDrivers) {
            boolean driverFits = true;
            Car car = driver.getCar();

            if (!carType.equals("Any") && !carType.equals(car.getCarType().toString())) {
                driverFits = false;
            }

            for (String service : additionalServices) {
                if (!car.getAdditionalServices().contains(service)) {
                    driverFits = false;
                    break;
                }
            }

            if (driverFits) {
                closestFittingDriver = driver;
                break;
            }
        }

        return closestFittingDriver;
    }

    private static Driver getNextFittingDriver(List<Driver> sortedAvailableDrivers, String carType, Set<String> additionalServices, Long currentDriverId) {
        if (sortedAvailableDrivers == null) {
            return null;
        }
        Driver nextFittingDriver = null;

        int i = 0;
        while (i < sortedAvailableDrivers.size()) {
            Driver driver = sortedAvailableDrivers.get(i);
            i++;
            if (currentDriverId.equals(driver.getId())) {
                break;
            }
        }

        while (i < sortedAvailableDrivers.size()) {
            Driver driver = sortedAvailableDrivers.get(i);
            boolean driverFits = true;
            Car car = driver.getCar();

            if (!carType.equals("Any") && !carType.equals(car.getCarType().toString())) {
                driverFits = false;
            }

            for (String service : additionalServices) {
                if (!car.getAdditionalServices().contains(service)) {
                    driverFits = false;
                    break;
                }
            }

            if (driverFits) {
                nextFittingDriver = driver;
                break;
            }

            i++;
        }

        return nextFittingDriver;
    }
}
