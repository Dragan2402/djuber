package com.djuber.djuberbackend.Application.Services.Reservation.Implementation;

import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Application.Services.Reservation.IReservationService;
import com.djuber.djuberbackend.Controllers.Reservation.Requests.CreateReservationRequest;
import com.djuber.djuberbackend.Controllers.Reservation.Response.ReservationResponse;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Domain.Route.Route;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.InvalidReservationStartException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IReservationRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.ICoordinatesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationService implements IReservationService {

    final IReservationRepository reservationRepository;

    final IIdentityRepository identityRepository;

    final IClientRepository clientRepository;

    final ICoordinatesRepository coordinatesRepository;

    @Override
    public Long createReservation(CreateReservationRequest request) {
        List<Client> clients = new ArrayList<>();
        for(String email : request.getClientEmails()){
            Identity clientIdentity = identityRepository.findByEmail(email);
            if(clientIdentity == null){
                throw new UserNotFoundException("Client does not exist.");
            }
            clients.add(clientRepository.findByIdentityId(clientIdentity.getId()));
        }
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime fiveHoursFromNow = now.plusHours(5);
        if(request.getStart().compareTo(fiveHoursFromNow) >= 0 || request.getStart().compareTo(now) <= 0){
            throw new InvalidReservationStartException("Invalid reservation time");
        }

        Reservation reservation = new Reservation();
        reservation.setRideType(RideType.fromString(request.getRideType()));
        reservation.setRequestedServices(request.getAdditionalServices());
        reservation.setRoute(new Route());
        for (CoordinateRequest cr : request.getCoordinates()) {
            Coordinate coordinate = new Coordinate();
            coordinate.setRoute(reservation.getRoute());
            coordinate.setIndex(cr.getIndex());
            coordinate.setLat(cr.getLat());
            coordinate.setLon(cr.getLon());

            reservation.getRoute().getCoordinates().add(coordinate);
        }
        reservation.getRoute().setDeleted(false);


        CarType carType = CarType.fromString(request.getCarType());

        double price = carType.getBasePrice() + request.getDistance() * 120;
        price = (double) Math.round(price * 100);
        price = price/100;
        reservation.setPrice(price);
        reservation.setDeleted(false);

        for(Client client : clients){
            reservation.getClients().add(client);
        }


        Reservation reservationSaved = reservationRepository.save(reservation);
        coordinatesRepository.saveAll(reservationSaved.getRoute().getCoordinates());
        return  reservationSaved.getId();
    }

    @Override
    public List<ReservationResponse> getLoggedClientReservations(String email) {
        Identity identity = identityRepository.findByEmail(email);
        if(identity == null){
            throw new UserNotFoundException("Client with provided email does not exist.");
        }
        Client client = clientRepository.findByIdentityId(identity.getId());

        List<Reservation> reservations = reservationRepository.getClientReservations(client.getId());
        List<ReservationResponse> responses = new ArrayList<>();

        for(Reservation reservation : reservations){
            ReservationResponse response = new ReservationResponse();
            response.setClientEmails(new HashSet<>());
            response.setCoordinates(new ArrayList<>());
            response.setPrice(reservation.getPrice());
            response.setStart(reservation.getStart());
            for(Client reservationClient : reservation.getClients()){
                response.getClientEmails().add(reservationClient.getIdentity().getEmail());
            }
            List<Coordinate> coordinates = coordinatesRepository.findByRouteId(reservation.getRoute().getId());

            for(Coordinate coordinate : coordinates){
                response.getCoordinates().add(new CoordinateResponse(coordinate));
            }
            responses.add(response);
        }

        return responses;
    }
}
