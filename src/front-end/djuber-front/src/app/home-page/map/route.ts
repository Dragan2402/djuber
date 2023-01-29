import { LatLngExpression } from "leaflet";
import { Coordinate } from "./coordinate";

export class Route{
  id:number;
  points:LatLngExpression[];
  distance:number;
  stopNames:string[];

  constructor();
  constructor(id:number, feature:any)
  constructor(id?:number, feature?:any){
    if(id !== undefined && feature !== undefined){
      this.id = id;
      this.distance = feature["properties"]["summary"]["distance"];
      this.points = [];
      const coordinates = feature["geometry"]["coordinates"];
      coordinates.forEach(coordinate => {
        this.points.push({lat:coordinate[1], lng:coordinate[0]});
      })
    }
  }

  public getPrice(){
    return Math.round((this.distance) * 120);
  }

  public setId(id:number):void{
    this.id=id;
  }

  public setPoints(points:LatLngExpression[]):void{
    this.points = points;
  }

  public setDistance(distance:number):void{
    this.distance =distance;
  }

  public setStopNames(names:string[]):void{
    this.stopNames = names;
  }

}
