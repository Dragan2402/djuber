package com.djuber.djuberbackend.BackendTesting.RepositoryTests;

import com.djuber.djuberbackend.Domain.Route.Coordinate;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.ICoordinatesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CoordinateRepositoryTest {

    @Autowired
    private ICoordinatesRepository coordinatesRepository;

    @Test
    public void shouldReturnStartingCoordinateByRouteId(){
        Coordinate coordinate = coordinatesRepository.findFirstCoordinateByRideId(100000L);
        assertThat(coordinate).isNotNull();
        assertThat(coordinate.getIndex()).isEqualTo(0);
    }

    @Test
    public void shouldReturnListOfCoordinatesByRouteId(){
        List<Coordinate> coordinates = coordinatesRepository.findByRouteId(100000L);
        assertThat(coordinates).hasSize(3);
    }

    @Test
    public void shouldReturnNullStartingCoordinateByRouteId(){
        Coordinate coordinate = coordinatesRepository.findFirstCoordinateByRideId(111000L);
        assertThat(coordinate).isNull();
    }

    @Test
    public void shouldReturnEmptyListOfCoordinatesByRouteId(){
        List<Coordinate> coordinates = coordinatesRepository.findByRouteId(111000L);
        assertThat(coordinates).hasSize(0);
    }
}
