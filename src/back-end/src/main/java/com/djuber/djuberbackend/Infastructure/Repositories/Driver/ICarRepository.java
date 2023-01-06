package com.djuber.djuberbackend.Infastructure.Repositories.Driver;

import com.djuber.djuberbackend.Domain.Driver.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car c where c.licensePlate = ?1 and c.deleted = false")
    Car findCarByLicensePlate(String licensePlate);
}
