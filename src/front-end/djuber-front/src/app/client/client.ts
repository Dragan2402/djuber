import {Coordinate} from "../home-page/map/coordinate";

export interface Client{
  id: number,
  identityId: number,
  email: string,
  firstName: string,
  lastName: string,
  city: string,
  phoneNumber: string,
  note:string,
  blocked:boolean,
}

export interface Ride {
  id: number
  startCoordinateName: string
  endCoordinateName: string
  price: number
  start: Date
  finish: Date
}

export interface ClientRide {
  clientEmails: string[]
  coordinates: Coordinate[]
  driverName: string
  price: number
  rideStatus: string
}

