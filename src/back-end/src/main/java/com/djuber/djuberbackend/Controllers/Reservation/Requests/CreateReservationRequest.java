package com.djuber.djuberbackend.Controllers.Reservation.Requests;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReservationRequest {
    @NotEmpty(message = "Coordinates are required.")
    List<CoordinateRequest> coordinates;

    @NotEmpty(message = "Car type is required.")
    String carType;

    @NotEmpty(message = "Ride type is required.")
    String rideType;

    @NotNull(message = "The distance is required.")
    Double distance;

    @NotNull(message = "Start date of the reservation is necessary")
    OffsetDateTime start;

    @NotNull(message = "Must provide additional services set. Can be empty.")
    Set<String> additionalServices;

    @NotEmpty(message = "Email is required.")
    List<String> clientEmails;
}
