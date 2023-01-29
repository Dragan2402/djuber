import {CoordinateResponse} from "./coordinateResponse";
export interface FavouriteRoute{
  id:number;
  stopNames:string[];
  coordinates:CoordinateResponse[];
  distance:number;
}
