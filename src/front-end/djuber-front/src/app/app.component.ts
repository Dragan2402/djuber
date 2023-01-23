import { Component, DoCheck } from '@angular/core';
import { AuthenticationService } from './authentication/authentication.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { HashService } from './utility/hash-service.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AcceptRideDriverDialogComponent } from './ride/dialogs/accept-ride-driver-dialog/accept-ride-driver-dialog.component';

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

  constructor(private authService:AuthenticationService, private hashService:HashService, public matDialog: MatDialog){}

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
          //func = what to do when client receives data (messages)
          this.toggleAcceptRideDialog(response);
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
          console.log(response);
        },
        (error) =>{
          console.log(error);
        }
      );
    });
  }

  private toggleAcceptRideDialog(data){
    const dialogConfig = new MatDialogConfig();
    // The user can't close the dialog by clicking outside its body
    dialogConfig.disableClose = true;
    dialogConfig.id = "note-modal";
    dialogConfig.data = {data};
    dialogConfig.height = "20%";
    dialogConfig.width = "20%";
    // https://material.angular.io/components/dialog/overview
    this.matDialog.open(AcceptRideDriverDialogComponent, dialogConfig);
  }

}
