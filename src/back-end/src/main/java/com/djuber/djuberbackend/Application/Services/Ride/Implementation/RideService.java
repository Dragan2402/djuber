package com.djuber.djuberbackend.Application.Services.Ride.Implementation;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Application.Services.Ride.Mapper.RideMapper;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageResult;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageStatus;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideUpdateResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.ICarRepository;
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

import java.io.IOException;
import java.util.List;
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
    final ICarRepository carRepository;

    private static final String TOPIC_PATH = "/topic/ride/";

    @Override
    @Transactional
    public void offerSingleRideToDriver(RideRequest rideRequest) {
        Ride ride = createRide(rideRequest);

        if (ride.getDriver() == null) {
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_DECLINED, null);
            Client client = ride.getClients().get(0);
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + client.getIdentity().getId(), result);
        } else {
            ride = rideRepository.save(ride);
            coordinatesRepository.saveAll(ride.getRoute().getCoordinates());

            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_DRIVER_OFFER, ride.getId());
            Identity driverIdentity = ride.getDriver().getIdentity();
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + driverIdentity.getId(), result);
        }
    }

    @Override
    public void offerSharedRideToClients(RideRequest rideRequest) {
        Ride ride = createRide(rideRequest);

        if (ride.getDriver() == null) {
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_DECLINED, null);
            Client client = ride.getClients().get(0);
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + client.getIdentity().getId(), result);

        } else {
            String clientEmail = rideRequest.getClientEmails().get(0);
            ride.getClientsAccepted().add(clientEmail);
            ride = rideRepository.save(ride);
            coordinatesRepository.saveAll(ride.getRoute().getCoordinates());

            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_OFFER, ride.getId());
            for (Client client : ride.getClients()) {
                if (!client.getIdentity().getEmail().equals(clientEmail)) {
                    simpMessagingTemplate.convertAndSend(TOPIC_PATH + client.getIdentity().getId(), result);
                }
            }
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
    public void acceptRideDriverOffer(Long rideId) throws IOException, InterruptedException {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        ride.setRideStatus(RideStatus.ON_THE_WAY);
        RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_ACCEPTED, ride.getId());
        for (Client client : ride.getClients()) {
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + client.getIdentity().getId(), result);
        }
        rideRepository.save(ride);

        this.execute(rideId);
    }

    @Override
    public void declineRideDriverOffer(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }

        Coordinate startCoordinate = ride.getRoute().getStartCoordinate();
        List<Driver> sortedAvailableDrivers = driverRepository.findAvailableDriversSortedByDistanceFromCoordinate(startCoordinate);
        String carType = ride.getDriver().getCar().getCarType().toString();
        Driver nextFittingDriver = getNextFittingDriver(sortedAvailableDrivers, carType, ride.getRequestedServices(), ride.getDriver().getId());

        if (nextFittingDriver == null) {
            rideRepository.delete(ride);
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_DECLINED, null);
            for (Client client : ride.getClients()) {
                simpMessagingTemplate.convertAndSend(TOPIC_PATH + client.getIdentity().getId(), result);
            }

        } else {
            ride.setDriver(nextFittingDriver);
            ride = rideRepository.save(ride);
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_DRIVER_OFFER, ride.getId());

            Identity driverIdentity = nextFittingDriver.getIdentity();
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + driverIdentity.getId(), result);
        }
        rideRepository.save(ride);
    }

    @Override
    public void acceptRideClientOfferAndSendDriverOffer(Long rideId, String clientEmail) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        ride.getClientsAccepted().add(clientEmail);
        ride = rideRepository.save(ride);

        if (ride.getClients().size() == ride.getClientsAccepted().size()) {
            RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_DRIVER_OFFER, ride.getId());
            Identity driverIdentity = ride.getDriver().getIdentity();
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + driverIdentity.getId(), result);
        }
    }

    @Override
    public void declineRideClientOffer(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }

        rideRepository.delete(ride);
        RideMessageResult result = new RideMessageResult(RideMessageStatus.RIDE_CLIENT_DECLINED, null);
        for (String otherClientEmail : ride.getClientsAccepted()) {
            Identity identity = identityRepository.findByEmail(otherClientEmail);
            simpMessagingTemplate.convertAndSend(TOPIC_PATH + identity.getId(), result);
        }
    }

    public boolean execute(Long rideId) throws IOException, InterruptedException {
        String[] commands = {"locust", "-f", "script/djuber-simulation.py", "--conf", "script/locust.conf", "--data", "{\\\"rideId\\\":\\\"1\\\"}"};
        ProcessBuilder pb = new ProcessBuilder().command(commands);

        Process process = pb.start();

        process.waitFor();
        return true;
    }

    @Override
    public CoordinateResponse getDriverStartingLocation(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        CoordinateResponse driverCoordinate = new CoordinateResponse();
        driverCoordinate.setIndex(0);
        driverCoordinate.setLat(ride.getDriver().getCar().getLat());
        driverCoordinate.setLon(ride.getDriver().getCar().getLon());

        return driverCoordinate;
    }

    @Override
    public CoordinateResponse getRideStartingLocation(Long rideId) {

        Coordinate startingCoordinate = coordinatesRepository.findFirstCoordinateByRideId(rideId);
        if (startingCoordinate == null) {
            throw new NotFoundException("Coordinate not found");
        }
        return new CoordinateResponse(startingCoordinate);
    }

    @Override
    public void updateVehicleLocation(Long rideId, CoordinateRequest request) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        Car car = carRepository.findById(ride.getDriver().getCar().getId()).orElse(null);

        if (car == null) {
            throw new NotFoundException("Car not found.");
        }

        car.setLat(request.getLat());
        car.setLon(request.getLon());

        carRepository.save(car);

        RideUpdateResponse rideUpdateResponse = new RideUpdateResponse(ride.getRideStatus().toString(), request.getLat(),request.getLon());

        simpMessagingTemplate.convertAndSend("/topic/singleRide/" + rideId, rideUpdateResponse);
    }

    @Override
    public List<CoordinateResponse> startRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        ride.setRideStatus(RideStatus.ACTIVE);
        rideRepository.save(ride);

        List<Coordinate> coordinates = coordinatesRepository.findByRouteId(ride.getRoute().getId());

        RideUpdateResponse rideUpdateResponse = new RideUpdateResponse(ride.getRideStatus().toString(), coordinates.get(0).getLat(),coordinates.get(0).getLon());

        simpMessagingTemplate.convertAndSend("/topic/singleRide/" + rideId, rideUpdateResponse);

        return RideMapper.map(coordinates);
    }

    @Override
    public void endRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElse(null);
        if (ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        ride.setRideStatus(RideStatus.DONE);
        rideRepository.save(ride);

        simpMessagingTemplate.convertAndSend("/topic/singleRide/" + rideId, new RideUpdateResponse(ride.getRideStatus().toString(),0D,0D));
    }

    private Ride createRide(RideRequest rideRequest) {
        Ride ride = RideMapper.map(rideRequest);
        for (String clientEmail : rideRequest.getClientEmails()) {
            Identity clientIdentity = identityRepository.findByEmail(clientEmail);
            Client client = clientRepository.findByIdentityId(clientIdentity.getId());
            ride.getClients().add(client);
        }

        Coordinate startCoordinate = ride.getRoute().getStartCoordinate();
        List<Driver> sortedAvailableDrivers = driverRepository.findAvailableDriversSortedByDistanceFromCoordinate(startCoordinate);
        Driver closestFittingDriver = getClosestFittingDriver(sortedAvailableDrivers, rideRequest.getCarType(), rideRequest.getAdditionalServices());
        ride.setDriver(closestFittingDriver);
        return ride;
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
