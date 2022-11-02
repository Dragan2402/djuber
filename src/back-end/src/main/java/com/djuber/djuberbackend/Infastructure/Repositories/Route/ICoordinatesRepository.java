package com.djuber.djuberbackend.Infastructure.Repositories.Route;

import com.djuber.djuberbackend.Domain.Route.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICoordinatesRepository extends JpaRepository<Coordinates, Long> {
}
