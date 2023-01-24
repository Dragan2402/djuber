package com.djuber.djuberbackend.Application.Services.Ride.Implementation;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Application.Services.Ride.Mapper.RideMapper;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageResult;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageStatus;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IRideRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideService implements IRideService {
    final IRideRepository rideRepository;
    final IIdentityRepository identityRepository;
    final IClientRepository clientRepository;
    final IDriverRepository driverRepository;
    final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public void getClosestFittingDriver(RideRequest rideRequest) {
        Ride ride = RideMapper.map(rideRequest);
        Identity clientIdentity = identityRepository.findByEmail(rideRequest.getClientEmail());
        Client client = clientRepository.findByIdentityId(clientIdentity.getId());
        ride.getPassengers().add(client);

        Coordinate startCoordinate = ride.getRoute().getStartCoordinate();
        List<Driver> sortedAvailableDrivers = driverRepository.findAvailableDriversSortedByDistanceFromCoordinate(startCoordinate);
        Driver closestFittingDriver = getClosestFittingDriver(rideRequest, sortedAvailableDrivers);

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

    private static Driver getClosestFittingDriver(RideRequest rideRequest, List<Driver> sortedAvailableDrivers) {
        if (sortedAvailableDrivers == null) {
            return null;
        }
        Driver closestFittingDriver = null;

        for (Driver driver : sortedAvailableDrivers) {
            boolean driverFits = true;
            Car car = driver.getCar();

            if (!rideRequest.getCarType().equals("Any") && !rideRequest.getCarType().equals(car.getCarType().toString())) {
                driverFits = false;
            }

            for (String service : rideRequest.getAdditionalServices()) {
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
}
