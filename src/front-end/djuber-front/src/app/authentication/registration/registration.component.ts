import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import { Observable } from 'rxjs';
import { tap } from 'rxjs-compat/operators/tap';
import CustomValidators from 'src/app/utility/customValidators';
import { AuthenticationService } from '../authentication.service';
import { RegistrationSubmit } from '../registrationSubmit';

@Component({
  selector: 'djuber-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
})
export class RegistrationComponent implements OnInit {

  selectedCity : string = 'Novi Sad';

  cities:string[] = ['Novi Sad','Beograd']

  profilePicture;

  firstFormGroup = this._formBuilder.group({
    email : ['', [Validators.required, Validators.email]],
    password : ['',Validators.required],
    confirmPassword : ['',[Validators.required]],
  },{validators: [CustomValidators.matchPasswords('password','confirmPassword')]});
  secondFormGroup = this._formBuilder.group({
    firstName : ['', [Validators.required]],
    lastName : ['', [Validators.required]],
    phoneNumber : ['', [Validators.required,Validators.pattern(/^[+]?[0-9]{8,13}$/)]],
  });


  constructor(private _formBuilder: FormBuilder, private authenticationService:AuthenticationService) {
  }


  ngOnInit(): void {
  }

  signUp():void{
    if(this.firstFormGroup.status === "VALID" && this.secondFormGroup.status==="VALID" ){
      const request = {email:this.firstFormGroup.controls["email"].value, "password":this.firstFormGroup.controls["password"].value,
      "confirmPassword":this.firstFormGroup.controls["confirmPassword"].value, "firstName":this.secondFormGroup.controls.firstName.value,
      "lastName":this.secondFormGroup.controls.lastName.value,
      "city":this.selectedCity,"phoneNumber":this.secondFormGroup.controls.phoneNumber.value,
      } as RegistrationSubmit;
      if(this.profilePicture !== undefined){
        request.picture = this.profilePicture;
      }
      this.authenticationService.signUp(request).subscribe();
    }
  }

  uploadFile(files: FileList) {
    const file = files.item(0);
    const reader = new FileReader();

    reader.readAsDataURL(file);
    reader.onload = () => {
      this.profilePicture = reader.result;
      const base64String = reader.result as string;
    };

  }

  clearPicture(){
    this.profilePicture = undefined;
  }

  getErrorMessage(num : number): string{
    if(num === 1){
      if(this.firstFormGroup.controls["email"].status === "INVALID"){
        return "Email is required.";
      };
      if(this.firstFormGroup.controls["password"].status === "INVALID"){
        return "Password is required.";
      }
      if(this.firstFormGroup.controls["confirmPassword"].status ==="INVALID"){
        if(this.firstFormGroup.controls["confirmPassword"].hasError("required")){
          return "Confirm password.";
        }else{
          return "Passwords must match.";
        }
      }
    }else{
      if(this.secondFormGroup.controls.firstName.status === "INVALID"){
        return "First name is required.";
      };
      if(this.secondFormGroup.controls.lastName.status === "INVALID"){
        return "Last name is required.";
      }
      if(this.secondFormGroup.controls.phoneNumber.status ==="INVALID"){
        return "Invalid phone number.";
      }
    }
    return "";

  }

}
