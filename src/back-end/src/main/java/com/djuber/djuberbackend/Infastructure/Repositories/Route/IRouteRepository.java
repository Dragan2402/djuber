package com.djuber.djuberbackend.Infastructure.Repositories.Route;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRouteRepository extends JpaRepository<Route, Long> {
    @Query("select r from Route r join fetch r.stopNames where r.id = ?1")
    Route findRouteStopNames(Long id);


    @Query("select r from Route r " +
            "join fetch r.stopNames " +
            "where r.id = ?1")
    @Override
    Optional<Route> findById(Long aLong);
}
