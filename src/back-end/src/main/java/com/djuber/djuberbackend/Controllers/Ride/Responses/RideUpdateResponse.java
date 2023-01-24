package com.djuber.djuberbackend.Controllers.Ride.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideUpdateResponse {
    String rideStatus;
    Double lat;
    Double lon;

    public RideUpdateResponse() {
    }

    public RideUpdateResponse(String rideStatus, Double lat, Double lon) {
        this.rideStatus = rideStatus;
        this.lat = lat;
        this.lon = lon;
    }
}
