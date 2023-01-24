package com.djuber.djuberbackend.Infastructure.Repositories.Route;

import com.djuber.djuberbackend.Domain.Route.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICoordinatesRepository extends JpaRepository<Coordinate, Long> {
    @Query("select c from Coordinate c where c.route.id = ?1")
    List<Coordinate> findByRouteId(Long id);

    @Query("select c from Coordinate c where c.route.id = ?1 and c.index = 0")
    Coordinate findFirstCoordinateByRideId(Long rideId);
}
