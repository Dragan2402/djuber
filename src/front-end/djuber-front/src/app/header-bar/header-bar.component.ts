import { Component, OnInit , OnChanges, SimpleChanges} from '@angular/core';
import { Router } from '@angular/router';
import {AuthenticationService} from "../authentication/authentication.service";
import { LoggedUser } from '../authentication/loggedUser';
import { ClientService } from '../client/client.service';
import { HashService } from '../utility/hash-service.service';

@Component({
  selector: 'djuber-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrls: ['./header-bar.component.css']
})
export class HeaderBarComponent implements OnInit {

  logged! : boolean;

  userInfo! : LoggedUser;

  balance:number = 0;

  constructor(private authenticationService : AuthenticationService, private router:Router, private hashService: HashService) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
    this.authenticationService.loggedUserInfo$.subscribe((loggedUser:LoggedUser) =>{
      this.userInfo = loggedUser;
    })
   }

  ngOnInit(): void {
    this.authenticationService.getLoggedClientBalance().subscribe({next:(v) =>{
      this.balance = v["balance"];
    }})
  }

  logout():void{
    this.authenticationService.logout();
  }

  loggedUserClient(){
    const role = this.authenticationService.getLoggedUserRole();
    return (role === this.hashService.hashString("CLIENT"));
  }

  loggedClientBalance(){
    this.authenticationService.getLoggedClientBalance().subscribe({next:(v) =>{
      this.balance = v["balance"];
    }})
  }


  navigateToProfile(){
    const role = this.authenticationService.getLoggedUserRole();
    if(role === this.hashService.hashString("ADMIN")){
      this.router.navigate(["/admin"]);
    }else if (role === this.hashService.hashString("CLIENT")){
      this.router.navigate(["/client"]);
    }else if (role === this.hashService.hashString("DRIVER")){
      this.router.navigate(["/driver"]);
    }
  }


}
