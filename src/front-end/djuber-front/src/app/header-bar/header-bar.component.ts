import { Component, OnInit , OnChanges, SimpleChanges} from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import {AuthenticationService} from "../authentication/authentication.service";
import { LoggedUser } from '../authentication/loggedUser';
import { ClientService } from '../client/client.service';
import { SnackbarComponent } from '../snackbar/snackbar.component';
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

  status:string = "Active";

  constructor(private authenticationService : AuthenticationService, private router:Router, private hashService: HashService, private matSnackbar:MatSnackBar) {
    this.authenticationService.logged$.subscribe((attr:boolean) =>{
      this.logged = attr;
    });
    this.authenticationService.loggedUserInfo$.subscribe((loggedUser:LoggedUser) =>{
      this.userInfo = loggedUser;
    })
   }

  ngOnInit(): void {
    if(this.loggedUserClient()){
      this.authenticationService.getLoggedClientBalance().subscribe({next:(v) =>{
        this.balance = v["balance"];
      }})
    }else if(this.loggedUserDriver()){
      this.authenticationService.activateLoggedDriver().subscribe({complete:()=>{
        this.status ="Active";
      }, error:(err)=>{
        this.status = "Inactive";
      }})
    }
  }

  changeStatus(){
    if(this.status==="Inactive"){
      this.authenticationService.activateLoggedDriver().subscribe({complete:()=>{
        this.status ="Active";
        this.matSnackbar.openFromComponent(SnackbarComponent,{data:"You are now active."});
      }, error:(err)=>{
        this.status = "Inactive";
        this.matSnackbar.openFromComponent(SnackbarComponent,{data:"You have reached ours limit. You have been moved to inactive drivers."});
      }})
    }else{
      this.authenticationService.deactivateLoggedDriver().subscribe({complete:()=>{
        this.status ="Inactive";
        this.matSnackbar.openFromComponent(SnackbarComponent,{data:"You are now inactive."});
      }, error:(err)=>{
        this.status = "Inactive";
        this.matSnackbar.openFromComponent(SnackbarComponent,{data:"You are now inactive."});
      }})
    }
  }

  logout():void{
    this.authenticationService.logout();
  }

  loggedUserClient(){
    const role = this.authenticationService.getLoggedUserRole();
    return (role === this.hashService.hashString("CLIENT"));
  }

  loggedUserDriver(){
    const role = this.authenticationService.getLoggedUserRole();
    return (role === this.hashService.hashString("DRIVER"));
  }

  loggedClientBalance(){
    if(this.logged && this.loggedUserClient()){
      this.authenticationService.getLoggedClientBalance().subscribe({next:(v) =>{
        this.balance = v["balance"];
      }})
    }
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
