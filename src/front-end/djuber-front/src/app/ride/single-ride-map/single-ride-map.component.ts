import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as L from 'leaflet';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { RideUpdateResponse } from '../rideUpdateResponse';


@Component({
  selector: 'djuber-single-ride-map',
  templateUrl: './single-ride-map.component.html',
  styleUrls: ['./single-ride-map.component.css']
})
export class SingleRideMapComponent implements OnInit {

  routeId:string;

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

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.routeId = this.route.snapshot.paramMap.get('id');
    this.openSocket();
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
        '/topic/singleRide/'+this.routeId ,
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

  private updateRideStatus(rideUpdateResponse:RideUpdateResponse){
    if(rideUpdateResponse.rideStatus==="DONE"){
      console.log("RIDE DONE PLEASE NAVIGATE");
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
