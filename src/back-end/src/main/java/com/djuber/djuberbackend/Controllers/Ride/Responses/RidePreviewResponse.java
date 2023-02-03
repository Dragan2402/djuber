package com.djuber.djuberbackend.Controllers.Ride.Responses;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RidePreviewResponse {
    Long id;
    Double price;
    OffsetDateTime start;
    OffsetDateTime end;
    List<String> stopNames;

    public RidePreviewResponse(){}

    public RidePreviewResponse(Ride ride, List<String> stopNames){
        this.id = ride.getId();
        this.price = ride.getPrice();
        this.start = ride.getStart();
        this.end = ride.getFinish();
        this.stopNames = stopNames;
    }
}
