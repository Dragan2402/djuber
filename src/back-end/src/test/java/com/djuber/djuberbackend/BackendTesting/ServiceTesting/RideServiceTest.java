package com.djuber.djuberbackend.BackendTesting.ServiceTesting;
import com.djuber.djuberbackend.Application.Services.Ride.Implementation.RideService;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
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
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.*;
import com.djuber.djuberbackend.Infastructure.Repositories.Authentication.IIdentityRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Client.IClientRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.ICarRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Review.IReviewRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IDriverReportRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IRideRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Route.ICoordinatesRepository;
import com.djuber.djuberbackend.Infastructure.Util.DateCalculator;
import org.junit.jupiter.api.*;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RideServiceTest {


    @InjectMocks
    private RideService rideService;

    @Mock
    private IRideRepository rideRepository;
    @Mock
    private IIdentityRepository identityRepository;
    @Mock
    private IClientRepository clientRepository;
    @Mock
    private IDriverRepository driverRepository;
    @Mock
    private ICoordinatesRepository coordinatesRepository;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private ICarRepository carRepository;
    @Mock
    private DateCalculator dateCalculator;
    @Mock
    private IReviewRepository reviewRepository;
    @Mock
    private IDriverReportRepository driverReportRepository;

    private Identity clientIdentity;
    private Identity client2Identity;

    private Identity driverIdentity;

    private Client client;
    private Client client2;

    private Driver driver;

    private Ride validRide;

    private Ride canceledRide;

    private Ride createdRide;

    private RideRequest rideRequest;
    
    private List<Coordinate> coordinates;
    
    private Route route;

    @BeforeEach
    public void setup(){
        Identity driverIdentity = new Identity(1L,"driver@mailrop.cc", "password", UserType.DRIVER,new ArrayList<>(), null, null, false);
        this.driverIdentity = driverIdentity;
        Identity clientIdentity = new Identity(2L,"client@mailrop.cc", "password", UserType.CLIENT,new ArrayList<>(), null, null, false);
        this.clientIdentity = clientIdentity;
        Identity client2Identity = new Identity(3L,"client2@mailrop.cc", "password", UserType.CLIENT, new ArrayList<>(), null, null, false);
        this.client2Identity = client2Identity;

        Client client = new Client(1L, clientIdentity, "Client" , "LClient" , "Novi Sad", "123345", true, 5512D, ClientSigningType.DEFAULT,new HashSet<>(),new HashSet<>(),new HashSet<>(), new HashSet<>(), false,false,"",new Date(),"",false);
        this.client = client;
        Client client2 = new Client(2L, client2Identity, "Client2" , "LClient2" , "Novi Sad", "1233456", true, 5513D, ClientSigningType.DEFAULT,new HashSet<>(),new HashSet<>(),new HashSet<>(), new HashSet<>(), false,false,"",new Date(),"",false);
        this.client2 = client2;
        List<Client> clients = new ArrayList<>();
        clients.add(client);

        Car car = new Car(1L, CarType.SEDAN, "1234556", null , 123D, 213D , new HashSet<>(), false);
        Driver driver = new Driver(2L, driverIdentity,"Gage", "Vozac", "Brcko","1233412312",true,new HashSet<>(),new HashSet<>(),car, OffsetDateTime.now(), false, false,"",false);
        this.driver = driver;
        car.setDriver(driver);

        Coordinate coordinate1 = new Coordinate(1L, 0, "Bulevar 123", 1.23D, 12.123D, null, null);
        Coordinate coordinate2 = new Coordinate(2L, 1, "Bulevar 555", 12.6D, 7.3D, null, null);
        Coordinate coordinate3 = new Coordinate(3L, 2, "Bulevar 999", 57.3D, 99.8D, null, null);
        List<Coordinate> coordinateList = new ArrayList<>();
        coordinateList.add(coordinate1);
        coordinateList.add(coordinate2);
        coordinateList.add(coordinate3);
        this.coordinates = coordinateList;

        Route route = new Route(1L, null, null ,coordinateList, new ArrayList<>(),false);
        this.route = route;

        Ride ride = new Ride(1L,
                driver,
                clients,
                new HashSet<>(),
                RideType.SINGLE,
                CarType.SEDAN,
                OffsetDateTime.now(),
                null,
                null,
                route,
                213D,
                RideStatus.ON_THE_WAY,
                new HashSet<>(),
                new HashSet<>(),
                false);
        client.getRides().add(ride);
        this.validRide = ride;
        Ride rideCanceled = new Ride(2L, driver, clients ,new HashSet<>(), RideType.SINGLE,CarType.SEDAN,  OffsetDateTime.now(), null, null, route, 213D, RideStatus.CANCELED, new HashSet<>(), new HashSet<>(),false);
        client.getRides().add(rideCanceled);
        this.canceledRide = rideCanceled;

        RideRequest rideRequest = new RideRequest();
        rideRequest.setCoordinates(List.of(
                new CoordinateRequest(0, 1.23D, 12.123D),
                new CoordinateRequest(1, 12.6D, 7.3D),
                new CoordinateRequest(2, 57.3D, 99.8D)
        ));
        rideRequest.setCarType("Sedan");
        rideRequest.setRideType("Single");
        rideRequest.setDistance(1.0);
        rideRequest.setAdditionalServices(Set.of());
        rideRequest.setClientEmails(List.of("client@maildrop.cc"));
        rideRequest.setStopNames(List.of("Bulevar 123", "Bulevar 999"));
        this.rideRequest = rideRequest;

        Ride rideCreated = new Ride();
        rideCreated.setId(3L);
        rideCreated.setClients(clients);
        rideCreated.setRideType(RideType.SINGLE);
        rideCreated.setCarType(CarType.SEDAN);
        rideCreated.setStart(OffsetDateTime.now());
        rideCreated.setRoute(route);
        rideCreated.setPrice(213D);
        rideCreated.setRideStatus(RideStatus.PENDING);
        rideCreated.setRequestedServices(Set.of());
        rideCreated.setClientsAccepted(Set.of(client.getIdentity().getEmail()));
        rideCreated.setDeleted(false);
        this.createdRide = rideCreated;
    }

    @Test()
    public void shouldReturnDriverStartingCoordinate(){
        validRide.getDriver().getCar().setLat(123D);
        validRide.getDriver().getCar().setLon(213D);
        given(rideRepository.findById(1L)).willReturn(Optional.ofNullable(this.validRide));
        CoordinateResponse coordinateResult = this.rideService.getDriverStartingLocation(1L);
        assertEquals(0,coordinateResult.getIndex());
        assertEquals(123D, coordinateResult.getLat());
        assertEquals(213D, coordinateResult.getLon());
        verify(rideRepository, times(1)).findById(1L);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfDriverStartingLocationWhenRideIsNotExisting(){
        given(rideRepository.findById(3L)).willReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.getDriverStartingLocation(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldReturnRideStartingCoordinate(){
        given(rideRepository.findById(1L)).willReturn(Optional.ofNullable(this.validRide));

        given(coordinatesRepository.findFirstCoordinateByRideId(1L)).willReturn(this.validRide.getRoute().getCoordinates().get(0));
        CoordinateResponse coordinateResult = this.rideService.getRideStartingLocation(1L);

        assertEquals(0,coordinateResult.getIndex());
        assertEquals(1.23D,  coordinateResult.getLat());
        assertEquals(12.123D, coordinateResult.getLon());
        verify(rideRepository, times(1)).findById(1L);
        verify(coordinatesRepository, times(1)).findFirstCoordinateByRideId(1L);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfRideStartingLocationWhenRideIsNotExisting(){
        given(rideRepository.findById(3L)).willReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.getRideStartingLocation(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfRideStartingLocationWhenCoordinateIsNotExisting(){
        given(rideRepository.findById(1L)).willReturn(Optional.of(this.validRide));
        given(coordinatesRepository.findFirstCoordinateByRideId(1L)).willReturn(null);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.getRideStartingLocation(1L));
        assertEquals("Coordinate not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(1L);
        verify(coordinatesRepository, times(1)).findFirstCoordinateByRideId(1L);
    }


    @Test()
    public void shouldReturnRideCoordinatesAndUpdateRideStatus(){
        given(rideRepository.findById(1L)).willReturn(Optional.ofNullable(this.validRide));
        given(coordinatesRepository.findByRouteId(1L)).willReturn(this.validRide.getRoute().getCoordinates());
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
        given(rideRepository.findById(3L)).willReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.startRide(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldEndRide(){
        given(rideRepository.findById(1L)).willReturn(Optional.ofNullable(this.validRide));
        rideService.endRide(1L);
        verify(rideRepository, times(1)).findById(1L);
        verify(rideRepository,times(1)).save(Mockito.any(Ride.class));
        verify(simpMessagingTemplate,times(1)).convertAndSend(any(String.class), any(RideUpdateResponse.class));
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionInsteadOfEndingRideWhenRideIsNotExisting(){
        given(rideRepository.findById(3L)).willReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.endRide(3L));
        assertEquals("Ride not found.", exception.getMessage());
        verify(rideRepository, times(1)).findById(3L);
    }

    @Test()
    public void shouldUpdateVehicleLocation(){
        given(rideRepository.findById(1L)).willReturn(Optional.ofNullable(this.validRide));
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
        given(rideRepository.findById(3L)).willReturn(Optional.empty());
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
        given(rideRepository.findById(2L)).willReturn(Optional.of(this.canceledRide));
        CoordinateRequest request = new CoordinateRequest();
        request.setLat(23D);
        request.setLon(23D);
        request.setIndex(0);
        CannotUpdateCanceledRideException exception = assertThrows(CannotUpdateCanceledRideException.class, () -> this.rideService.updateVehicleLocation(2L, request));
        assertEquals("The ride you are trying to update has been canceled.", exception.getMessage());
        verify(rideRepository, times(1)).findById(2L);
        verify(simpMessagingTemplate,times(1)).convertAndSend(any(String.class), any(RideUpdateResponse.class));
    }

    @Test()
    public void shouldCancelRide(){
        given(rideRepository.findById(1L)).willReturn(Optional.ofNullable(this.validRide));
        rideService.declineAssignedRide(1L);
        assertEquals(RideStatus.CANCELED, this.validRide.getRideStatus());
        assertEquals(false, this.validRide.getDriver().getInRide());
        verify(rideRepository, times(1)).save(this.validRide);
    }

    @Test()
    public void shouldThrowEntityNotFoundExceptionWhenCancellingRide(){
        given(rideRepository.findById(3L)).willReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.rideService.declineAssignedRide(3L));
        assertEquals("Ride to review not found.", exception.getMessage());
        verify(rideRepository,times(1)).findById(3L);
    }

    @Test()
    public void shouldThrowRideNotOnTheWayExceptionWhenCancellingRide(){
        given(rideRepository.findById(2L)).willReturn(Optional.ofNullable(this.canceledRide));
        RideNotOnTheWayException exception = assertThrows(RideNotOnTheWayException.class, () -> this.rideService.declineAssignedRide(2L));
        assertEquals("Ride is not on the way.", exception.getMessage());
        verify(rideRepository,times(1)).findById(2L);
    }

    @Test
    @DisplayName("Should throw UserNotFound when creating a ride with a non existing identity")
    void shouldThrowUserNotFoundWhenCreatingRideWithNonExistingIdentity() {
        this.rideRequest.setClientEmails(List.of("emailnotfound@maildrop.cc"));

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(null);

        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> rideService.processRideRequest(this.rideRequest));
        assertEquals("User not found.", e.getMessage());

        verify(identityRepository, times(1)).findByEmail(any());
        verifyNoMoreInteractions(identityRepository);
    }

    @Test
    @DisplayName("Should throw UserNotFound when creating a ride with a non existing client")
    void shouldThrowUserNotFoundWhenCreatingRideWithNonExistingClient() {
        this.rideRequest.setClientEmails(List.of("emailnotfound@maildrop.cc"));

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(this.driverIdentity);
        given(clientRepository.findByIdentityId(this.driverIdentity.getId()))
                .willReturn(null);

        UserNotFoundException e = assertThrows(UserNotFoundException.class,
                () -> rideService.processRideRequest(this.rideRequest));
        assertEquals("Client not found.", e.getMessage());

        verify(identityRepository, times(1)).findByEmail(any());
        verify(clientRepository, times(1)).findByIdentityId(any());
        verifyNoMoreInteractions(identityRepository);
    }

    @Test
    @DisplayName("Should throw BadRideTypeException when creating a Shared ride with one client")
    void shouldThrowBadRideTypeExceptionWhenCreatingSharedRide() {
        this.rideRequest.setRideType("Share ride");
        this.rideRequest.setClientEmails(List.of(this.client.getIdentity().getEmail()));
        this.createdRide.setRideType(RideType.SHARE_RIDE);

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(this.clientIdentity);
        given(clientRepository.findByIdentityId(this.clientIdentity.getId()))
                .willReturn(this.client);
        given(rideRepository.save(any()))
                .willReturn(this.createdRide);

        BadRideTypeException e = assertThrows(BadRideTypeException.class,
                () -> rideService.processRideRequest(this.rideRequest));
        assertEquals("Selected 'Share ride' but there are less than 2 clients.", e.getMessage());


        verify(identityRepository, times(1)).findByEmail(any());
        verify(clientRepository, times(1)).findByIdentityId(any());
        verify(rideRepository, times(1)).save(any());
        verify(coordinatesRepository, times(1)).saveAll(any());
        verifyNoMoreInteractions(identityRepository);
        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(rideRepository);
        verifyNoMoreInteractions(coordinatesRepository);
    }

    @Test
    @DisplayName("Should throw BadRideTypeException when creating a Single ride with multiple clients")
    void shouldThrowBadRideTypeExceptionWhenCreatingSingleRide() {
        this.rideRequest.setRideType("Single");
        this.rideRequest.setClientEmails(List.of(this.client.getIdentity().getEmail(), this.client2.getIdentity().getEmail()));
        this.createdRide.setRideType(RideType.SINGLE);
        this.createdRide.setClients(List.of(this.client, this.client2));

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(this.clientIdentity);
        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(1)))
                .willReturn(this.client2Identity);
        given(clientRepository.findByIdentityId(this.clientIdentity.getId()))
                .willReturn(this.client);
        given(clientRepository.findByIdentityId(this.client2Identity.getId()))
                .willReturn(this.client2);
        given(rideRepository.save(any()))
                .willReturn(this.createdRide);

        BadRideTypeException e = assertThrows(BadRideTypeException.class,
                () -> rideService.processRideRequest(this.rideRequest));
        assertEquals("Selected 'Single' but there are more than 1 clients.", e.getMessage());

        verify(identityRepository, times(2)).findByEmail(any());
        verify(clientRepository, times(2)).findByIdentityId(any());
        verify(rideRepository, times(1)).save(any());
        verify(coordinatesRepository, times(1)).saveAll(any());
        verifyNoMoreInteractions(identityRepository);
        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(rideRepository);
        verifyNoMoreInteractions(coordinatesRepository);
    }

    @Test
    @DisplayName("Should return nothing when creating a valid Shared ride")
    void shouldReturnNothingWhenCreatingSharedRide() {
        this.rideRequest.setRideType("Share ride");
        this.rideRequest.setClientEmails(List.of(this.client.getIdentity().getEmail(), this.client2.getIdentity().getEmail()));
        this.createdRide.setRideType(RideType.SHARE_RIDE);
        this.createdRide.setClients(List.of(this.client, this.client2));

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(this.clientIdentity);
        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(1)))
                .willReturn(this.client2Identity);
        given(clientRepository.findByIdentityId(this.clientIdentity.getId()))
                .willReturn(this.client);
        given(clientRepository.findByIdentityId(this.client2Identity.getId()))
                .willReturn(this.client2);
        given(rideRepository.save(any()))
                .willReturn(this.createdRide);

        assertDoesNotThrow(() -> rideService.processRideRequest(this.rideRequest));

        verify(identityRepository, times(2)).findByEmail(any());
        verify(clientRepository, times(2)).findByIdentityId(any());
        verify(rideRepository, times(1)).save(any());
        verify(coordinatesRepository, times(1)).saveAll(any());
        verifyNoMoreInteractions(identityRepository);
        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(rideRepository);
        verifyNoMoreInteractions(coordinatesRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to decline non existing Shared ride")
    void shouldThrowEntityNotFoundExceptionWhenDecliningNonExistingSharedRide() {
        given(rideRepository.findById(10L))
                .willReturn(Optional.empty());

        EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
                () -> rideService.declineShareRideRequest(10L));
        assertEquals("Ride not found.", e.getMessage());

        verify(rideRepository, times(1)).findById(any());
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should return nothing when declining Shared ride")
    void shouldReturnNothingWhenDecliningSharedRide() {
        createdRide.setClientsAccepted(Set.of(client.getIdentity().getEmail()));

        given(rideRepository.findById(this.createdRide.getId()))
                .willReturn(Optional.of(this.createdRide));
        given(identityRepository.findByEmail(client.getIdentity().getEmail()))
                .willReturn(clientIdentity);

        assertDoesNotThrow(() -> rideService.declineShareRideRequest(this.createdRide.getId()));

        verify(rideRepository, times(1)).findById(any());
        verify(rideRepository, times(1)).delete(any());
        verify(identityRepository, times(1)).findByEmail(any());
        verifyNoMoreInteractions(rideRepository);
        verifyNoMoreInteractions(identityRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when trying to accept non existing Shared ride")
    void shouldThrowEntityNotFoundExceptionWhenAcceptingNonExistingSharedRide() {
        given(rideRepository.findById(10L))
                .willReturn(Optional.empty());

        EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
                () -> rideService.acceptShareRideRequest(10L, "emailnotfound@maildrop.cc"));
        assertEquals("Ride not found.", e.getMessage());

        verify(rideRepository, times(1)).findById(any());
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should return nothing when declining Shared ride")
    void shouldReturnNothingWhenAcceptingSharedRide() {
        createdRide.setClientsAccepted(new HashSet<>());
        createdRide.setClients(List.of(this.client, this.client2));

        given(rideRepository.findById(this.createdRide.getId()))
                .willReturn(Optional.of(this.createdRide));
        given(rideRepository.save(any()))
                .willReturn(this.createdRide);

        assertDoesNotThrow(() -> rideService.acceptShareRideRequest(this.createdRide.getId(), this.client.getIdentity().getEmail()));

        verify(rideRepository, times(1)).findById(any());
        verify(rideRepository, times(1)).save(any());
        verifyNoMoreInteractions(rideRepository);
    }

    @Test
    @DisplayName("Should not find active drivers and then reject ride when creating a valid Single ride")
    void shouldNotFindActiveDriversAndThenRejectRideWhenCreatingSingleRide() {
        this.rideRequest.setRideType("Single");
        this.rideRequest.setClientEmails(List.of(this.client.getIdentity().getEmail()));
        this.createdRide.setRideType(RideType.SINGLE);
        this.createdRide.setClients(List.of(this.client));

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(this.clientIdentity);
        given(clientRepository.findByIdentityId(this.clientIdentity.getId()))
                .willReturn(this.client);
        given(rideRepository.save(any()))
                .willReturn(this.createdRide);
        given(coordinatesRepository.findByRouteId(this.route.getId()))
                .willReturn(this.coordinates);
        given(rideRepository.findById(this.createdRide.getId()))
                .willReturn(Optional.of(this.createdRide));
        given(driverRepository.countAdequateActiveDrivers(this.createdRide.getCarType()))
                .willReturn(0L);

        assertDoesNotThrow(() -> rideService.processRideRequest(this.rideRequest));

        verify(identityRepository, times(2)).findByEmail(any());
        verify(clientRepository, times(1)).findByIdentityId(any());
        verify(rideRepository, times(1)).save(any());
        verify(coordinatesRepository, times(1)).saveAll(any());
        verify(coordinatesRepository, times(1)).findByRouteId(any());
        verify(rideRepository, times(2)).findById(any());
        verify(driverRepository, times(1)).countAdequateActiveDrivers(any());
        verify(rideRepository, times(1)).delete(any());
        verifyNoMoreInteractions(identityRepository);
        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(rideRepository);
        verifyNoMoreInteractions(coordinatesRepository);
        verifyNoMoreInteractions(driverRepository);
    }

    @Test
    @DisplayName("Should find active drivers and then initiate ride when creating a valid Single ride")
    void shouldFindActiveDriversAndThenInitiateRideWhenCreatingSingleRide() throws IOException, InterruptedException {
        this.rideRequest.setRideType("Single");
        this.rideRequest.setClientEmails(List.of(this.client.getIdentity().getEmail()));
        this.createdRide.setRideType(RideType.SINGLE);
        this.createdRide.setClients(List.of(this.client));

        Ride updatedRide = new Ride();
        updatedRide.setId(3L);
        updatedRide.setClients(List.of(this.client));
        updatedRide.setDriver(this.driver);
        updatedRide.setRideType(RideType.SINGLE);
        updatedRide.setCarType(CarType.SEDAN);
        updatedRide.setStart(OffsetDateTime.now());
        updatedRide.setRoute(route);
        updatedRide.setPrice(213D);
        updatedRide.setRideStatus(RideStatus.PENDING);
        updatedRide.setRequestedServices(Set.of());
        updatedRide.setClientsAccepted(Set.of(client.getIdentity().getEmail()));
        updatedRide.setDeleted(false);

        given(identityRepository.findByEmail(this.rideRequest.getClientEmails().get(0)))
                .willReturn(this.clientIdentity);
        given(clientRepository.findByIdentityId(this.clientIdentity.getId()))
                .willReturn(this.client);
        given(rideRepository.save(any()))
                .willReturn(updatedRide);
        given(coordinatesRepository.findByRouteId(this.route.getId()))
                .willReturn(this.coordinates);
        given(rideRepository.findById(this.createdRide.getId()))
                .willReturn(Optional.of(this.createdRide));
        given(driverRepository.countAdequateActiveDrivers(this.createdRide.getCarType()))
                .willReturn(1L);
        given(driverRepository.findFreeAdequateActiveDrivers(any(), any()))
                .willReturn(Arrays.asList(this.driver));

        assertDoesNotThrow(() -> rideService.processRideRequest(this.rideRequest));

        verify(identityRepository, times(1)).findByEmail(any());
        verify(clientRepository, times(1)).findByIdentityId(any());
        verify(rideRepository, times(3)).save(any());
        verify(coordinatesRepository, times(1)).saveAll(any());
        verify(coordinatesRepository, times(1)).findByRouteId(any());
        verify(rideRepository, times(2)).findById(any());
        verify(driverRepository, times(1)).countAdequateActiveDrivers(any());
        verify(driverRepository, times(2)).save(any());
        verify(clientRepository, times(1)).save(any());
        verify(driverRepository, times(1)).findFreeAdequateActiveDrivers(any(), any());
        verifyNoMoreInteractions(identityRepository);
        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(rideRepository);
        verifyNoMoreInteractions(coordinatesRepository);
        verifyNoMoreInteractions(driverRepository);
    }
}
