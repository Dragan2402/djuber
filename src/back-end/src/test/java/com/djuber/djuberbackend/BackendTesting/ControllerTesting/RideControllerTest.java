package com.djuber.djuberbackend.BackendTesting.ControllerTesting;

import com.djuber.djuberbackend.Application.Services.Ride.IRideService;
import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import com.djuber.djuberbackend.Controllers.Ride.RideController;
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

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = RideController.class)
@RunWith(SpringRunner.class)
public class RideControllerTest {

    @MockBean
    private IRideService rideService;

    @Autowired
    private RideController rideController;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
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
        Mockito.doNothing().when(rideService).endRide(100000L);

        mockMvc.perform(put("/api/ride/script/endRide/100000"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should update vehicle location - /api/ride/script/updateVehicleLocation/{rideId}")
    public void shouldUpdateVehicleLocation() throws Exception {

        Mockito.doNothing().when(rideService).endRide(100000L);
        String body = "{\"index\": 0, \"lat\": 123.3, \"lon\": 1232.3}";

        mockMvc.perform(put("/api/ride/script/updateVehicleLocation/100000").content(body).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
