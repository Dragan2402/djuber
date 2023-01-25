import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as L from 'leaflet';
import { AuthenticationService } from 'src/app/authentication/authentication.service';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { HashService } from 'src/app/utility/hash-service.service';
import { AvailableDriver } from './availableDriver';
import { MapService } from './map.service';
import {Coordinate} from "./coordinate";
import { Route } from './route';
import {firstValueFrom, lastValueFrom} from "rxjs";
import { RideRequest } from './rideRequest';

@Component({
  selector: 'djuber-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  map!: L.Map;

  clientLocation : Coordinate;

  desiredLocation : Coordinate;

  clientMarker :L.Marker;

  desireMarker : L.Marker = undefined;

  routes : Route[]=[];

  routeLines : L.Polyline[]=[];

  availableDriversMarkers : L.Marker[] = [];

  checkPrice:number = 2;

  differentStartingPoint: boolean = false;

  fastestRoute : boolean = false;

  routePathPoints : Coordinate[] = [];

  routePathAddresses : string[]= [];

  logged! : boolean;

  orderStatus:number = 0;

  selectedCarType:string = "Sedan";

  carTypes:string[]= ["Sedan","Station wagon","Van","Transporter"];

  extraLuggage: boolean;
  pets: boolean;
  luggageTransport: boolean;
  knowingEnglish:boolean;

  options = {
    layers: getLayers(),
    zoom: 16,
    center: { lat: 45.25461307185434, lng:  19.842973257328783 }
  }

  desiredAddress:string = '';

  startingAddress:string = '';

  constructor(private mapService:MapService, private _snackBar: MatSnackBar, private authenticationService : AuthenticationService, private hashService:HashService) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
  }


  ngOnInit(): void {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.setGeoLocation.bind(this));
   }
   this.setAvailableDrivers();
  }

  getCoordinates() {
    let addressToDisplay = this.desiredAddress;
    if(this.desiredAddress.length > 4){
    this.mapService.searchLocation(this.desiredAddress).subscribe({ next:(response : Array<any>)=> {
        this.clear();
        if(response["features"].length > 0){
        this.desiredLocation = {lat:response["features"][0]["geometry"]["coordinates"][1] , lon : response["features"][0]["geometry"]["coordinates"][0], index:9999} as Coordinate;
        this.desireMarker = L.marker([this.desiredLocation.lat, this.desiredLocation.lon], {
          icon: L.icon({
              iconUrl: 'assets/finish.svg',
              iconSize: [30, 30],
              iconAnchor: [10, 10],
              popupAnchor: [0, 0]
          }),
          title: 'Desired location'
      }).addTo(this.map);
      this.map.panTo([this.desiredLocation.lat, this.desiredLocation.lon]);
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
    let points = [];
    points.push(this.clientLocation);
    points.push(this.desiredLocation);
    this.mapService.getRoutesBetweenPointsORS(points, 'fastest').subscribe({
      next:(response) =>{
        let i = 0 ;
        if(response["features"].length > 0){
          response["features"].forEach(feature => {
            let route = new Route(i, feature);
            this.routes.push(route);
            this.drawRoute(route);
            i++;
          })
        }
        else{
          this._snackBar.openFromComponent(SnackbarComponent, {data:"Can not find route."});
        }
      }
    })
  }

  addRoutePoint(index:number){
    this.routePathAddresses.splice(index+1, 0, "");
  }

  removeRoutePoint(index:number){
    this.routePathAddresses.splice(index, 1);
  }

  onValueUpdate(event, index){
    this.routePathAddresses[index]=event.target.value;
  }

  setGeoLocation(position: { coords: { latitude: any; longitude: any } }) {
    const {
       coords: { latitude, longitude },
    } = position;
    this.clientLocation = {lat:latitude, lon:longitude, index:0} as Coordinate;
    this.clientMarker = this.drawMarker(this.clientLocation,"person","Your location");
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

  canUserOrderRide(){
    if(this.logged){
    return (this.authenticationService.getLoggedUserRole()===this.hashService.hashString("CLIENT"));
    }
  }

  drawAvailableDriverMarker(driver:AvailableDriver){

    this.availableDriversMarkers.push(this.drawMarker({lat:driver.lat, lon: driver.lon, index:0 } as Coordinate,"car","Available driver"));
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
    this.desiredAddress = '';
    this.desireMarker = undefined;
    this.routes.splice(0,this.routes.length);
    this.routeLines.forEach(line => line.remove());
    this.routeLines.splice(0, this.routeLines.length);
    this.checkPrice = 2;
    this.routePathAddresses.splice(0, this.routePathAddresses.length);
    this.routePathPoints.splice(0, this.routePathPoints.length);
    this.orderStatus = 0;
    this.selectedCarType = "Sedan";
    this.extraLuggage = false;
    this.pets = false;
    this.luggageTransport = false;
    this.knowingEnglish = false;
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

  private async getCoordinateFromAddress(address:string, id:number){
    const result$ = this.mapService.searchLocationAsync(address).pipe();
    const response = await firstValueFrom(result$);
    if(response["features"].length > 0){
      return {lat:response["features"][0]["geometry"]["coordinates"][1] , lon : response["features"][0]["geometry"]["coordinates"][0], index:id} as Coordinate;
    }
  }

  getRoutesFromCoordinates(coordinates:Coordinate[], preference:string){
    this.mapService.getRoutesBetweenPointsORS(coordinates, preference).subscribe({
      next:(response) =>{
        let i = 0 ;
        if(response["features"].length > 0){
          response["features"].forEach(feature => {
            let route = new Route(i, feature);
            this.routes.push(route);
            this.drawRoute(route);
            i++;
            this.orderStatus = 2;
          });
          this._snackBar.openFromComponent(SnackbarComponent, {data:"Routes have been displayed. Please continue"});
        }
        else{
          this._snackBar.openFromComponent(SnackbarComponent, {data:"Can not find route."});
        }
      }
    })
  }

  async createRoute(){
    this.routePathPoints.splice(0, this.routePathPoints.length);

    if(this.differentStartingPoint && this.startingAddress.length <= 4){
      this._snackBar.openFromComponent(SnackbarComponent, {data:"Please provide at least 5 characters of starting address."});
      return;
    }
    if(this.desiredAddress.length <= 4){
      this._snackBar.openFromComponent(SnackbarComponent, {data:"Please provide at least 5 characters of desired address."});
      return;
    }

    this.routePathAddresses.forEach(address => {
        if(address.length <= 4){
          this._snackBar.openFromComponent(SnackbarComponent, {data:`Please provide at least 5 characters for address ${address}.`});
          return;
        }
      });
    this.orderStatus = 1;

    if(this.clientMarker !== undefined){
      this.clientMarker.remove();
    }
    const clientPoint = this.differentStartingPoint ? await this.getCoordinateFromAddress(this.startingAddress,0) : this.clientLocation;
    this.routePathPoints.push(clientPoint);
    this.clientMarker = this.drawMarker(clientPoint,"person","Starting location");

    for(let i = 0 ; i < this.routePathAddresses.length ; i++){
      this.routePathPoints.push(await this.getCoordinateFromAddress(this.routePathAddresses[i], i+1));
    }

    const desiredPoint = await this.getCoordinateFromAddress(this.desiredAddress,9999);

    this.routePathPoints.push(desiredPoint);
    this.desireMarker = this.drawMarker(desiredPoint, "finish",this.desiredAddress.toUpperCase());

    this.routePathPoints.sort((a, b) => {return a.index - b.index});

    const preference = this.fastestRoute ? "fastest": "shortest";

    this.getRoutesFromCoordinates(this.routePathPoints, preference);
  }

  private drawMarker(coordinate:Coordinate, iconUrlPart:string, tooltipText:string){
    const marker = L.marker([coordinate.lat, coordinate.lon], {
      icon: L.icon({
          iconUrl: 'assets/'+iconUrlPart+'.svg',
          iconSize: [30, 30],
          iconAnchor: [10, 10],
          popupAnchor: [0, 0]
      }),
      title: 'Desired location'
    }).addTo(this.map);
    this.map.panTo([coordinate.lat, coordinate.lon]);
    marker.bindTooltip(tooltipText);
    return marker;
  }


  private  getAdditionalServices():string[]{
    const services = new Array();
    if(this.pets){
      services.push("pets");
    }
    if(this.extraLuggage){
      services.push("extraLuggage");
    }
    if(this.luggageTransport){
      services.push("luggageTransport");
    }
    if(this.knowingEnglish){
      services.push("knowingEnglish");
    }
    return services;

  }

  orderRide(){
    const coordinates = [];
    const points = this.routes[0].points;
    if(points.length >= 2){
      for(let i = 0; i < points.length ; i++){

        coordinates.push({lat:points[i]["lat"], lon:points[i]["lng"],index:i} as Coordinate)
      };
      const request = {clientEmail:this.authenticationService.loggedUserInfo$.value["email"], distance:this.routes[0].distance, carType:this.selectedCarType,
                        additionalServices:this.getAdditionalServices(), coordinates:coordinates,rideType:"Single"} as RideRequest;
      this.mapService.orderRide(request).subscribe();
      this.orderStatus = 1;
      this._snackBar.openFromComponent(SnackbarComponent,{data:"Waiting for driver."});
    }
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
