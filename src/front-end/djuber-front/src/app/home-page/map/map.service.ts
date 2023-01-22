import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AvailableDriver } from './availableDriver';
import { Point } from './point';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http:HttpClient) { }

  getAvailableDrivers(){
    return this.http.get<AvailableDriver[]>("/api/driver/availableDrivers");
  }

  searchLocation(address:string){
    return this.http.get(`https://nominatim.openstreetmap.org/search?q=${address}&format=json`);
  }


  getRoutesBetweenPoints(clientPoint:Point, desiredPoint:Point) {
      const url = `https://graphhopper.com/api/1/route?point=${clientPoint.lat},${clientPoint.long}&point=${desiredPoint.lat},${desiredPoint.long}&points_encoded=false&alternative_route.max_paths=5&alternative_route.max_weight_factor=2000&key=7682fe6b-4550-4ee9-a316-9a4c1a083109`;
      return this.http.get(url);
    }

}
