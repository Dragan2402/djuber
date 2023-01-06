import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import CustomValidators from 'src/app/utility/customValidators';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'djuber-password-change',
  templateUrl: './password-change.component.html',
  styleUrls: ['./password-change.component.css']
})
export class PasswordChangeComponent implements OnInit {

  controlGroup = this._formBuilder.group({
    password : ['',Validators.required],
    confirmPassword : ['',[Validators.required]],
  },{validators: [CustomValidators.matchPasswords('password','confirmPassword')]});

  constructor(private _formBuilder: FormBuilder, private authenticationService:AuthenticationService,  private router: Router,private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  change(){
    if(this.controlGroup.status === "VALID"){
      this.authenticationService
      .changePassword({"password":this.controlGroup.controls['password'].value,"confirmPassword":this.controlGroup.controls['confirmPassword'].value})
      .subscribe({
        next: (v) => {
          this._snackBar.openFromComponent(SnackbarComponent, {data:"Please login again."});
          console.log(v); this.authenticationService.logout()},
        error: (e) => console.error(e),
        complete: () => console.info('complete')
    })
    }
  }

}
