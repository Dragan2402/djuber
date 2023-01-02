import { Component, OnInit , OnChanges, SimpleChanges} from '@angular/core';
import {AuthenticationService} from "../authentication/authentication.service";
import { LoggedUser } from '../authentication/loggedUser';

@Component({
  selector: 'djuber-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrls: ['./header-bar.component.css']
})
export class HeaderBarComponent implements OnInit , OnChanges{

  logged! : boolean;

  userInfo! : LoggedUser;

  constructor(private authenticationService : AuthenticationService) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
    this.authenticationService.loggedUserInfo$.subscribe((loggedUser:LoggedUser) =>{
      this.userInfo = loggedUser;
    })
   }
  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
  }

  ngOnInit(): void {
  }

  logout():void{
    this.authenticationService.logout();
  }


}
