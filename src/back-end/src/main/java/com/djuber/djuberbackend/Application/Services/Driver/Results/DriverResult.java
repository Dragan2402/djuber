package com.djuber.djuberbackend.Application.Services.Driver.Results;

import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Value
@AllArgsConstructor
public class DriverResult {

    Long id;

    Long identityId;

    String email;

    String firstName;

    String lastName;

    String city;

    String phoneNumber;

    Long carId;

    CarType carType;

    String licensePlate;

    String note;

    boolean blocked;

    Set<String> additionalService;

    public DriverResult(@NotNull Driver driver){
        this.id = driver.getId();
        this.identityId = driver.getIdentity().getId();
        this.email = driver.getIdentity().getEmail();
        this.firstName = driver.getFirstName();
        this.lastName = driver.getLastName();
        this.city = driver.getCity();
        this.phoneNumber = driver.getPhoneNumber();
        this.carId = driver.getCar().getId();
        this.carType = driver.getCar().getCarType();
        this.licensePlate = driver.getCar().getLicensePlate();
        this.note = driver.getNote();
        this.blocked = driver.getBlocked();
        this.additionalService = driver.getCar().getAdditionalServices();
    }
}
