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
            self.client.put(f'/ride/script/updateVehicleLocation/{rideId}',
                            json={'index': 0, 'lat': new_coordinate[0], 'lon': new_coordinate[1]})
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
        print(self.coordinates)
        self.driving_to_start_point = False
        self.driving_the_route = True

    def end_route(self):
        self.client.put(f'/ride/script/endRide/{rideId}')
        self.environment.runner.quit()

    def get_coordinates(self):
        response = requests.get(
            f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{self.driver_location["lat"]},{self.driver_location["lon"]};{self.starting_point["lat"]},{self.starting_point["lon"]}?geometries=geojson&overview=false&alternatives=true&steps=true')
        self.routeGeoJSON = response.json()
        self.coordinates = []
        for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
            self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]

    def get_ride_starting_point(self):
        response = self.client.get(f'/ride/script/getRideStartingLocation/{rideId}')
        self.driver_location = response.json()

    def get_driver_location(self):
        response = self.client.get(f'/ride/script/getDriverStartingLocation/{rideId}')
        self.starting_point = response.json()

    def end_ride(self):
        print("end")
