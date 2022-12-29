import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  {path:"homePage", component: HomePageComponent},
  {path:"authentication", loadChildren: ()=>
        import('./authentication/authentication.module').then((m) => m.AuthenticationModule)},
  {path: '' , redirectTo : '/homePage', pathMatch : 'full'},
  {path: "**", component: NotFoundComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
