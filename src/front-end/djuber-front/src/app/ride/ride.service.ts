import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RideReviewResponse } from './ride-review/rideReviewResponse';
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

  getRideForReview(rideId:string){
    return this.http.get<RideReviewResponse>(`/api/ride/rideForReview/${rideId}`);
  }

  reviewRide(reviewRide){
    return this.http.post("/api/ride/reviewRide",reviewRide);
  }

  declineAssignedRide(rideId:string){
    return this.http.post(`/api/ride/declineAssignedRide/${rideId}`,{});
  }

  submitCancellingNote(rideId:string, note:string){
    return this.http.put(`/api/ride/submitCancellingNote/${rideId}`,{note:note});
  }
}
