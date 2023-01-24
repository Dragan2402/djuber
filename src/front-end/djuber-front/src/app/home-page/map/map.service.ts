import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AvailableDriver } from './availableDriver';
import { Coordinate } from './coordinate';
import { RideRequest } from './rideRequest';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http:HttpClient) { }

  getAvailableDrivers(){
    return this.http.get<AvailableDriver[]>("/api/driver/availableDrivers");
  }

  searchLocation(address:string){
    return this.http.get("https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf62486b80eee7f91341569a61109ec802d68f&text="+address+"&size=1");
  }

  searchLocationAsync(address:string){
    return this.http.get("https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf62486b80eee7f91341569a61109ec802d68f&text="+address+"&size=1");
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
    return this.http.post("/api/ride/driver", request);
  }



}
