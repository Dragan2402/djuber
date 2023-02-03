package com.djuber.djuberbackend.Application.Services.Ride;

import com.djuber.djuberbackend.Application.Services.Ride.Results.RideResult;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.ReviewRideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RidePreviewResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IRideService {

    Page<RideResult> readPageable(Pageable pageable, String clientEmail);
    RideResponse getRideResponse(Long rideId);
    List<RideResult> getAllRides();
    void processRideRequest(RideRequest rideRequest) throws IOException, InterruptedException;

    void acceptShareRideRequest(Long rideId, String clientEmail) throws IOException, InterruptedException;

    void declineShareRideRequest(Long rideId);
    CoordinateResponse getDriverStartingLocation(Long rideId);

    CoordinateResponse getRideStartingLocation(Long rideId);
    void updateVehicleLocation(Long rideId, CoordinateRequest request);

    List<CoordinateResponse> startRide(Long rideId);

    void endRide(Long rideId);

    RideReviewResponse getRideForReviewResponse(String email, Long rideId);

    void reviewRide(String email, ReviewRideRequest request);

    void declineAssignedRide(Long rideId);

    void submitCancellingNote(Long rideId, String note);

    void submitDriverReport(String email, Long rideId, String reason);

    Page<RidePreviewResponse> getUserRides(Long id, Pageable pageable);

    void createRideFromRideId(Long rideId, String email) throws InterruptedException;
}
