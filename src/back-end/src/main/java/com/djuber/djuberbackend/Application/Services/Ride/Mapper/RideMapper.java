package com.djuber.djuberbackend.Application.Services.Ride.Mapper;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Domain.Route.Route;

import javax.validation.constraints.AssertTrue;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RideMapper {
    private RideMapper() {}

    public static Ride map(RideRequest rideRequest) {
        Ride ride = new Ride();

        RideType rideType = RideType.fromString(rideRequest.getRideType());
        ride.setRideType(rideType);
        ride.setRequestedServices(rideRequest.getAdditionalServices());


        ride.setRoute(new Route());
        for (CoordinateRequest cr : rideRequest.getCoordinates()) {
            Coordinate coordinate = new Coordinate();
            coordinate.setIndex(cr.getIndex());
            coordinate.setLat(cr.getLat());
            coordinate.setLon(cr.getLon());

            ride.getRoute().getCoordinates().add(coordinate);
        }
        ride.getRoute().setDeleted(false);


        CarType carType = CarType.fromString(rideRequest.getCarType());

        Double price = carType.getBasePrice() + rideRequest.getDistance() * 120;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        ride.setPrice(Double.valueOf(twoDForm.format(price)));


        ride.setRideStatus(RideStatus.PENDING);
        ride.setDeleted(false);

        return ride;
    }

    public static List<CoordinateResponse> map(List<Coordinate> coordinates){
        List<CoordinateResponse> coordinateResponses = new ArrayList<>();
        for(Coordinate coordinate : coordinates){
            coordinateResponses.add(new CoordinateResponse(coordinate));
        }
        return coordinateResponses;
    }

    public static RideResponse mapResponse(Ride ride, List<Coordinate> coordinates) {
        RideResponse response = new RideResponse();
        response.setDriverName(ride.getDriver().getFirstName());
        response.setPrice(ride.getPrice());
        response.setRideStatus(ride.getRideStatus().toString());
        response.setClientEmails(new HashSet<>());
        for (Client client : ride.getClients()) {
            response.getClientEmails().add(client.getIdentity().getEmail());
        }

        response.setCoordinates(new ArrayList<>());
        for (Coordinate coordinate : coordinates) {
            CoordinateResponse coordinateResponse = new CoordinateResponse();
            coordinateResponse.setIndex(coordinate.getIndex());
            coordinateResponse.setLat(coordinate.getLat());
            coordinateResponse.setLon(coordinate.getLon());

            response.getCoordinates().add(coordinateResponse);
        }

        return response;
    }
}
