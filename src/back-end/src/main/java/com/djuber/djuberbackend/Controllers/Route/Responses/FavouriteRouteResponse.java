package com.djuber.djuberbackend.Controllers.Route.Responses;

import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavouriteRouteResponse {

    Long id;

    List<String> stopNames;

    List<CoordinateResponse> coordinates;
}
