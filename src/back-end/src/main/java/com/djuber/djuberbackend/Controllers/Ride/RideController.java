package com.djuber.djuberbackend.Controllers.Ride;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Application.Services.Ride.Mapper.RideMapper;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Domain.Route.Route;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ride API", description = "Provides Ride CRUD end-points")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/ride")
public class RideController {
    private final IRideService rideService;

    @GetMapping("/driver")
    @PreAuthorize("hasAnyRole('CLIENT','DRIVER')")
    public ResponseEntity<Void> sendRideRequestToClosestFittingDriver(@RequestBody RideRequest rideRequest) {
        rideService.getClosestFittingDriver(rideRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
