import { Coordinate } from "./coordinate";

export interface ReservationRequest{
  coordinates:Coordinate[];
  carType:string;
  rideType:string;
  distance:number;
  start:Date;
  additionalServices:string[];
  clientEmails:string[];
  stopNames:string[];
}
