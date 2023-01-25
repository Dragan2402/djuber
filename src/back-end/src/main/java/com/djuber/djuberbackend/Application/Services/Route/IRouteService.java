package com.djuber.djuberbackend.Application.Services.Route;

import com.djuber.djuberbackend.Controllers.Route.Requests.CreateFavouriteRouteRequest;
import com.djuber.djuberbackend.Controllers.Route.Responses.FavouriteRouteResponse;

import java.util.List;

public interface IRouteService {

    Long createFavouriteRoute(String email, CreateFavouriteRouteRequest request);

    List<FavouriteRouteResponse> getFavouriteRoutesByEmail(String email);

    void deleteFavouriteRouteById(Long routeId);
}
