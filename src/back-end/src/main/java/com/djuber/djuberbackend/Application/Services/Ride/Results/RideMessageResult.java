package com.djuber.djuberbackend.Application.Services.Ride.Results;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class RideMessageResult {
    RideMessageStatus status;
    Long rideId;
    Double ridePrice;

    public RideMessageResult(RideMessageStatus status, Long rideId) {
        this.status = status;
        this.rideId = rideId;
        this.ridePrice = null;
    }
}
