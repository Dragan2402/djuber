package com.djuber.djuberbackend.BackendTesting.RepositoryTests;

import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import com.djuber.djuberbackend.Domain.Route.Route;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IRideRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RideRepositoryTest {

    @Autowired
    private IRideRepository rideRepository;

    @Autowired
    private IDriverRepository driverRepository;

    @Test
    public void shouldSaveRide(){
        Ride ride = new Ride();
        ride.setRideType(RideType.SINGLE);
        ride.setRideStatus(RideStatus.PENDING);
        ride.setStart(OffsetDateTime.now());
        ride.setDeleted(false);
        ride.setPrice(123D);
        Route route = new Route();
        route.setDeleted(false);
        route.setRide(ride);
        ride.setRoute(route);
        ride.setDriver(driverRepository.findById(100000L).orElse(null));
        Ride rideSaved = rideRepository.save(ride);

        assertThat(rideSaved).usingRecursiveComparison().ignoringFields("id").isEqualTo(ride);
    }


    @Test
    public void shouldReturnRideById(){
        Optional<Ride> ride = rideRepository.findById(100000L);
        assertThat(ride).isNotEmpty();
    }

    @Test
    public void shouldReturnRideNullById(){
        Optional<Ride> ride = rideRepository.findById(110000L);
        assertThat(ride).isEmpty();
    }

}
