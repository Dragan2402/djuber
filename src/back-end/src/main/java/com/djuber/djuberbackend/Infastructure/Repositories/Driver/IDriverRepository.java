package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Route.Coordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDriverRepository extends JpaRepository<Driver, Long> {

    @Query("select d from Driver d where d.identity.id = ?1 and d.deleted = false")
    Driver findByIdentityId(Long identityId);

    @Query("select d from Driver d where lower(d.firstName) like lower(concat('%', ?1,'%') ) or lower(d.lastName) like lower(concat('%', ?1,'%') ) " +
            "or lower(d.car.licensePlate) like lower(concat('%', ?1,'%') ) " +
            "or concat(d.id,'') like lower(concat('%', ?1,'%') ) " +
            "or lower(d.identity.email) like lower(concat('%', ?1,'%') ) " +
            "or lower(d.city) like lower(concat('%', ?1,'%') ) " +
            "or lower(d.car.carType) like lower(concat('%', ?1,'%') ) ")
    Page<Driver> findAllWithFilter(String filter, Pageable pageable);

    @Query("select d from Driver d where d.deleted=false and d.active=true and d.inRide=false and d.blocked=false")
    List<Driver> getAvailableDrivers();

    @Query("select d from Driver d " +
            "where d.deleted=false and d.active=true and d.inRide=false and d.blocked=false " +
            "order by (abs(d.car.lat - :#{#c.lat}) + abs(d.car.lon - :#{#c.lon})) asc")
    List<Driver> findAvailableDriversSortedByDistanceFromCoordinate(@Param("c") Coordinate coordinate);

    @Query("select d from Driver d where d.active=true")
    List<Driver> getActiveDrivers();
}
