package com.djuber.djuberbackend.Infastructure.Repositories.Route;

import com.djuber.djuberbackend.Domain.Route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRouteRepository extends JpaRepository<Route, Long> {
}
