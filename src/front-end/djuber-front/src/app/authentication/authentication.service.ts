import { HttpClient, HttpErrorResponse, HttpInterceptor } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import {localStorageToken} from "../utility/localstorage.token";
import { tap} from "rxjs-compat/operators/tap";
import {catchError} from 'rxjs/operators';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { LoggedUser } from './loggedUser';
import { RegistrationSubmit } from './registrationSubmit';
import { FacebookLoginProvider, SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';
import { HashService } from '../utility/hash-service.service';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  loading$ = new BehaviorSubject<boolean>(false);

  logged$ = new BehaviorSubject<boolean>(this.isLoggedIn());

  loggedUserInfo$ = new BehaviorSubject<LoggedUser>(this.getLoggedUserInfo());

  constructor(private http:HttpClient,
    @Inject(localStorageToken) private localStorage : Storage,
    private route: Router,
    private socialAuthService: SocialAuthService,
    private hashService: HashService) {
      this.socialAuthService.authState.subscribe((user) => {this.socialLogin(user)});
    }


  public socialLogin(socialUser: SocialUser){


    this.http.post("/api/auth/socialSignIn",socialUser).pipe(tap(res => this.setToken(res)),
      catchError((error : HttpErrorResponse): Observable<any> => {
        this.handleError(error);
        return of(null);
      })).subscribe();
  }

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
    return this.http.post<number>("api/auth/signUp",request).pipe(tap( res => {
      this.reportSuccessfulRegistration();
    }),catchError((error : HttpErrorResponse): Observable<any> => {
      this.handleError(error);
      return of(null);
    }));
  }

  public facebookSignIn(){
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  public verifyClientAccount(token: string) {
    return this.http.put("api/auth/verifyClientAccount",{"token":token});
  }


  public async setToken(token : any){
    this.loading$.next(true);
    this.localStorage.setItem("jwt", token["accessToken"])
    this.localStorage.setItem("jwt-expiringDate", token["expiringDate"]);
    this.http.get<LoggedUser>("/api/auth/getLoggedUserInfo").pipe(tap(res => {
      this.localStorage.setItem("user-email",res.email);
      this.localStorage.setItem("user-first-name",res.firstName);
      this.localStorage.setItem("user-last-name",res.lastName);
      this.localStorage.setItem("user-picture",res.picture);
      this.localStorage.setItem("user-role",this.hashService.hashString(res.role));
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
    this.localStorage.removeItem("jwt-expiringDate");
    this.localStorage.removeItem("user-picture");
    this.logged$.next(this.isLoggedIn());
    this.route.navigate(["/"]);
  }

  public refreshToken():void{
    const dateExpiring = new Date(this.localStorage.getItem("jwt-expiringDate"));
    const dateNow = new Date();
    if((dateNow.getTime()+360000)>dateExpiring.getTime()){
      this.http.get("/api/auth/refreshToken").subscribe((response) =>{
        this.localStorage.setItem("jwt", response["accessToken"])
        this.localStorage.setItem("jwt-expiringDate", response["expiringDate"]);
      })
    }
  }

  public resetPassword(body){
    return this.http.put("/api/auth/passwordReset",body);
  }

  public changePassword(body){
    return this.http.put("/api/auth/passwordChange",body);
  }

  public requestPasswordReset(email:string){
    return this.http.get("/api/auth/passwordResetToken?email="+email);
  }

  public getLoggedUserRole():string{
    return this.localStorage.getItem("user-role");
  }

  private isLoggedIn():boolean{
    if(this.localStorage.getItem("jwt")=== null){
      return false;
    }else{
      const dateExpiring = new Date(this.localStorage.getItem("jwt-expiringDate"));
      const dateNow = new Date();
      return dateExpiring > dateNow;
    }
  }

  private getLoggedUserInfo():LoggedUser{
    return {email: this.localStorage.getItem("user-email"), firstName: this.localStorage.getItem("user-first-name"), lastName: this.localStorage.getItem("user-last-name"), picture: this.localStorage.getItem("user-picture")} as LoggedUser;
  }

  public getLoggedUserIdentityId(){
    return this.http.get("/api/auth/getLoggedUserIdentityId");
  }


  public async handleError(error : HttpErrorResponse){
    this.loading$.next(true);
    await new Promise(r => setTimeout(r, 1000));
    this.loading$.next(false);
  }

  private async reportSuccessfulRegistration(){
    this.loading$.next(true);
    await new Promise(r => setTimeout(r, 1000));
    this.loading$.next(false);
    this.route.navigate(['/'])
  }

  public getLoggedClientBalance(){
    return this.http.get("/api/client/loggedClientBalance");
  }

}
