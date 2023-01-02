import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthenticationRoutingModule } from './authentication-routing.module';
import { LoginComponent } from './login/login.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import { SpinnerComponent } from '../animations/spinner/spinner.component';
import {MatStepperModule} from '@angular/material/stepper';
import { RegistrationComponent } from './registration/registration.component';
import {MatSelectModule} from '@angular/material/select';

@NgModule({
  declarations: [
    LoginComponent,
    RegistrationComponent,
    SpinnerComponent
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatStepperModule,
    MatSelectModule
  ]
})
export class AuthenticationModule { }
