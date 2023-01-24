package com.djuber.djuberbackend.Controllers.Driver.Response;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvailableDriverResponse {

    private long id;

    private Double lat;

    private Double lon;

    public AvailableDriverResponse() {
    }

    public AvailableDriverResponse(Driver driver){
        this.id = driver.getId();
        this.lat = driver.getCar().getLat();
        this.lon = driver.getCar().getLon();
    }
}
