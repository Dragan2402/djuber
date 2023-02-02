package com.djuber.djuberbackend.BackendTesting.ServiceTesting;
import com.djuber.djuberbackend.Application.Services.Ride.Implementation.RideService;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideUpdateResponse;
import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.Authentication.UserType;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Client.ClientSigningType;
import com.djuber.djuberbackend.Domain.Driver.Car;
import com.djuber.djuberbackend.Domain.Driver.CarType;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Ride;
import com.djuber.djuberbackend.Domain.Ride.RideStatus;
import com.djuber.djuberbackend.Domain.Ride.RideType;
import com.djuber.djuberbackend.Domain.Route.Coordinate;

import com.djuber.djuberbackend.Domain.Route.Route;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.CannotUpdateCanceledRideException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.EntityNotFoundException;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.ICarRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IRideRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.ICoordinatesRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.time.OffsetDateTime;
import java.util.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideServiceTest {
    //updateVehicleLocation
    //endRide
    //startRide
    //getRideStartingLocation
    //getDriverStartingLocation

    @Autowired
    private RideService rideService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private IRideRepository rideRepository;

    @MockBean
    private ICarRepository carRepository;

    @MockBean
    private ICoordinatesRepository coordinatesRepository;

    private Ride validRide;

    private Ride canceledRide;

    @BeforeAll
    public void setup(){
        Identity driverIdentity = new Identity(1L,"driver@mailrop.cc", "password", UserType.DRIVER,new ArrayList<>(), null, null, false);
        Identity clientIdentity = new Identity(2L,"client@mailrop.cc", "password", UserType.CLIENT,new ArrayList<>(), null, null, false);

        Client client = new Client(1L, clientIdentity, "Client" , "LClient" , "Novi Sad", "123345", true, 5512D,ClientSigningType.DEFAULT,new HashSet<>(),new HashSet<>(),new HashSet<>(), new HashSet<>(), false,false,"",new Date(),"",false);
        List<Client> clients = new ArrayList<>();
        clients.add(client);

        Car car = new Car(1L, CarType.SEDAN, "1234556", null , 123D, 213D , new HashSet<>(), false);
        Driver driver = new Driver(2L, driverIdentity,"Gage", "Vozac", "Brcko","1233412312",true,new HashSet<>(),new HashSet<>(),car, OffsetDateTime.now(), false, false,"",false);
        car.setDriver(driver);

        Coordinate coordinate1 = new Coordinate(1L, 0, "Bulevar 123", 1.23D, 12.123D, null, null);
        Coordinate coordinate2 = new Coordinate(2L, 1, "Bulevar 555", 12.6D, 7.3D, null, null);
        Coordinate coordinate3 = new Coordinate(3L, 2, "Bulevar 999", 57.3D, 99.8D, null, null);
        List<Coordinate> coordinateList = new ArrayList<>();
        coordinateList.add(coordinate1);
        coordinateList.add(coordinate2);
        coordinateList.add(coordinate3);

        Route route = new Route(1L, null, null ,coordinateList, new ArrayList<>(),false);

        Ride ride = new Ride(1L, driver, clients ,new HashSet<>(), RideType.SINGLE, CarType.SEDAN, OffsetDateTime.now(), null, null, route, 213D, RideStatus.ON_THE_WAY, new HashSet<>(), new HashSet<>(),false);
        client.getRides().add(ride);
        this.validRide = ride;
        Ride rideCanceled = new Ride(2L, driver, clients ,new HashSet<>(), RideType.SINGLE,CarType.SEDAN,  OffsetDateTime.now(), null, null, route, 213D, RideStatus.CANCELED, new HashSet<>(), new HashSet<>(),false);
        client.getRides().add(rideCanceled);
        this.canceledRide = rideCanceled;
    }


    @Test()
    public void shouldReturnDriverStartingCoordinate(){
        validRide.getDriver().getCar().setLat(123D);
        validRide.getDriver().getCar().setLon(213D);
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.ofNullable(this.validRide));
        CoordinateResponse coordinateResult = this.rideService.getDriverStartingLocation(1L);
        assertEquals(0,coordinateResult.getIndex());
        assertEquals(123D, coordinateResult.getLat());
        assertEquals(213D, coordinateResult.getLon());
        verify(rideRepository, times(1)).findById(1L);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfDriverStartingLocationWhenRideIsNotExisting(){
        Mockito.when(rideRepository.findById(3L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.getDriverStartingLocation(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldReturnRideStartingCoordinate(){
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.ofNullable(this.validRide));

        Mockito.when(coordinatesRepository.findFirstCoordinateByRideId(1L)).thenReturn(this.validRide.getRoute().getCoordinates().get(0));
        CoordinateResponse coordinateResult = this.rideService.getRideStartingLocation(1L);

        assertEquals(0,coordinateResult.getIndex());
        assertEquals(1.23D,  coordinateResult.getLat());
        assertEquals(12.123D, coordinateResult.getLon());
        verify(rideRepository, times(1)).findById(1L);
        verify(coordinatesRepository, times(1)).findFirstCoordinateByRideId(1L);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfRideStartingLocationWhenRideIsNotExisting(){
        Mockito.when(rideRepository.findById(3L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.getRideStartingLocation(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfRideStartingLocationWhenCoordinateIsNotExisting(){
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.of(this.validRide));
        Mockito.when(coordinatesRepository.findFirstCoordinateByRideId(1L)).thenReturn(null);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.getRideStartingLocation(1L));
        assertEquals("Coordinate not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(1L);
        verify(coordinatesRepository, times(1)).findFirstCoordinateByRideId(1L);
    }


    @Test()
    public void shouldReturnRideCoordinatesAndUpdateRideStatus(){
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.ofNullable(this.validRide));
        Mockito.when(coordinatesRepository.findByRouteId(1L)).thenReturn(this.validRide.getRoute().getCoordinates());
        List<CoordinateResponse> coordinateResponse = this.rideService.startRide(1L);

        assertEquals(3,coordinateResponse.size());
        assertEquals(1.23D, coordinateResponse.get(0).getLat());
        assertEquals(7.3D, coordinateResponse.get(1).getLon());
        assertEquals(57.3D, coordinateResponse.get(2).getLat());
        verify(rideRepository, times(1)).findById(1L);
        verify(coordinatesRepository, times(1)).findByRouteId(1L);
        verify(rideRepository, times(1)).save(Mockito.any(Ride.class));
        verify(simpMessagingTemplate,times(1)).convertAndSend(any(String.class), any(RideUpdateResponse.class));
    }


    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfListOfCoordinatesWhenRideIsNotExisting(){
        Mockito.when(rideRepository.findById(3L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.startRide(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldEndRide(){
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.ofNullable(this.validRide));
        rideService.endRide(1L);
        verify(rideRepository, times(1)).findById(1L);
        verify(rideRepository,times(1)).save(Mockito.any(Ride.class));
        verify(simpMessagingTemplate,times(1)).convertAndSend(any(String.class), any(RideUpdateResponse.class));
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfEndingRideWhenRideIsNotExisting(){
        Mockito.when(rideRepository.findById(3L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.endRide(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldUpdateVehicleLocation(){
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.ofNullable(this.validRide));
        CoordinateRequest request = new CoordinateRequest();
        request.setLat(23D);
        request.setLon(23D);
        request.setIndex(0);
        rideService.updateVehicleLocation(1L, request);
        verify(rideRepository, times(1)).findById(1L);
        verify(carRepository,times(1)).save(Mockito.any(Car.class));
        verify(simpMessagingTemplate,times(1)).convertAndSend(any(String.class), any(RideUpdateResponse.class));
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfUpdatingVehicleLocationWhenRideIsNotExisting(){
        Mockito.when(rideRepository.findById(3L)).thenReturn(Optional.empty());
        CoordinateRequest request = new CoordinateRequest();
        request.setLat(23D);
        request.setLon(23D);
        request.setIndex(0);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.updateVehicleLocation(3L, request));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldThrowCannotUpdateCanceledRideExceptionInsteadOfUpdatingVehicleLocationWhenRideIsCanceled(){
        Mockito.when(rideRepository.findById(2L)).thenReturn(Optional.of(this.canceledRide));
        CoordinateRequest request = new CoordinateRequest();
        request.setLat(23D);
        request.setLon(23D);
        request.setIndex(0);
        CannotUpdateCanceledRideException exception = assertThrows(CannotUpdateCanceledRideException.class, () -> this.rideService.updateVehicleLocation(2L, request));
        assertEquals("The ride you are trying to update has been canceled.", exception.getMessage());
        verify(rideRepository, times(1)).findById(2L);
        verify(simpMessagingTemplate,times(1)).convertAndSend(any(String.class), any(RideUpdateResponse.class));
    }

}
