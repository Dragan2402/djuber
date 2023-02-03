import { Coordinate } from "./coordinate";

export interface RideRequest{
  distance:number;
  carType:string;
  rideType:string;
  additionalServices:string[];
  coordinates:Coordinate[];
  clientEmails:string[];
  stopNames:string[];
}
