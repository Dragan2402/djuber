package com.djuber.djuberbackend.Controllers.Ride.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRideRequest {

    @NotNull(message = "The car rating is required.")
    @Min(1)
    @Max(5)
    Double carRating;

    @NotNull(message = "The driver rating is required.")
    @Min(1)
    @Max(5)
    Double driverRating;

    @Size(max = 500, message = "The note is too long")
    String comment;

    @NotNull(message = "The ride id is required.")
    @Positive(message = "Invalid ride id.")
    Long rideId;
}
