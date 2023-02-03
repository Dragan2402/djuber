package com.djuber.djuberbackend.Controllers.Reservation;

import com.djuber.djuberbackend.Application.Services.Reservation.IReservationService;
import com.djuber.djuberbackend.Controllers.Reservation.Requests.CreateReservationRequest;
import com.djuber.djuberbackend.Controllers.Reservation.Response.ReservationResponse;
import com.djuber.djuberbackend.Controllers._Common.Responses.IdResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "Reservation API", description = "Provides Reservation CRUD end-points")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "api/reservation")
public class ReservationController {

    final IReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<IdResponse> createReservation(@RequestBody @Valid CreateReservationRequest request){
        return new ResponseEntity<>(new IdResponse(reservationService.createReservation(request)), HttpStatus.CREATED);
    }

    @GetMapping(value = "loggedClientReservations")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<List<ReservationResponse>> getLoggedClientReservations(Principal principal){
        return new ResponseEntity<>(reservationService.getLoggedClientReservations(principal.getName()), HttpStatus.OK);
    }
}
