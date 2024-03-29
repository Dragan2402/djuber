package com.djuber.djuberbackend.Controllers.Ride;

import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Application.Services.Ride.IRideService;

import com.djuber.djuberbackend.Application.Services.Ride.Results.RideResult;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CancellingNoteRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.ReviewRideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.*;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RidePreviewResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideReviewResponse;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Tag(name = "Ride API", description = "Provides Ride CRUD end-points")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/ride")
public class RideController {
    private final IRideService rideService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Page<RideResult>> getRides(Principal user, Pageable pageable){
        return new ResponseEntity<>(rideService.readPageable(pageable, user.getName()), HttpStatus.OK);
    }

    @GetMapping(value = "/userRides/{identityId}")
    @PreAuthorize("hasAnyRole('CLIENT','DRIVER','ADMIN')")
    public ResponseEntity<Page<RidePreviewResponse>> getUserRides(@PathVariable("identityId") Long identityId, Pageable pageable){
        return new ResponseEntity<>(rideService.getUserRides(identityId, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<List<RideResult>> getAllRides() {
        return new ResponseEntity<>(rideService.getAllRides(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> createRide(@RequestBody @Valid RideRequest rideRequest) throws IOException, InterruptedException {
        rideService.processRideRequest(rideRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/createRideFromRideId/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> createRideFromRideId(@PathVariable("rideId") Long rideId, Principal principal) throws IOException, InterruptedException {
        rideService.createRideFromRideId(rideId, principal.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/client/accept/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> acceptShareRide(@PathVariable("rideId") Long rideId, Principal principal) throws IOException, InterruptedException {
        rideService.acceptShareRideRequest(rideId, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/client/decline/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> declineShareRide(@PathVariable("rideId") Long rideId) {
        rideService.declineShareRideRequest(rideId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/declineAssignedRide/{rideId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Void> declineAssignedRide(@PathVariable("rideId") Long rideId) {
        rideService.declineAssignedRide(rideId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/submitCancellingNote/{rideId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Void> submitCancellingNote(@PathVariable("rideId") Long rideId, @RequestBody CancellingNoteRequest request) {
        rideService.submitCancellingNote(rideId, request.getNote());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/submitDriverReport/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> submitDriverReport(Principal principal, @PathVariable("rideId") Long rideId, @RequestBody DriverReportRequest request) {
        rideService.submitDriverReport(principal.getName(), rideId, request.getReason());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'DRIVER', 'ADMIN')")
    public ResponseEntity<RideResponse> getRide(@PathVariable("rideId") Long rideId) {
        return new ResponseEntity<>(rideService.getRideResponse(rideId), HttpStatus.OK);
    }

    @GetMapping("/rideForReview/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<RideReviewResponse> getRideForReview(Principal principal, @PathVariable("rideId") Long rideId) {
        return new ResponseEntity<>(rideService.getRideForReviewResponse(principal.getName(),rideId), HttpStatus.OK);
    }

    @PostMapping("/reviewRide")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public void reviewRide(Principal principal, @RequestBody @Valid ReviewRideRequest request) {
        rideService.reviewRide(principal.getName(),request);
    }

    @GetMapping("/script/getDriverStartingLocation/{rideId}")
    public ResponseEntity<CoordinateResponse> getDriverStartingLocation(@PathVariable("rideId") Long rideId) {
        return new ResponseEntity<>(rideService.getDriverStartingLocation(rideId), HttpStatus.OK);
    }

    @GetMapping("/script/getRideStartingLocation/{rideId}")
    public ResponseEntity<CoordinateResponse> getRideStartingLocation(@PathVariable("rideId") Long rideId) {
        return new ResponseEntity<>(rideService.getRideStartingLocation(rideId), HttpStatus.OK);
    }

    @GetMapping("/script/startRide/{rideId}")
    public ResponseEntity<List<CoordinateResponse>> startRide(@PathVariable("rideId") Long rideId) {
        return new ResponseEntity<>(rideService.startRide(rideId), HttpStatus.OK);
    }

    @PutMapping("/script/endRide/{rideId}")
    public void endRide(@PathVariable("rideId") Long rideId) {
        rideService.endRide(rideId);
    }

    @PutMapping("/script/updateVehicleLocation/{rideId}")
    public void updateVehicleLocation(@PathVariable("rideId") Long rideId, @RequestBody CoordinateRequest request) {
        rideService.updateVehicleLocation(rideId, request);
    }
}
