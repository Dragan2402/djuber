package com.djuber.djuberbackend.Application.Services.Ride;

import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;

public interface IRideService {
    void getClosestFittingDriver(RideRequest rideRequest);
}
