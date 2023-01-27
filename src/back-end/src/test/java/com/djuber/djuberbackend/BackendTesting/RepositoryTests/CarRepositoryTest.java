package com.djuber.djuberbackend.BackendTesting.RepositoryTests;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.ICarRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CarRepositoryTest {

    @Autowired
    private ICarRepository carRepository;

    @Autowired
    private IDriverRepository driverRepository;

    @Test
    public void shouldSaveRide(){
        Car car = new Car();
        car.setLon(21D);
        car.setLat(22D);
        car.setLicensePlate("123345");
        car.setDeleted(false);
        car.setCarType(CarType.SEDAN);
        car.setDriver(driverRepository.findById(100000L).orElse(null));
        Car carSaved = carRepository.save(car);
        assertThat(carSaved).usingRecursiveComparison().ignoringFields("id").isEqualTo(car);
    }


    @Test
    public void shouldReturnCarById(){
        Optional<Car> car = carRepository.findById(100000L);
        assertThat(car).isNotEmpty();
    }

    @Test
    public void shouldReturnRideNullById(){
        Optional<Car> car = carRepository.findById(110000L);
        assertThat(car).isEmpty();
    }
}
