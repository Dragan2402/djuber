import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { RegistrationComponent } from './registration/registration.component';
import { VerifyAccountComponent } from './verify-account/verify-account.component';

const routes: Routes = [
  {path:'', children:[
    {path:"login", component:LoginComponent},
    {path: "register",component:RegistrationComponent},
    {path:"verify", component:VerifyAccountComponent},
    {path:"password-reset", component:PasswordResetComponent}]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthenticationRoutingModule { }
