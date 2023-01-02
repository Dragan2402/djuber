import { HttpClient, HttpErrorResponse, HttpInterceptor } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import {localStorageToken} from "../utility/localstorage.token";
import { tap} from "rxjs-compat/operators/tap";
import {catchError} from 'rxjs/operators';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { LoggedUser } from './loggedUser';
import { RegistrationSubmit } from './registrationSubmit';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  loading$ = new BehaviorSubject<boolean>(false);

  logged$ = new BehaviorSubject<boolean>(this.isLoggedIn());

  loggedUserInfo$ = new BehaviorSubject<LoggedUser>(this.getLoggedUserInfo());

  constructor(private http:HttpClient,
    @Inject(localStorageToken) private localStorage : Storage,
    private route: Router) { }

  public login(email:string, password:string){
    const body = {
      email: email,
      password: password
    }
    return this.http.post("/api/auth/login",body).pipe(tap(res => this.setToken(res)),
    catchError((error : HttpErrorResponse): Observable<any> => {
      this.handleError(error);
      return of(null);
    }));
  }

  public signUp(request:RegistrationSubmit){
    return this.http.post<number>("api/auth/signUp",request);
  }


  private async setToken(token : any){
    this.loading$.next(true);

    this.localStorage.setItem("jwt", token["accessToken"])

    this.http.get<LoggedUser>("/api/auth/getLoggedUserInfo").pipe(tap(res => {
      this.localStorage.setItem("user-email",res.email);
      this.localStorage.setItem("user-first-name",res.firstName);
      this.localStorage.setItem("user-last-name",res.lastName);
      this.loggedUserInfo$.next(res);
    })).subscribe();
    await new Promise(r => setTimeout(r, 1000));
    this.logged$.next(true);
    this.loading$.next(false);
    this.route.navigate(['/'])
  }


  public logout():void {
    this.localStorage.removeItem("jwt");
    this.localStorage.removeItem("user-email");
    this.localStorage.removeItem("user-first-name");
    this.localStorage.removeItem("user-last-name");
    this.logged$.next(this.isLoggedIn());
  }

  private isLoggedIn(){
    return this.localStorage.getItem("jwt")!== null;
  }

  private getLoggedUserInfo():LoggedUser{
    return {email: this.localStorage.getItem("user-email"), firstName: this.localStorage.getItem("user-first-name"), lastName: this.localStorage.getItem("user-last-name")} as LoggedUser;
  }

  private async handleError(error : HttpErrorResponse){
    this.loading$.next(true);
    await new Promise(r => setTimeout(r, 1000));
    this.loading$.next(false);
  }

}
