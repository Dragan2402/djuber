package com.djuber.djuberbackend.Infastructure.Repositories.Ride;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRideRepository extends JpaRepository<Ride, Long> {
    @Query("select r from Ride r " +
            "join fetch r.clients " +
            "where r.id = ?1")
    @Override
    Optional<Ride> findById(Long aLong);

    @Query( value="select r from Ride r  where r.driver.id = ?1",
            countQuery = "select count(r) from Ride r where r.driver.id = ?1")
    Page<Ride> findDriverRides(Long id, Pageable pageable);


}
