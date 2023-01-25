import requests
import json
import sys

from locust import HttpUser, task, between, events
from random import randrange

rideId = 0


@events.init_command_line_parser.add_listener
def _(parser):
    parser.add_argument("--data", type=json.loads)


@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    global rideId
    data = environment.parsed_options.data
    rideId = data["rideId"]


class QuickstartUser(HttpUser):
    host = 'http://localhost:8080'
    wait_time = between(0.5, 2)
    global rideId

    def on_start(self):
        self.driving_to_start_point = True
        self.driving_the_route = False
        self.get_driver_location()
        self.get_ride_starting_point()
        self.get_coordinates()
        print(self.starting_point["lat"])
        print(self.driver_location)
        print(self.coordinates)

    @task
    def update_vehicle_coordinates(self):
        if len(self.coordinates) > 0:
            new_coordinate = self.coordinates.pop(0)
            response = self.client.put(f'/ride/script/updateVehicleLocation/{rideId}',
                                json={'index': 0, 'lat': new_coordinate[0], 'lon': new_coordinate[1]})

            if response.status_code != 200:
                self.end_ride()
        if len(self.coordinates) == 0 and self.driving_to_start_point:
            self.start_route()
        if len(self.coordinates) == 0 and self.driving_the_route:
            self.end_route()

    def start_route(self):
        response = self.client.get(f'/ride/script/startRide/{rideId}')
        response_json = response.json()
        for step in response_json:
            coordinate = [step["lat"], step["lon"]]
            self.coordinates.append(coordinate)
        self.driving_to_start_point = False
        self.driving_the_route = True

    def end_route(self):
        self.client.put(f'/ride/script/endRide/{rideId}')
        self.environment.runner.quit()

    def get_coordinates(self):
        response = requests.get(
            f'https://graphhopper.com/api/1/route?point={self.starting_point["lat"]},{self.starting_point["lon"]}&point={self.driver_location["lat"]},{self.driver_location["lon"]}&points_encoded=false&alternative_route.max_paths=5&alternative_route.max_weight_factor=2000&key=7682fe6b-4550-4ee9-a316-9a4c1a083109')
        self.routeGeoJSON = response.json()
        self.coordinates = []
        for step in self.routeGeoJSON['paths'][0]["points"]["coordinates"]:
            coordinate = [step[1], step[0]]
            self.coordinates.append(coordinate)

    def get_ride_starting_point(self):
        response = self.client.get(f'/ride/script/getRideStartingLocation/{rideId}')
        self.driver_location = response.json()

    def get_driver_location(self):
        response = self.client.get(f'/ride/script/getDriverStartingLocation/{rideId}')
        self.starting_point = response.json()

    def end_ride(self):
        self.environment.runner.quit()
