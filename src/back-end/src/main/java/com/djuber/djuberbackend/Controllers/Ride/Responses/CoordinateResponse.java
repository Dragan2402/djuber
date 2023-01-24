package com.djuber.djuberbackend.Controllers.Ride.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoordinateResponse {
    Integer index;
    Double lat;
    Double lon;
}
