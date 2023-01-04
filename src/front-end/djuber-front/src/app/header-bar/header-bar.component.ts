import { Component, OnInit , OnChanges, SimpleChanges} from '@angular/core';
import { Router } from '@angular/router';
import {AuthenticationService} from "../authentication/authentication.service";
import { LoggedUser } from '../authentication/loggedUser';

@Component({
  selector: 'djuber-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrls: ['./header-bar.component.css']
})
export class HeaderBarComponent implements OnInit {

  logged! : boolean;

  userInfo! : LoggedUser;

  constructor(private authenticationService : AuthenticationService, private router:Router) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
    this.authenticationService.loggedUserInfo$.subscribe((loggedUser:LoggedUser) =>{
      this.userInfo = loggedUser;
    })
   }

  ngOnInit(): void {
    if(this.logged){

    }
  }

  logout():void{
    this.authenticationService.logout();
  }

  navigateToProfile(){
    this.router.navigate(["/admin"]);
  }


}
