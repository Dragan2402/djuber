import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { AdminProfileComponent } from './admin-page/admin-profile/admin-profile.component';
import { ClientPreviewComponent } from './admin-page/client-preview/client-preview.component';
import { DriversPreviewComponent } from './admin-page/drivers-preview/drivers-preview.component';
import { RegisterDriverComponent } from './admin-page/register-driver/register-driver.component';

const routes: Routes = [
  {path:'', component:AdminPageComponent, children:[
    {path:"profile", component:AdminProfileComponent, outlet:"ao", data: {role:"ADMIN"}},
    {path:"registerDriver", component:RegisterDriverComponent, outlet:"ao", data: {role:"ADMIN"}},
    {path:"previewDrivers", component:DriversPreviewComponent, outlet:"ao", data: {role:"ADMIN"}},
    {path:"previewClients", component:ClientPreviewComponent, outlet:"ao", data: {role:"ADMIN"}},
]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
