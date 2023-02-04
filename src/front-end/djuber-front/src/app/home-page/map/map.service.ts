import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { FavouriteRoute } from 'src/app/ride/favouriteRoute';
import { DriverLocation } from './driverLocation';
import { Coordinate } from './coordinate';
import { RideRequest } from './rideRequest';
import { ReservationRequest } from './reservationRequest';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http:HttpClient) { }

  getDriversLocation(){
    return this.http.get<DriverLocation[]>("/api/driver/driversLocation");
  }

  getLoggedDriverLocation(){
    return this.http.get<DriverLocation>("/api/driver/loggedDriverLocation");
  }

  searchLocation(address:string){
    return this.http.get("https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf62486b80eee7f91341569a61109ec802d68f&text="+address+"&size=1");
  }

  searchLocationAsync(address:string){
    return this.http.get("https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf62486b80eee7f91341569a61109ec802d68f&text="+address+"&size=1");
  }

  async getClientLocationName(clientLocation:Coordinate){
    const result$ = this.http.get(`https://api.openrouteservice.org/geocode/reverse?api_key=5b3ce3597851110001cf62486b80eee7f91341569a61109ec802d68f&point.lon=${clientLocation.lon}&point.lat=${clientLocation.lat}&size=1`).pipe();
    const response = await firstValueFrom(result$);
    return response["features"][0]["properties"]["name"];
  }


  getRoutesBetweenPoints(clientPoint:Coordinate, desiredPoint:Coordinate) {
      const url = `https://graphhopper.com/api/1/route?point=${clientPoint.lat},${clientPoint.lon}&point=${desiredPoint.lat},${desiredPoint.lon}&points_encoded=false&alternative_route.max_paths=5&alternative_route.max_weight_factor=2000&key=7682fe6b-4550-4ee9-a316-9a4c1a083109`;
      return this.http.get(url);
    }

  getRoutesBetweenPointsORS(points:Coordinate[], preference:string){
    let pointsBody =[];
    points.forEach(point => {
      pointsBody.push([point.lon, point.lat]);
    })
    const body = {"coordinates":pointsBody,"geometry_simplify":"true","preference":preference,"units":"km"}
    const url = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";
    return this.http.post(url, body);
  }


  orderRide(request:RideRequest){
    return this.http.post("/api/ride", request);
  }

  makeReservation(reservationRequest) {
    const url = "/api/reservation/create";
    return this.http.post(url, reservationRequest);
  }

  checkIfClientsExist(clientEmails: string[]) {
    const url = "/api/client/checkIfClientsExist";
    return this.http.post(url, clientEmails);
  }

  checkIfClientsAreBlocked(clientEmails: string[]) {
    const url = "/api/client/checkIfClientsAreBlocked";
    return this.http.post(url, clientEmails);
  }

  getClientFavouriteRoutes(){
    return this.http.get<FavouriteRoute[]>("/api/route/loggedClientFavouriteRoutes");
  }

  deleteFavouriteRoute(id:number){
    return this.http.delete(`/api/route/favouriteRoute/${id}`);
  }
}
