import { Coordinate } from "./coordinate";

export interface RideRequest{
  distance:number;
  carType:string;
  additionalServices:string[];
  coordinates:Coordinate[];
  clientEmail:string;
}
