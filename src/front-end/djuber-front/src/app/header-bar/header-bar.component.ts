import { Component, OnInit , OnChanges, SimpleChanges} from '@angular/core';
import { Router } from '@angular/router';
import {AuthenticationService} from "../authentication/authentication.service";
import { LoggedUser } from '../authentication/loggedUser';
import { HashService } from '../utility/hash-service.service';

@Component({
  selector: 'djuber-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrls: ['./header-bar.component.css']
})
export class HeaderBarComponent implements OnInit {

  logged! : boolean;

  userInfo! : LoggedUser;

  constructor(private authenticationService : AuthenticationService, private router:Router, private hashService: HashService) {
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
    const role = this.authenticationService.getLoggedUserRole();
    if(role === this.hashService.hashString("ADMIN")){
      this.router.navigate(["/admin"]);
    }else if (role === this.hashService.hashString("CLIENT")){
      this.router.navigate(["/client"]);
    }
  }


}
