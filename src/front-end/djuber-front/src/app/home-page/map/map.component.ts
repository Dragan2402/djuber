import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as L from 'leaflet';
import { AuthenticationService } from 'src/app/authentication/authentication.service';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { HashService } from 'src/app/utility/hash-service.service';
import { DriverLocation } from './driverLocation';
import { MapService } from './map.service';
import {Coordinate} from "./coordinate";
import { Route } from './route';
import {firstValueFrom, lastValueFrom} from "rxjs";
import { RideRequest } from './rideRequest';
import { Validators, FormControl } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ClientFavouriteRidesDialogComponent } from 'src/app/home-page/map/client-favourite-rides-dialog/client-favourite-rides-dialog.component';

@Component({
  selector: 'djuber-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  emailFormControl = new FormControl('', [Validators.email]);

  map!: L.Map;

  clientLocation : Coordinate;

  desiredLocation : Coordinate;

  clientMarker :L.Marker;

  loggedDriverLocation: DriverLocation;

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

  sharedRide: boolean = false;

  sharedRideClientEmails: string[] = [];

  selectedCarType:string = "Sedan";

  stopNames:string[] =[];

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

  firstSharedRideClient: string = '';

  constructor(private mapService:MapService, private _snackBar: MatSnackBar, private authenticationService : AuthenticationService, private hashService:HashService, public matDialog: MatDialog) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
  }


  ngOnInit(): void {
    if(this.logged && this.hashService.matchRoles("DRIVER",this.authenticationService.getLoggedUserRole())){
      this.mapService.getLoggedDriverLocation().subscribe({next:(driver)=>{
       this.drawMarker({lat:driver.lat, lon: driver.lon, index:0 } as Coordinate,"car","You",true);
       this.loggedDriverLocation = driver;
      }})
    }else{
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(this.setGeoLocation.bind(this));
     }
    }
   this.updateDriversLocation();
  }

  async updateDriversLocation(){
    while(true){
      await new Promise(f => setTimeout(f, 5000));
      this.setDriversLocation();
    }
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

  addSharedRideClient(index: number) {
    this.sharedRideClientEmails.splice(index+1, 0, "");
  }

  removeSharedRideClient(index: number) {
    this.sharedRideClientEmails.splice(index, 1);
  }



  onValueUpdate(event, index){
    this.routePathAddresses[index]=event.target.value;
  }
  onEmailUpdate(event, i) {
    this.sharedRideClientEmails[i] = event.target.value;
  }

  setGeoLocation(position: { coords: { latitude: any; longitude: any } }) {
    const {
       coords: { latitude, longitude },
    } = position;
    this.clientLocation = {lat:latitude, lon:longitude, index:0} as Coordinate;
    this.clientMarker = this.drawMarker(this.clientLocation,"person","Your location", true);
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

  drawDriverLocation(driver:DriverLocation){
    if(driver.inRide === true){
      this.availableDriversMarkers.push(this.drawMarker({lat:driver.lat, lon: driver.lon, index:0 } as Coordinate,"carGray","In ride",false));
    }else{
      this.availableDriversMarkers.push(this.drawMarker({lat:driver.lat, lon: driver.lon, index:0 } as Coordinate,"car","Available driver", false));
    }
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
    this.stopNames.splice(0, this.stopNames.length);
    this.orderStatus = 0;
    this.selectedCarType = "Sedan";
    this.extraLuggage = false;
    this.pets = false;
    this.luggageTransport = false;
    this.knowingEnglish = false;
  }

  setDriversLocation(){
    this.mapService.getDriversLocation().subscribe({
      next:(v) =>{
        this.availableDriversMarkers.forEach(m => {m.remove()});
        this.availableDriversMarkers.splice(0, this.availableDriversMarkers.length);
        v.forEach(d => { if(this.loggedDriverLocation === undefined || this.loggedDriverLocation.id !==d.id){
          this.drawDriverLocation(d);
        };
      })
    }})
  }

  private async getCoordinateFromAddress(address:string, id:number){
    const result$ = this.mapService.searchLocationAsync(address).pipe();
    const response = await firstValueFrom(result$);
    this.stopNames.push(response["features"][0]["properties"]["name"]);
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

  toggleFavourteRidesDialog(){
    const dialogConfig = new MatDialogConfig();
    // The user can't close the dialog by clicking outside its body
    dialogConfig.disableClose = false;
    dialogConfig.id = "fav-routes-dialog";
    dialogConfig.height = "55%";
    dialogConfig.width = "50%";
    // https://material.angular.io/components/dialog/overview
    const dialogRef = this.matDialog.open(ClientFavouriteRidesDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(route =>{
      if(route !== undefined){
        this.routes.push(route);
        this.stopNames = route.stopNames;
        this.drawRoute(route);
        this.orderStatus=2;
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
    if(!this.differentStartingPoint){
      const name = await this.mapService.getClientLocationName(this.clientLocation);
      this.stopNames.push(name);
    }
    this.routePathPoints.push(clientPoint);
    this.clientMarker = this.drawMarker(clientPoint,"person","Starting location", true);

    for(let i = 0 ; i < this.routePathAddresses.length ; i++){
      this.routePathPoints.push(await this.getCoordinateFromAddress(this.routePathAddresses[i], i+1));
    }

    const desiredPoint = await this.getCoordinateFromAddress(this.desiredAddress,9999);
    this.routePathPoints.push(desiredPoint);
    this.desireMarker = this.drawMarker(desiredPoint, "finish",this.desiredAddress.toUpperCase(), true);

    this.routePathPoints.sort((a, b) => {return a.index - b.index});

    const preference = this.fastestRoute ? "fastest": "shortest";
    this.getRoutesFromCoordinates(this.routePathPoints, preference);
  }

  private drawMarker(coordinate:Coordinate, iconUrlPart:string, tooltipText:string, panTo:boolean){
    const marker = L.marker([coordinate.lat, coordinate.lon], {
      icon: L.icon({
          iconUrl: 'assets/'+iconUrlPart+'.svg',
          iconSize: [30, 30],
          iconAnchor: [10, 10],
          popupAnchor: [0, 0]
      }),
      title: 'Desired location'
    }).addTo(this.map);
    if(panTo){
      this.map.panTo([coordinate.lat, coordinate.lon]);
    }
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

  async orderRide(){
    let clientEmails: string[] = [this.firstSharedRideClient].concat(this.sharedRideClientEmails);
    if (this.sharedRide) {
      let valid = await this.areSharedRideClientsValid(clientEmails);
      if (!valid) {
        return;
      }
    }

    const coordinates = [];
    const points = this.routes[0].points;
    if(points.length >= 2){
      for(let i = 0; i < points.length ; i++){
        coordinates.push({lat:points[i]["lat"], lon:points[i]["lng"],index:i} as Coordinate)
      };
      let passengerEmails: string[] = this.sharedRide? [this.authenticationService.getLoggedUserEmail()].concat(clientEmails):[this.authenticationService.getLoggedUserEmail()];
      console.log(passengerEmails);
      const request = {
        clientEmails: passengerEmails,
        distance: this.routes[0].distance,
        carType: this.selectedCarType,
        additionalServices: this.getAdditionalServices(),
        coordinates: coordinates,
        rideType: this.sharedRide ? "Share ride" : "Single",
        stopNames:this.stopNames
      } as RideRequest;
      console.log(request);
      this.mapService.orderRide(request).subscribe({error:(err)=>{
        this._snackBar.openFromComponent(SnackbarComponent,{data:"Error while trying to order ride."});
        this.orderStatus = 0;
      }});
      this.orderStatus = 1;
      this._snackBar.openFromComponent(SnackbarComponent,{data:"Waiting for driver."});
    }
  }

  async areSharedRideClientsValid(clientEmails: string[]): Promise<boolean> {
    for (let email of clientEmails) {
      let formControl = new FormControl(email, [Validators.email]);
      if (formControl.status === "INVALID") {
        this._snackBar.openFromComponent(SnackbarComponent, {
          data: `Input '${email}' is not a valid email address.`
        });
        return false;
      }

      let loggedUserEmail = this.authenticationService.getLoggedUserEmail();
      if (email == loggedUserEmail) {
        this._snackBar.openFromComponent(SnackbarComponent, {
          data: `Do not add your own e-mail address to passenger addresses.`
        });
        return false;
      }
    }

    const resultExist$ = this.mapService.checkIfClientsExist(clientEmails).pipe();
    const responseExist = await firstValueFrom(resultExist$);
    if (responseExist !== null) {
      this._snackBar.openFromComponent(SnackbarComponent, {
        data: `Client with e-mail '${responseExist}' not found.`
      });
      return false;
    }

    const resultBlocked$ = this.mapService.checkIfClientsAreBlocked(clientEmails).pipe();
    const responseBlocked = await firstValueFrom(resultBlocked$);
    if (responseBlocked !== null) {
      this._snackBar.openFromComponent(SnackbarComponent, {
        data: `Client with e-mail '${responseBlocked}' is blocked.`
      });
      return false;
    }

    return true;
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
