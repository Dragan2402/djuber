package com.djuber.djuberbackend.Application.Services.Route.Implementation;

import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Application.Services.Route.IRouteService;
import com.djuber.djuberbackend.Application.Services.Route.Mapper.RouteMapper;
import com.djuber.djuberbackend.Controllers.Route.Requests.CreateFavouriteRouteRequest;
import com.djuber.djuberbackend.Controllers.Route.Responses.FavouriteRouteResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Route.FavouriteRoute;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.EntityNotFoundException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.ICoordinatesRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.IFavouriteRouteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteService implements IRouteService {

    final IFavouriteRouteRepository favouriteRouteRepository;

    final IIdentityRepository identityRepository;

    final IClientRepository clientRepository;

    final ICoordinatesRepository coordinatesRepository;

    @Override
    public Long createFavouriteRoute(String email, CreateFavouriteRouteRequest request) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Client client = clientRepository.findByIdentityId(identity.getId());

        FavouriteRoute newFavouriteRoute = RouteMapper.map(request);
        newFavouriteRoute.setClient(client);
        FavouriteRoute saved = favouriteRouteRepository.save(newFavouriteRoute);
        coordinatesRepository.saveAll(newFavouriteRoute.getCoordinates());
        return saved.getId();
    }

    @Override
    public List<FavouriteRouteResponse> getFavouriteRoutesByEmail(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Client client = clientRepository.findByIdentityId(identity.getId());

        List<FavouriteRoute> favouriteRoutes = favouriteRouteRepository.findByClientId(client.getId());
        List<FavouriteRouteResponse> responses = new ArrayList<>();
        for(FavouriteRoute favouriteRoute: favouriteRoutes){
            FavouriteRouteResponse newResponse = new FavouriteRouteResponse();
            newResponse.setId(favouriteRoute.getId());
            newResponse.setStopNames(favouriteRoute.getStopNames());
            newResponse.setCoordinates(RouteMapper.mapCoordinates(coordinatesRepository.findCoordinatesByFavouriteRouteId(favouriteRoute.getId())));
            responses.add(newResponse);
        }
        return responses;
    }

    @Override
    public void deleteFavouriteRouteById(Long routeId) {
        FavouriteRoute favouriteRoute = favouriteRouteRepository.findById(routeId).orElse(null);
        if(favouriteRoute == null){
            throw new EntityNotFoundException("Favourite route was not found.");
        }
        favouriteRouteRepository.delete(favouriteRoute);
    }
}