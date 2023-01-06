import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientPageComponent } from './client-page/client-page.component';
import { ClientProfileComponent } from './client-page/client-profile/client-profile.component';

const routes: Routes = [
  {path:'', component:ClientPageComponent ,children:[
    {path:"profile", title:'client-profile', component:ClientProfileComponent, outlet:"co", data: {role:"CLIENT"}},
]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
