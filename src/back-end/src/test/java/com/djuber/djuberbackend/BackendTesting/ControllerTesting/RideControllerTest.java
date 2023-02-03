package com.djuber.djuberbackend.BackendTesting.ControllerTesting;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Controllers.Ride.Requests.CoordinateRequest;
import com.djuber.djuberbackend.Controllers.Ride.Requests.RideRequest;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.Responses.RideResponse;
import com.djuber.djuberbackend.Controllers.Ride.RideController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = RideController.class)
@RunWith(SpringRunner.class)
public class RideControllerTest {

    @MockBean
    private IRideService rideService;

    @Autowired
    private RideController rideController;


    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return driver based on the ride id starting location - /api/ride/script/getDriverStartingLocation/{rideId}")
    public void shouldReturnDriverStartingLocationByRideId() throws Exception {

        CoordinateResponse coordinateResponse = new CoordinateResponse();
        coordinateResponse.setIndex(0);
        coordinateResponse.setLon(0D);
        coordinateResponse.setLat(312D);

        Mockito.when(rideService.getDriverStartingLocation(100000L)).thenReturn(coordinateResponse);

        mockMvc.perform(get("/api/ride/script/getDriverStartingLocation/100000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andExpect(jsonPath("$.index", Matchers.is(0)))
                .andExpect(jsonPath("$.lon", Matchers.is(0.0)))
                .andExpect(jsonPath("$.lat", Matchers.is(312.0)));

    }

    @Test
    @DisplayName("Should decline share ride - /api/ride/client/decline/{rideId}")
    public void shouldDeclineShareRide() throws Exception {

        Mockito.doNothing().when(rideService).declineShareRideRequest(100000L);

        mockMvc.perform(post("/api/ride/client/decline/100000").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should return ride starting location - /api/ride/script/getRideStartingLocation/{rideId}")
    public void shouldReturnRideStartingLocationBy() throws Exception {

        CoordinateResponse coordinateResponse = new CoordinateResponse();
        coordinateResponse.setIndex(2);
        coordinateResponse.setLon(1.23D);
        coordinateResponse.setLat(312D);

        Mockito.when(rideService.getRideStartingLocation(100000L)).thenReturn(coordinateResponse);

        mockMvc.perform(get("/api/ride/script/getRideStartingLocation/100000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andExpect(jsonPath("$.index", Matchers.is(2)))
                .andExpect(jsonPath("$.lon", Matchers.is(1.23)))
                .andExpect(jsonPath("$.lat", Matchers.is(312.0)));

    }

    @Test
    @DisplayName("Should start ride and return list of coordinates - /api/ride/script/startRide/{rideId}")
    public void shouldStartrideAndReturnListOfCoordinates() throws Exception {

        CoordinateResponse coordinateResponse = new CoordinateResponse();
        coordinateResponse.setIndex(2);
        coordinateResponse.setLon(1.23D);
        coordinateResponse.setLat(312D);

        CoordinateResponse coordinateResponse2 = new CoordinateResponse();
        coordinateResponse2.setIndex(0);
        coordinateResponse2.setLon(0D);
        coordinateResponse2.setLat(312D);

        Mockito.when(rideService.startRide(100000L)).thenReturn(asList(coordinateResponse, coordinateResponse2));

        mockMvc.perform(get("/api/ride/script/startRide/100000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].index", Matchers.is(2)))
                .andExpect(jsonPath("$[0].lon", Matchers.is(1.23)))
                .andExpect(jsonPath("$[0].lat", Matchers.is(312.0)))
                .andExpect(jsonPath("$[1].index", Matchers.is(0)))
                .andExpect(jsonPath("$[1].lon", Matchers.is(0.0)))
                .andExpect(jsonPath("$[1].lat", Matchers.is(312.0)));

    }

    @Test
    @DisplayName("Should end ride - /api/ride/script/endRide/{rideId}")
    public void shouldEndRide() throws Exception {
        doNothing().when(rideService).endRide(100000L);

        mockMvc.perform(put("/api/ride/script/endRide/100000"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should update vehicle location - /api/ride/script/updateVehicleLocation/{rideId}")
    public void shouldUpdateVehicleLocation() throws Exception {

        doNothing().when(rideService).endRide(100000L);
        String body = "{\"index\": 0, \"lat\": 123.3, \"lon\": 1232.3}";

        mockMvc.perform(put("/api/ride/script/updateVehicleLocation/100000").content(body).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should decline ride - /api/ride/script/declineAssignedRide/{rideId}")
    public void shouldDeclineRide() throws Exception {

        doNothing().when(rideService).declineAssignedRide(100000L);

        mockMvc.perform(post("/api/ride/declineAssignedRide/100000").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should create new ride and return status 201 created - /api/ride")
    public void shouldCreateRide() throws Exception {
        CoordinateRequest coordinateRequest = new CoordinateRequest();
        coordinateRequest.setIndex(0);
        coordinateRequest.setLat(45.5);
        coordinateRequest.setLon(19.5);

        RideRequest rideRequest = new RideRequest();
        rideRequest.setCoordinates(Arrays.asList(coordinateRequest));
        rideRequest.setCarType("Sedan");
        rideRequest.setRideType("Single");
        rideRequest.setDistance(1.0);
        rideRequest.setAdditionalServices(new HashSet<>());
        rideRequest.setClientEmails(Arrays.asList("client@maildrop.cc"));
        rideRequest.setStopNames(Arrays.asList("Stop"));

        doNothing().when(rideService).processRideRequest(any());

        mockMvc.perform(post("/api/ride")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rideRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should not create a new ride and return status 400 bad request - /api/ride")
    public void shouldNotCreateRide() throws Exception {
        RideRequest rideRequest = new RideRequest();

        doNothing().when(rideService).processRideRequest(any());

        mockMvc.perform(post("/api/ride")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rideRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should accept ride and return status 200 ok - /api/ride/client/accept/{rideId}")
    public void shouldAcceptShareRide() throws Exception {
        Principal principal = () -> "client@maildrop.cc";

        doNothing().when(rideService).acceptShareRideRequest(1L, principal.getName());

        mockMvc.perform(post("/api/ride/client/accept/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should decline ride and return status 200 ok - /api/ride/client/decline/{rideId}")
    public void shouldDeclineShareRide() throws Exception {
        doNothing().when(rideService).declineShareRideRequest(1L);

        mockMvc.perform(post("/api/ride/client/decline/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
