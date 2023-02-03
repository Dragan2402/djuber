package com.djuber.djuberbackend.Application.Services.Reservation;

import com.djuber.djuberbackend.Controllers.Reservation.Requests.CreateReservationRequest;
import com.djuber.djuberbackend.Controllers.Reservation.Response.ReservationResponse;

import java.util.List;

public interface IReservationService {

    Long createReservation(CreateReservationRequest request);

    List<ReservationResponse> getLoggedClientReservations(String email);
}
