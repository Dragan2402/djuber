package com.djuber.djuberbackend.Infastructure.Repositories.Ride;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservationRepository extends JpaRepository<Ride, Long> {
}
