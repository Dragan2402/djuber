package com.djuber.djuberbackend.Controllers.Reservation.Response;

import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {
    Set<String> clientEmails;
    Double price;
    OffsetDateTime start;
    List<CoordinateResponse> coordinates;
}
