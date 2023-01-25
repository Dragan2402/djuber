package com.djuber.djuberbackend.Application.Services.Route.Mapper;

import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Route.Requests.CreateFavouriteRouteRequest;
import com.djuber.djuberbackend.Controllers.Route.Responses.FavouriteRouteResponse;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Domain.Route.FavouriteRoute;
import com.djuber.djuberbackend.Domain.Route.Route;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.IFavouriteRouteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;


public class RouteMapper {

    public static FavouriteRoute map(CreateFavouriteRouteRequest request) {
        FavouriteRoute favouriteRoute = new FavouriteRoute();

        for (CoordinateRequest cr : request.getCoordinates()) {
            Coordinate coordinate = new Coordinate();
            coordinate.setFavouriteRoute(favouriteRoute);
            coordinate.setIndex(cr.getIndex());
            coordinate.setLat(cr.getLat());
            coordinate.setLon(cr.getLon());
            favouriteRoute.getCoordinates().add(coordinate);
        }

        favouriteRoute.setStopNames(new ArrayList<>());

        for(String stopName : request.getStopNames()){
            favouriteRoute.getStopNames().add(stopName);
        }
        return favouriteRoute;
    }

    public static List<CoordinateResponse> mapCoordinates(List<Coordinate> coordinates){
        List<CoordinateResponse> coordinateResponses = new ArrayList<>();
        for(Coordinate coordinate : coordinates){
            coordinateResponses.add(new CoordinateResponse(coordinate));
        }
        return coordinateResponses;
    }
}
