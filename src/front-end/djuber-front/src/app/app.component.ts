import { Component, DoCheck } from '@angular/core';
import { AuthenticationService } from './authentication/authentication.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { HashService } from './utility/hash-service.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AcceptRideDriverDialogComponent } from './ride/dialogs/accept-ride-driver-dialog/accept-ride-driver-dialog.component';
import { RideSocketResponse } from './ride/rideSocketResponse';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from './snackbar/snackbar.component';
import { AcceptRideClientDialogComponent } from './ride/dialogs/accept-ride-client-dialog/accept-ride-client-dialog.component';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'djuber-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements DoCheck{

  title = 'djuber-front';

  socketOpened:boolean = false;

  identityId:number;

  url = 'http://localhost:8080';
  socket?: WebSocket;
  stompClient?: Stomp.Client;

  constructor(private authService:AuthenticationService, private hashService:HashService, public matDialog: MatDialog, private router:Router, private _snackBar:MatSnackBar){}

  ngDoCheck(): void {
    if(this.authService.logged$.value === true && this.socketOpened === false){
      console.log("Opening socket");
      this.authService.getLoggedUserIdentityId().subscribe({
        next:(v) =>{
          this.identityId=v["id"];
          const role = this.authService.getLoggedUserRole();
          if (role === this.hashService.hashString("CLIENT")){
            this.openRideSocketClient();
          }else if (role === this.hashService.hashString("DRIVER")){
            this.openRideSocketDriver();
          }
        }
      });
      this.socketOpened = true;
    }else if(this.authService.logged$.value === false && this.socketOpened === true){
      this.socketOpened = false;
      console.log("Closing socket");
    }
  }

  private openRideSocketDriver(){
    this.socket = new SockJS(this.url + '/ride',{
      transports: ['xhr-streaming']
    });
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({}, (frame) => {
      //func = what to do when connection is established
      this.stompClient!.subscribe(
        '/topic/ride/'+this.identityId ,
        (response) => {
          const rideSocketResponse = JSON.parse(response["body"]) as RideSocketResponse;
          if(rideSocketResponse.status === "RIDE_DRIVER_OFFER"){
            this.toggleAcceptRideDriverDialog(rideSocketResponse);
          }
        }
      );
    });
  }

  private openRideSocketClient(){
    this.socket = new SockJS(this.url + '/ride',{
      transports: ['xhr-streaming']
    });
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({}, (frame) => {
      //func = what to do when connection is established
      this.stompClient!.subscribe(
        '/topic/ride/'+this.identityId ,
        (response) => {
          //func = what to do when client receives data (messages)
          const rideSocketResponse = JSON.parse(response["body"]) as RideSocketResponse;
          if(rideSocketResponse.status === "RIDE_CLIENT_ACCEPTED"){
            this.router.navigate(["singleRideMap",rideSocketResponse.rideId]);
          }else if(rideSocketResponse.status === "RIDE_CLIENT_DECLINED"){
            this._snackBar.openFromComponent(SnackbarComponent,{data:"Sorry, but we did not manage to find a driver for your ride."});
          }else if(rideSocketResponse.status === "RIDE_CLIENT_OFFER"){
           this.toggleAcceptRideClientDialog(response);
          }
          console.log(response);
        },
        (error) =>{
          console.log(error);
        }
      );
    });
  }

  private toggleAcceptRideDriverDialog(response : RideSocketResponse){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.id = "driver-modal";
    dialogConfig.data = {response};
    dialogConfig.height = "20%";
    dialogConfig.width = "20%";
    this.matDialog.open(AcceptRideDriverDialogComponent, dialogConfig);
  }

  private toggleAcceptRideClientDialog(response : RideSocketResponse){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.id = "client-modal";
    dialogConfig.data = {response};
    dialogConfig.height = "20%";
    dialogConfig.width = "20%";
    this.matDialog.open(AcceptRideClientDialogComponent, dialogConfig);
  }

}
