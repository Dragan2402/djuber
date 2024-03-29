package com.djuber.djuberbackend.Controllers.Ride.Requests;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoordinateRequest {
    @NotEmpty(message = "Index is required.")
    Integer index;

    @NotNull(message = "The latitude is required.")
    Double lat;

    @NotNull(message = "The longitude is required.")
    Double lon;
}
