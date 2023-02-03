package com.djuber.djuberbackend.Controllers.Ride.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideReviewResponse {
    Long rideId;

    Long driverId;

    boolean canRate;

    String driverName;

    public RideReviewResponse() {
    }

    public RideReviewResponse(Long rideId, Long driverId, boolean canRate, String driverName) {
        this.rideId = rideId;
        this.driverId = driverId;
        this.canRate = canRate;
        this.driverName = driverName;
    }
}
