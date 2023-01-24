package com.djuber.djuberbackend.Controllers.Ride.Requests;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoordinateRequest {
    @NotEmpty(message = "Index is required.")
    Integer index;

    @NotEmpty(message = "Latitude is required.")
    Double lat;

    @NotEmpty(message = "Longitude is required.")
    Double lon;
}
