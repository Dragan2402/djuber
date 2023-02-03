package com.djuber.djuberbackend.Infastructure.Repositories.Ride;

import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.deleted=false and r.start >= ?1 and r.start <= ?2")
    List<Reservation> getAllEligibleReservationForProcessing(OffsetDateTime now, OffsetDateTime fifteenMinutesFromNow);

    @Query("select c.reservations from Client c where c.id= ?1")
    List<Reservation> getClientReservations(Long clientId);
}
