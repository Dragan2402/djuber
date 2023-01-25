package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.DriverActiveLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface IDriverLogRepository extends JpaRepository<DriverActiveLog, Long> {

    @Query("select dal from DriverActiveLog dal where dal.driver.id = ?1 and dal.logEnd > ?2")
    public List<DriverActiveLog> getDriverActiveLogsByTimeWindow(Long driverId, OffsetDateTime yesterday);
}
