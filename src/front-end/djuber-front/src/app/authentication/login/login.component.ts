
import { Component, OnDestroy, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'djuber-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent   {

  email = new FormControl('', [Validators.required, Validators.email]);

  password = new FormControl('',[Validators.required]);


  constructor(private authenticationService:AuthenticationService) {}

  getEmailErrorMessage() {
    return this.email.hasError('required') ? '' :
        this.email.hasError('email') ? 'Invalid email' :
            '';
  }

  getPasswordErrorMessage() {
    return this.password.hasError('required') ? 'Required': '';
  }

  public login():void{
    if(this.email.valid && this.password.valid){
      this.authenticationService.login(<string>this.email.value, <string>this.password.value).subscribe();
    }
  }

  facebookSignin(): void {
    this.authenticationService.facebookSignIn();
  }

}
