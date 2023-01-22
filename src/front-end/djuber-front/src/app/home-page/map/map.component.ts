import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as L from 'leaflet';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { AvailableDriver } from './availableDriver';
import { MapService } from './map.service';
import {Point} from "./point";
import { Route } from './route';

@Component({
  selector: 'djuber-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  map!: L.Map;

  clientLocation : Point;

  desiredLocation : Point;

  desireMarker : L.Marker = undefined;

  routes : Route[]=[];

  routeLines : L.Polyline[]=[];

  availableDriversMarkers : L.Marker[] = [];

  options = {
    layers: getLayers(),
    zoom: 16,
    center: { lat: 45.25461307185434, lng:  19.842973257328783 }
  }

  address:string;

  constructor(private mapService:MapService, private _snackBar: MatSnackBar) { }


  ngOnInit(): void {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.setGeoLocation.bind(this));
   }
   this.setAvailableDrivers();
  }

  getCoordinates() {
    let addressToDisplay = this.address;
    if(this.address.length > 4){
    this.mapService.searchLocation(this.address).subscribe({ next:(response : Array<any>)=> {
        this.clear();
        if(response.length !==0){

        this.desiredLocation = {lat:response[0].lat , long : response[0].lon} as Point;
        this.desireMarker = L.marker([this.desiredLocation.lat, this.desiredLocation.long], {
          icon: L.icon({
              iconUrl: 'assets/finish.svg',
              iconSize: [30, 30],
              iconAnchor: [10, 10],
              popupAnchor: [0, 0]
          }),
          title: 'Desired location'
      }).addTo(this.map);
      this.map.panTo([this.desiredLocation.lat, this.desiredLocation.long]);
      this.desireMarker.bindTooltip(addressToDisplay.toUpperCase());
      this.getRoutes();
    }else{
      this._snackBar.openFromComponent(SnackbarComponent, {data:"Unknown location."});
    }},
    error:(error) =>{
      console.log(error);
      this._snackBar.openFromComponent(SnackbarComponent, {data:"Api currently unavailable."});
    }});
    }else{
      this._snackBar.openFromComponent(SnackbarComponent, {data:"Please provide at least 5 characters of desired address."});
    }
  }

  getRoutes(){
    this.mapService.getRoutesBetweenPoints(this.clientLocation, this.desiredLocation).subscribe({
      next:(v)=>{
        const paths = v["paths"];
        let i = 0;
        paths.forEach(path => {
          let route = new Route(i, path);
          this.routes.push(route);
          this.drawRoute(route);
          i = i+1;
        })
      }
    });
  }

  setGeoLocation(position: { coords: { latitude: any; longitude: any } }) {
    const {
       coords: { latitude, longitude },
    } = position;
    this.clientLocation = {lat:latitude, long:longitude} as Point;
    L.marker([latitude, longitude], {
      icon: L.icon({
          iconUrl: 'assets/person.svg',
          iconSize: [30, 30],
          iconAnchor: [10, 10],
          popupAnchor: [0, 0]
      }),
      title: 'Client'
  }).addTo(this.map);
}


  onMapReady($event: L.Map) {
    this.map = $event;
  }

  mapClicked($event: any) {
    console.log($event.latlng.lat, $event.latlng.lng);
  }

  markerClicked($event: any, index: number) {
    console.log($event.latlng.lat, $event.latlng.lng);
  }

  markerDragEnd($event: any, index: number) {
    console.log($event.target.getLatLng());
  }

  drawAvailableDriverMarker(driver:AvailableDriver){
    const marker = L.marker([driver.lat, driver.lon], {
      icon: L.icon({
          iconUrl: 'assets/car.svg',
          iconSize: [30, 30],
          iconAnchor: [10, 10],
          popupAnchor: [0, 0]
      }),
      title: 'Available Driver'
    }).addTo(this.map);

    this.availableDriversMarkers.push(marker);
  }

  drawRoute(route:Route){
    const polyline = new L.Polyline(route.points, { color: 'red' }).addTo(this.map);
    polyline.bindTooltip("Aprox. price: " + route.getPrice()+"RSD");
    this.routeLines.push(polyline);
  }

  clear(){
    if(this.desireMarker !== undefined){
      this.desireMarker.remove();
    }
    this.address = '';
    this.desireMarker = undefined;
    this.routes.splice(0,this.routes.length);
    this.routeLines.forEach(line => line.remove());
    this.routeLines.splice(0, this.routeLines.length);
  }

  setAvailableDrivers(){
    this.mapService.getAvailableDrivers().subscribe({
      next:(v) =>{
        this.availableDriversMarkers.forEach(m => {m.remove()});
        this.availableDriversMarkers.splice(0, this.availableDriversMarkers.length);
        v.forEach(d => {this.drawAvailableDriverMarker(d)});
      }
    })
  }
}

export const getLayers = (): L.Layer[] => {
  return [
    // Basic style
    new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    } as L.TileLayerOptions),
  ] as L.Layer[];
};
