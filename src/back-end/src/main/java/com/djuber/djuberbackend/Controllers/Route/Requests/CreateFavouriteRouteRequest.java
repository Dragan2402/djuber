package com.djuber.djuberbackend.Controllers.Route.Requests;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFavouriteRouteRequest {

    @NotEmpty(message = "Stop names are required.")
    @Size(min = 2)
    List<String> stopNames;

    @NotEmpty(message = "Coordinates are required.")
    @Size(min = 2)
    List<CoordinateRequest> coordinates;
}
