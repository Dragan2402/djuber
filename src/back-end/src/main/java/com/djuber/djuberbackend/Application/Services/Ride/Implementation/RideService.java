package com.djuber.djuberbackend.Application.Services.Ride.Implementation;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Application.Services.Ride.Mapper.RideMapper;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideService implements IRideService {
    final IDriverRepository driverRepository;

    @Override
    public void getClosestFittingDriver(RideRequest rideRequest) {
        Ride ride = RideMapper.map(rideRequest);
        Coordinate startCoordinate = ride.getRoute().getStartCoordinate();
        List<Driver> sortedAvailableDrivers = driverRepository.findAvailableDriversSortedByDistanceFromCoordinate(startCoordinate);
        Driver closestFittingDriver = null;

        if (sortedAvailableDrivers != null) {
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
        }
        if (closestFittingDriver == null) {
            System.out.println("No available drivers that fit your criteria.");
        } else {
            System.out.println("Hello driver " +
                    closestFittingDriver.getFirstName() + " " +
                    closestFittingDriver.getLastName() + ", do you want this ride?");
        }
    }
}
