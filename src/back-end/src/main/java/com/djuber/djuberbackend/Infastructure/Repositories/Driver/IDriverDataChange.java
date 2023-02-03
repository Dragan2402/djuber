package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.DriverDataChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDriverDataChange extends JpaRepository<DriverDataChange, Long> {

    @Query("select dc from DriverDataChange dc where dc.driver.id = ?1")
    DriverDataChange findByDriverId(Long driverId);
}
