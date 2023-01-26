package com.djuber.djuberbackend.Application.Services.Ride.Results;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Value
@AllArgsConstructor
public class RideResult {

    Long id;
    String startCoordinateName;
    String endCoordinateName;
    Double price;
    OffsetDateTime start;
    OffsetDateTime finish;


    public RideResult(@NotNull Ride ride) {
        this.id = ride.getId();
        this.startCoordinateName = ride.getRoute().getStartCoordinate().getLocationName();
        this.endCoordinateName = ride.getRoute().getLastCoordinate().getLocationName();
        this.price = ride.getPrice();
        this.start = ride.getStart();
        this.finish = ride.getFinish();
    }
}

