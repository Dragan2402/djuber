import { LatLngExpression } from "leaflet";
import { Point } from "./point";

export class Route{
  id:number;
  points:LatLngExpression[];
  distance:number;

  constructor(id:number, path:any){
    this.id = id;
    this.distance = path["distance"];
    this.points = [];
    const coordinates = path["points"]["coordinates"];
    coordinates.forEach(coordinate => {
      this.points.push({lat:coordinate[1], lng:coordinate[0]});
    })
  }

  public getPrice(){
    return Math.round((this.distance/1000) * 120);
  }

}
