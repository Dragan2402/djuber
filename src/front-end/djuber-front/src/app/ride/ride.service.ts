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

  acceptRideDriver(rideId:number){
    return this.http.post(`/api/ride/driver/accept/${rideId}`, null);
  }

  declineRideDriver(rideId:number){
    return this.http.post(`/api/ride/driver/decline/${rideId}`, null);
  }

  acceptRideClient(rideId:number){
    return this.http.post(`/api/ride/client/accept/${rideId}`, null);
  }

  declineRideClient(rideId:number){
    return this.http.post(`/api/ride/client/decline/${rideId}`, null);
  }


}
