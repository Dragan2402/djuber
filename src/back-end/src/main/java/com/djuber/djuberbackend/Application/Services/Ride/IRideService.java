package com.djuber.djuberbackend.Application.Services.Ride;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Domain.Ride.Ride;

import java.io.IOException;
import java.util.List;

public interface IRideService {
    void getClosestFittingDriver(RideRequest rideRequest);
    RideResponse getRideResponse(Long rideId);
    void acceptRideOffer(Long rideId) throws IOException, InterruptedException;
    void declineRideOffer(Long rideId);

    CoordinateResponse getDriverStartingLocation(Long rideId);

    CoordinateResponse getRideStartingLocation(Long rideId);

    void updateVehicleLocation(Long rideId, CoordinateRequest request);

    List<CoordinateResponse> startRide(Long rideId);

    void endRide(Long rideId);
}
