package com.djuber.djuberbackend.Controllers.Ride.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideUpdateResponse {
    String rideStatus;
    int minutesRemaining;
    Double lat;
    Double lon;

    public RideUpdateResponse() {
    }

    public RideUpdateResponse(String rideStatus, int minutesRemaining, Double lat, Double lon) {
        this.rideStatus = rideStatus;
        this.minutesRemaining = minutesRemaining;
        this.lat = lat;
        this.lon = lon;
    }
}
