package com.djuber.djuberbackend.Infastructure.Repositories.Route;

import com.djuber.djuberbackend.Domain.Route.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoordinatesRepository extends JpaRepository<Coordinates, Long> {
}
