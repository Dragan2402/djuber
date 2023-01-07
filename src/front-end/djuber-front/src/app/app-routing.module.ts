import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from './guards/login.guard';
import { RolesGuard } from './guards/roles.guard';
import { HomePageComponent } from './home-page/home-page.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  {path:"homePage", component: HomePageComponent},
  {path:"authentication", loadChildren: ()=>
        import('./authentication/authentication.module').then((m) => m.AuthenticationModule)},
  {path:"admin", loadChildren: ()=>
        import('./admin/admin.module').then((m) => m.AdminModule), canActivate: [LoginGuard, RolesGuard], canActivateChild: [LoginGuard,RolesGuard], canLoad: [LoginGuard,RolesGuard],
      data: {role:"ADMIN"}},
  {path:"client", loadChildren: ()=>
      import('./client/client.module').then((m) => m.ClientModule), canActivate: [LoginGuard, RolesGuard], canActivateChild: [LoginGuard,RolesGuard], canLoad: [LoginGuard,RolesGuard],
    data: {role:"CLIENT"}},
  {path:"driver", loadChildren: ()=>
    import('./driver/driver.module').then((m) => m.DriverModule), canActivate: [LoginGuard, RolesGuard], canActivateChild: [LoginGuard,RolesGuard], canLoad: [LoginGuard,RolesGuard],
  data: {role:"DRIVER"}},
  {path: '' , redirectTo : '/homePage', pathMatch : 'full'},
  {path: "**", component: NotFoundComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
