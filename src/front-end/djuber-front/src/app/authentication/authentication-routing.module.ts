import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from '../guards/login.guard';
import { LoginComponent } from './login/login.component';
import { PasswordChangeComponent } from './password-change/password-change.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { PasswordResetRequestComponent } from './password-reset/password-resset-request/password-reset-request.component';
import { RegistrationComponent } from './registration/registration.component';
import { VerifyAccountComponent } from './verify-account/verify-account.component';

const routes: Routes = [
  {path:'', children:[
    {path:"login", component:LoginComponent},
    {path: "register",component:RegistrationComponent},
    {path:"verify/:token", component:VerifyAccountComponent},
    {path:"passwordReset/:token", component:PasswordResetComponent},
    {path: "requestPasswordReset", component: PasswordResetRequestComponent},
    {path: "changePassword", component: PasswordChangeComponent, canActivate:[LoginGuard], canLoad:[LoginGuard]}]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthenticationRoutingModule { }
