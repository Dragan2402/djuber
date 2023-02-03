package com.djuber.djuberbackend.Controllers.Ride.Responses;

import com.djuber.djuberbackend.Domain.Route.Coordinate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoordinateResponse {
    Integer index;
    Double lat;
    Double lon;

    public CoordinateResponse(){}

    public CoordinateResponse(Coordinate coordinate){
        this.index = coordinate.getIndex();
        this.lat = coordinate.getLat();
        this.lon = coordinate.getLon();
    }
}
