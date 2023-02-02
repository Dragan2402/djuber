import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientPageComponent } from './client-page/client-page.component';
import { ClientPaymentComponent } from './client-page/client-payment/client-payment.component';
import { ClientProfileComponent } from './client-page/client-profile/client-profile.component';
import {ClientRidesComponent} from "./client-page/client-rides/client-rides.component";
import {ClientReportsComponent} from "./client-page/client-report/client-reports.component";

const routes: Routes = [
  {path:'', component:ClientPageComponent ,children:[
    {path:"profile", title:'client-profile', component:ClientProfileComponent, outlet:"co", data: {role:"CLIENT"}},
    {path:"payment", title:'client-payment', component:ClientPaymentComponent, outlet:"co", data: {role:"CLIENT"}},
    {path:"rides", title:'client-rides', component:ClientRidesComponent, outlet:"co", data: {role:"CLIENT"}},
    {path:"reports", title:'client-reports', component:ClientReportsComponent, outlet:"co", data: {role:"CLIENT"}},
]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
