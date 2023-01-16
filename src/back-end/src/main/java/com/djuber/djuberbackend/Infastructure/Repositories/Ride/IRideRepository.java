package com.djuber.djuberbackend.Infastructure.Repositories.Ride;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRideRepository extends JpaRepository<Ride, Long> {
}
