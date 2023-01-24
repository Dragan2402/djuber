package com.djuber.djuberbackend.Application.Services.Ride.Mapper;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Review.Review;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Domain.Route.Route;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RideMapper {
    public static Ride map(RideRequest rideRequest) {
        Ride ride = new Ride();

//        ride.setRideType(rideRequest.getRideType());


        ride.setRoute(new Route());
        ride.getRoute().setDeleted(false);

        for (CoordinateRequest cr : rideRequest.getCoordinates()) {
            Coordinate coordinate = new Coordinate();
            coordinate.setIndex(cr.getIndex());
            coordinate.setLat(cr.getLat());
            coordinate.setLon(cr.getLon());

            ride.getRoute().getCoordinates().add(coordinate);
        }
        ride.setRideStatus(RideStatus.PENDING);
        ride.setDeleted(false);

        return ride;
    }
}
