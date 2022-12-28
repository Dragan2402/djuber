import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'djuber-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email]);

  password = new FormControl('',[Validators.required]);


  constructor(private authenticationService:AuthenticationService) { }

  ngOnInit(): void {
  }

  getEmailErrorMessage() {
    return this.email.hasError('required') ? '' :
        this.email.hasError('email') ? 'Invalid email' :
            '';
  }

  getPasswordErrorMessage() {
    return this.password.hasError('required') ? '': '';
  }

  public login():void{
    if(this.email.valid && this.password.valid){
      this.authenticationService.login(<string>this.email.value, <string>this.password.value).subscribe((response) =>{
        console.log(response);
      });
    }

  }
}
