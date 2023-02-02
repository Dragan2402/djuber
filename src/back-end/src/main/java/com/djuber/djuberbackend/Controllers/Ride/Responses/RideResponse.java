package com.djuber.djuberbackend.Controllers.Ride.Responses;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideResponse {
    Set<String> clientEmails;
    String driverName;
    Double price;
    String rideStatus;
    List<CoordinateResponse> coordinates;
    Long id;
}
