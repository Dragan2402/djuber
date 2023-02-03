import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DriverPageComponent} from './driver-page/driver-page.component';
import {DriverProfileComponent} from './driver-page/driver-profile/driver-profile.component';
import {DriverRidesComponent} from "./driver-page/driver-rides/driver-rides.component";

const routes: Routes = [
  {path:'', component:DriverPageComponent ,children:[
      {path:"profile",  component:DriverProfileComponent, outlet:"co", data: {role:"DRIVER"}},
      {path:"rides", title:'driver-rides', component:DriverRidesComponent, outlet:"co", data: {role:"DRIVER"}}
]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
