import { LatLngExpression } from "leaflet";
import { Point } from "./point";

export class Route{
  id:number;
  points:LatLngExpression[];
  distance:number;

  constructor(id:number, feature:any){
    this.id = id;
    this.distance = feature["properties"]["summary"]["distance"];
    this.points = [];
    const coordinates = feature["geometry"]["coordinates"];
    coordinates.forEach(coordinate => {
      this.points.push({lat:coordinate[1], lng:coordinate[0]});
    })
  }

  public getPrice(){
    return Math.round((this.distance/1000) * 120);
  }

}
