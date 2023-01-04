import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../authentication/authentication.service';
import { HashService } from '../utility/hash-service.service';

@Injectable({
  providedIn: 'root'
})
export class RolesGuard implements CanActivate, CanActivateChild, CanLoad {

  hashedProvidedRole :string;

  constructor(private authenticationService:AuthenticationService, private router:Router, private hashService: HashService){
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.hashService.matchRoles(route.data["role"], this.authenticationService.getLoggedUserRole())? true: this.router.navigate(["/"]);
  }
  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.hashService.matchRoles(childRoute.data["role"], this.authenticationService.getLoggedUserRole())? true: this.router.navigate(["/"]);
  }
  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.hashService.matchRoles(route.data["role"], this.authenticationService.getLoggedUserRole())? true: this.router.navigate(["/"]);

  }
}
