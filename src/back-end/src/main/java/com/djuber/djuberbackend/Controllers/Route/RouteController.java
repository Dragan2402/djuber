package com.djuber.djuberbackend.Controllers.Route;

import com.djuber.djuberbackend.Application.Services.Route.IRouteService;
import com.djuber.djuberbackend.Controllers.Route.Requests.CreateFavouriteRouteRequest;
import com.djuber.djuberbackend.Controllers.Route.Responses.FavouriteRouteResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.IdResponse;
import com.djuber.djuberbackend.Domain.Route.FavouriteRoute;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "Route API", description = "Provides route CRUD end-points")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/route")
public class RouteController {

    final IRouteService routeService;

    @PostMapping("/favouriteRoute")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<IdResponse> createFavouriteRoute(Principal principal, @RequestBody @Valid CreateFavouriteRouteRequest request){
        return new ResponseEntity<>(new IdResponse(routeService.createFavouriteRoute(principal.getName(), request)), HttpStatus.CREATED);
    }

    @GetMapping(value = "/loggedClientFavouriteRoutes")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<List<FavouriteRouteResponse>> getLoggedClientFavouriteRoutes(Principal principal){
        return new ResponseEntity<>(routeService.getFavouriteRoutesByEmail(principal.getName()),HttpStatus.OK);
    }

    @DeleteMapping(value = "/favouriteRoute/{routeId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public void deleteRouteById(@PathVariable("routeId") Long routeId){
      routeService.deleteFavouriteRouteById(routeId);
    }
}
