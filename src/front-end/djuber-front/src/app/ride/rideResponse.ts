import { CoordinateResponse } from "./coordinateResponse";

export interface RideResponse{
  clientEmails:string[];
  driverName:string;
  rideStatus:string;
  price:number;
  coordinates:CoordinateResponse[];
}
