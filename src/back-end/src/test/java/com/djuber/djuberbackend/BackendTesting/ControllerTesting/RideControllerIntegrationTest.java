package com.djuber.djuberbackend.BackendTesting.ControllerTesting;


import com.djuber.djuberbackend.Controllers.Ride.Responses.CoordinateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@TestPropertySource(locations = "spring.datasource.driver-class-name")
public class RideControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should return driver starting location - /api/ride/script/getDriverStartingLocation/{rideId}")
    public void shouldReturnDriverStartingLocation() {
        ResponseEntity<CoordinateResponse> responseEntity = restTemplate.exchange("/api/ride/script/getDriverStartingLocation/100000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        CoordinateResponse coordinate = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, coordinate.getIndex());
        assertEquals(45.23851423717639, coordinate.getLat());
        assertEquals(19.832768440246586, coordinate.getLon());
    }

    @Test
    @DisplayName("Should return not found exception instead of driver starting location - /api/ride/script/getDriverStartingLocation/{rideId}")
    public void shouldReturnNotFoundExceptionInsteadOfDriverStartingLocation() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/getDriverStartingLocation/600000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "Ride not found.");

    }

    @Test
    @DisplayName("Should return ride starting location - /api/ride/script/getRideStartingLocation/{rideId}")
    public void shouldReturnRideStartingLocation() {
        ResponseEntity<CoordinateResponse> responseEntity = restTemplate.exchange("/api/ride/script/getRideStartingLocation/100000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        CoordinateResponse coordinate = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, coordinate.getIndex());
        assertEquals(45.24473173439358, coordinate.getLat());
        assertEquals(19.84146283175646, coordinate.getLon());
    }

    @Test
    @DisplayName("Should return not found exception instead of ride starting location - /api/ride/script/getRideStartingLocation/{rideId}")
    public void shouldReturnNotFoundExceptionInsteadOfRideStartingLocation() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/getRideStartingLocation/600000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "Ride not found.");

    }

    @Test
    @DisplayName("Should return not found exception when missing coordinates instead of ride starting location - /api/ride/script/getRideStartingLocation/{rideId}")
    public void shouldReturnNotFoundExceptionWhenMissingCoordinatesInsteadOfRideStartingLocation() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/getRideStartingLocation/100000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "Coordinate not found.");

    }

    @Test
    @DisplayName("Should start ride and return list of coordinates - /api/ride/script/startRide/{rideId}")
    public void shouldStartRideAndReturnListOfCoordinates() {
        ResponseEntity<List<CoordinateResponse>> responseEntity = restTemplate.exchange("/api/ride/script/startRide/100000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        List<CoordinateResponse> coordinates = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, coordinates.size());
        assertEquals(0, coordinates.get(0).getIndex());
        assertEquals(45.24473173439358, coordinates.get(0).getLat());
        assertEquals(19.84146283175646, coordinates.get(0).getLon());
        assertEquals(1, coordinates.get(1).getIndex());
        assertEquals(45.2480590388405, coordinates.get(1).getLat());
        assertEquals(19.839219531462838, coordinates.get(1).getLon());
        assertEquals(2, coordinates.get(2).getIndex());
        assertEquals(45.25258638691436, coordinates.get(2).getLat());
        assertEquals(19.83754453391027, coordinates.get(2).getLon());
    }

    @Test
    @DisplayName("Should return not found exception instead of list of coordinates - /api/ride/script/startRide/{rideId}")
    public void shouldReturnNotFoundExceptionInsteadOfListOfCoordinates() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/startRide/600000",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "Ride not found.");

    }

    @Test
    @DisplayName("Should end ride - /api/ride/script/startRide/{endRide}")
    public void shouldEndRide() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/endRide/100000",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<>() {
                });


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return not found exception instead of ending ride - /api/ride/script/endRide/{rideId}")
    public void shouldReturnNotFoundExceptionInsteadOfEndingRide() {


        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/endRide/600000",
                HttpMethod.PUT, null,
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "Ride not found.");

    }

    @Test
    @DisplayName("Should update vehicle location - /api/ride/script/updateVehicleLocation/{endRide}")
    public void shouldUpdateVehicleLocation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"index\": 0, \"lat\": 123.3, \"lon\": 1232.3}";


        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/updateVehicleLocation/100000",
                HttpMethod.PUT, new HttpEntity<>(body, headers),
                new ParameterizedTypeReference<>() {
                });


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }


    @Test
    @DisplayName("Should return not found exception instead of  updating car - /api/ride/script/updateVehicleLocation/{rideId}")
    public void shouldReturnNotFoundExceptionInsteadOfUpdatingCar() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"index\": 0, \"lat\": 123.3, \"lon\": 1232.3}";

        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/updateVehicleLocation/600000",
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "Ride not found.");

    }

    @Test
    @DisplayName("Should return not found exception instead of  updating car - /api/ride/script/updateVehicleLocation/{rideId}")
    public void shouldReturnBadRequestInsteadOfUpdatingCar() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"index\": 0, \"lat\": 123.3, \"lon\": 1232.3}";

        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/ride/script/updateVehicleLocation/100000",
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                new ParameterizedTypeReference<>() {
                });

        Map<String,Object> object = (Map<String, Object>) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(object.get("message"), "The ride you are trying to update has been canceled.");
    }
}
