import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import CustomValidators from 'src/app/utility/customValidators';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'djuber-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  token:string;

  controlGroup = this._formBuilder.group({
    password : ['',Validators.required],
    confirmPassword : ['',[Validators.required]],
  },{validators: [CustomValidators.matchPasswords('password','confirmPassword')]});

  constructor(private _formBuilder: FormBuilder, private authenticationService:AuthenticationService, private route: ActivatedRoute,  private router: Router) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get("token");
  }

  reset(){
    if(this.controlGroup.status === "VALID"){
      this.authenticationService
      .resetPassword({"token":this.token,"password":this.controlGroup.controls['password'].value,"confirmPassword":this.controlGroup.controls['confirmPassword'].value})
      .subscribe((res) => {
        this.router.navigate(["/"]);
      });
    }
  }

}
