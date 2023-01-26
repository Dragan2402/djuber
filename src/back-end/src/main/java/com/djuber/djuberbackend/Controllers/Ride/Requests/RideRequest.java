package com.djuber.djuberbackend.Controllers.Ride.Requests;

import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideRequest {
    @NotEmpty(message = "Coordinates are required.")
    List<CoordinateRequest> coordinates;

    @NotEmpty(message = "Car type is required.")
    String carType;

    @NotEmpty(message = "Ride type is required.")
    String rideType;

    @NotNull(message = "The distance is required.")
    Double distance;

    @NotNull(message = "Must provide additional services set. Can be empty.")
    Set<String> additionalServices;

    @NotEmpty(message = "Email is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    List<String> clientEmails;
}
