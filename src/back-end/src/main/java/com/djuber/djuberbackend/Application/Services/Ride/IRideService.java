package com.djuber.djuberbackend.Application.Services.Ride;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.ReviewRideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideReviewResponse;
import com.djuber.djuberbackend.Domain.Ride.Ride;

import java.io.IOException;
import java.util.List;

public interface IRideService {
    void offerSingleRideToDriver(RideRequest rideRequest);
    void offerSharedRideToClients(RideRequest rideRequest);
    RideResponse getRideResponse(Long rideId);
    void acceptRideDriverOffer(Long rideId) throws IOException, InterruptedException;
    void declineRideDriverOffer(Long rideId);
    void acceptRideClientOfferAndSendDriverOffer(Long rideId, String clientEmail);

    void declineRideClientOffer(Long rideId);

    CoordinateResponse getDriverStartingLocation(Long rideId);

    CoordinateResponse getRideStartingLocation(Long rideId);

    void updateVehicleLocation(Long rideId, CoordinateRequest request);

    List<CoordinateResponse> startRide(Long rideId);

    void endRide(Long rideId);

    RideReviewResponse getRideForReviewResponse(String email, Long rideId);

    void reviewRide(String email, ReviewRideRequest request);

    void declineAssignedRide(Long rideId);

    void submitCancellingNote(Long rideId, String note);
}
