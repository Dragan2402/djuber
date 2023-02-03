package com.djuber.djuberbackend.Controllers.Driver.Response;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverLocationResponse {

    long id;

    Double lat;

    Double lon;

    boolean inRide;

    public DriverLocationResponse() {
    }

    public DriverLocationResponse(Driver driver){
        this.id = driver.getId();
        this.lat = driver.getCar().getLat();
        this.lon = driver.getCar().getLon();
        this.inRide = driver.getInRide();
    }
}
