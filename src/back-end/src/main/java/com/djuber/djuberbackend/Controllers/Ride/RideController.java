package com.djuber.djuberbackend.Controllers.Ride;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/driver")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> offerRideToDriver(@RequestBody @Valid RideRequest rideRequest, Principal principal) {
        RideType rideType = RideType.fromString(rideRequest.getRideType());
        if (rideType == RideType.SINGLE) {
            rideService.offerSingleRideToDriver(rideRequest);
        } else {
            rideService.offerSharedRideToClients(rideRequest, principal.getName());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/driver/accept/{rideId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Void> acceptRideDriverOffer(@PathVariable("rideId") Long rideId) throws IOException, InterruptedException {
        rideService.acceptRideDriverOffer(rideId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/driver/decline/{rideId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Void> declineRideDriverOffer(@PathVariable("rideId") Long rideId) {
        rideService.declineRideDriverOffer(rideId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/client/accept/{rideId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Void> acceptRideClientOffer(@PathVariable("rideId") Long rideId, Principal principal) {
        rideService.acceptRideClientOfferAndSendDriverOffer(rideId, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/client/decline/{rideId}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<Void> declineRideClientOffer(@PathVariable("rideId") Long rideId) {
        rideService.declineRideClientOffer(rideId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'DRIVER')")
    public ResponseEntity<RideResponse> getRide(@PathVariable("rideId") Long rideId) {
        return new ResponseEntity<>(rideService.getRideResponse(rideId), HttpStatus.OK);
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
