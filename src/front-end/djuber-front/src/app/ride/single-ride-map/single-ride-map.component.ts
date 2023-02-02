import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as L from 'leaflet';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { RideService } from '../ride.service';
import { RideResponse } from '../rideResponse';
import { CoordinateResponse } from '../coordinateResponse';
import { Coordinate } from 'src/app/home-page/map/coordinate';
import { AuthenticationService } from 'src/app/authentication/authentication.service';
import { HashService } from 'src/app/utility/hash-service.service';
import { RideUpdateResponse } from '../rideUpdateResponse';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CancelRideNoteDialogComponent } from '../dialogs/cancel-ride-note-dialog/cancel-ride-note-dialog.component';



@Component({
  selector: 'djuber-single-ride-map',
  templateUrl: './single-ride-map.component.html',
  styleUrls: ['./single-ride-map.component.css']
})
export class SingleRideMapComponent implements OnInit {

  rideId:string;
  ride:RideResponse;
  rideStatus:string ="Pending";
  routePolyLine : L.Polyline;
  finish: L.Marker;
  start: L.Marker;
  minutesRemaining:number = 0;

  map!: L.Map;

  url = 'http://localhost:8080';
  socket?: WebSocket;
  stompClient?: Stomp.Client;

  options = {
    layers: getLayers(),
    zoom: 16,
    center: { lat: 45.25461307185434, lng:  19.842973257328783 }
  }
  driverMarker:L.Marker;

  constructor(private route: ActivatedRoute, private rideService:RideService,public matDialog: MatDialog , private snackBar : MatSnackBar, private authService : AuthenticationService, private hashService:HashService, private router:Router) { }


  ngOnInit(): void {
    this.rideId = this.route.snapshot.paramMap.get('id');
    this.openSocket();
    this.rideService.getRideResponse(this.rideId).subscribe({
      next:(response) =>{
        this.ride = response;
        this.rideStatus = response.rideStatus;
        this.ride.coordinates.sort((a: CoordinateResponse, b:Coordinate) => a.index - b.index);
        this.drawRoute(this.ride);
        this.start = this.drawMarker(this.ride.coordinates.find(coordinate => coordinate.index===0),"person","Starting location");
        this.finish = this.drawMarker(this.ride.coordinates.find(coordinate => coordinate.index===(this.ride.coordinates.length-1)),"finish","Starting location");
      }
    });
  }

  onMapReady($event: L.Map) {
    this.map = $event;
  }

  private openSocket(){
    this.socket = new SockJS(this.url + '/singleRide',{
      transports: ['xhr-streaming']
    });
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({}, (frame) => {
      //func = what to do when connection is established
      this.stompClient!.subscribe(
        '/topic/singleRide/'+this.rideId ,
        (response) => {
          //func = what to do when client receives data (messages)
          const RideUpdateResponse = JSON.parse(response["body"]) as RideUpdateResponse;
          this.updateRideStatus(RideUpdateResponse);
        },
        (error) =>{
          console.log(error);
        }
      );
    });
  }

  drawRoute(ride:RideResponse){
    const points = ride.coordinates.map(coordinate => {return {lat:coordinate.lat, lng:coordinate.lon}})
    const polyline = new L.Polyline(points, { color: 'red' }).addTo(this.map);
    polyline.bindTooltip("Your route");
    this.routePolyLine = polyline;
  }

  private drawMarker(coordinate:CoordinateResponse, iconUrlPart:string, tooltipText:string){
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

  userIsDriver(){
    const role = this.authService.getLoggedUserRole();
    return (this.hashService.matchRoles("DRIVER",role));}

  cancelRide(){
    this.rideService.declineAssignedRide(this.rideId).subscribe({complete:() =>{
      const dialogConfig = new MatDialogConfig();
      dialogConfig.disableClose = true;
      dialogConfig.id = "driver-note-modal";
      dialogConfig.data = {data:this.rideId};
      dialogConfig.height = "40%";
      dialogConfig.width = "20%";
      this.matDialog.open(CancelRideNoteDialogComponent, dialogConfig);
    }});
  }

  reportDriver(){
    
  }

  private updateRideStatus(rideUpdateResponse:RideUpdateResponse){
    if(rideUpdateResponse.rideStatus === "CANCELED"){
      this.snackBar.openFromComponent(SnackbarComponent,{data:"Ride has been canceled by the driver"});
      this.minutesRemaining = 0;
      this.router.navigate(["/"]);
    }
    if(rideUpdateResponse.rideStatus==="DONE"){
      if(this.userIsDriver()){
        this.minutesRemaining = 0;
        this.router.navigate(["/"]);
      }else{
        this.snackBar.openFromComponent(SnackbarComponent,{data:"Thank you for using our services"});
        this.router.navigate(["rideReview",this.rideId]);
      }
    }else{
      if(this.driverMarker !== undefined){
        this.driverMarker.remove();
      }
      this.driverMarker = L.marker([rideUpdateResponse.lat, rideUpdateResponse.lon], {
        icon: L.icon({
            iconUrl: 'assets/car.svg',
            iconSize: [30, 30],
            iconAnchor: [10, 10],
            popupAnchor: [0, 0]
        }),
        title: 'Driver'
      }).addTo(this.map);
      this.map.panTo([rideUpdateResponse.lat, rideUpdateResponse.lon]);
      this.driverMarker.bindTooltip("Driver");
      this.rideStatus = rideUpdateResponse.rideStatus;
      this.minutesRemaining = rideUpdateResponse.minutesRemaining;
    }
  }}



export const getLayers = (): L.Layer[] => {
  return [
    // Basic style
    new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    } as L.TileLayerOptions),
  ] as L.Layer[];
};
