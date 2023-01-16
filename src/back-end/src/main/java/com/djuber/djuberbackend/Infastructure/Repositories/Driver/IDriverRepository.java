package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
