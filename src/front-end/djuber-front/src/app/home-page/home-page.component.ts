import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from '../authentication/authentication.service';
import { SnackbarComponent } from '../snackbar/snackbar.component';
import { HashService } from '../utility/hash-service.service';
import * as Leaflet from "leaflet";

@Component({
  selector: 'djuber-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {


  // you want to get it of the window global
  //const provider = new GeoSearch.OpenStreetMapProvider();

  logged! : boolean;

  toggledLiveChat:boolean;

  constructor(private authenticationService:AuthenticationService,private _snackBar: MatSnackBar, private hashService:HashService) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
    this.toggledLiveChat = false;
  }


  ngOnInit(): void {
  }


  canUserContactLiveChat(){
    if(this.logged)
    return (this.authenticationService.getLoggedUserRole()===this.hashService.hashString("CLIENT") || this.authenticationService.getLoggedUserRole()===this.hashService.hashString("DRIVER"));
  }

  toggleLiveChat(){
    if(this.canUserContactLiveChat()){
      this.toggledLiveChat = true;
    }
  }

  close(){
    this.toggledLiveChat = false;
  }

}
