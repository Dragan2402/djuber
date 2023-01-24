package com.djuber.djuberbackend.Application.Services.Ride;

import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Domain.Ride.Ride;

public interface IRideService {
    void getClosestFittingDriver(RideRequest rideRequest);
    RideResponse getRideResponse(Long rideId);
    void acceptRideOffer(Long rideId);
    void declineRideOffer(Long rideId);
}
