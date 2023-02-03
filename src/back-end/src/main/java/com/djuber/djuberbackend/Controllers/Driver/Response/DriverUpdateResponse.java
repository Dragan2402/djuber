package com.djuber.djuberbackend.Controllers.Driver.Response;

import com.djuber.djuberbackend.Domain.Driver.DriverDataChange;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverUpdateResponse {
    Long id;
    Long requestId;
    String email;
    String firstName;
    String lastName;
    String city;
    String carType;
    String licensePlate;
    String newFirstName;
    String newLastName;
    String newCity;
    String newPhoneNumber;
    String newCarType;
    String newLicensePlate;
    Set<String> newAdditionalServices;

    public DriverUpdateResponse(DriverDataChange driverDataChange){
        this.id = driverDataChange.getDriver().getId();
        this.requestId = driverDataChange.getId();
        this.email = driverDataChange.getDriver().getIdentity().getEmail();
        this.firstName = driverDataChange.getDriver().getFirstName();
        this.lastName = driverDataChange.getDriver().getLastName();
        this.city = driverDataChange.getDriver().getCity();
        this.carType = String.valueOf(driverDataChange.getDriver().getCar().getCarType());
        this.licensePlate = driverDataChange.getDriver().getCar().getLicensePlate();
        this.newFirstName = driverDataChange.getFirstName();
        this.newLastName = driverDataChange.getLastName();
        this.newCity = driverDataChange.getCity();
        this.newPhoneNumber = driverDataChange.getPhoneNumber();
        this.newCarType = String.valueOf(driverDataChange.getCarType());
        this.newLicensePlate = driverDataChange.getLicensePlate();
        this.newAdditionalServices = driverDataChange.getAdditionalServices();
    }
}
