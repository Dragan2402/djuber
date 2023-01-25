import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RideResponse } from './rideResponse';

@Injectable({
  providedIn: 'root'
})
export class RideService {

  constructor(private http:HttpClient) { }

  getRideResponse(rideId:string){
    return this.http.get<RideResponse>(`/api/ride/${rideId}`);
  }

  acceptRide(rideId:number){
    return this.http.post(`/api/ride/accept/${rideId}`,null);
  }
}
