package com.djuber.djuberbackend.Controllers.Ride.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RideResponse {
    Set<String> clientEmails;
    String driverName;
    Double price;
    List<CoordinateResponse> coordinates;
}
