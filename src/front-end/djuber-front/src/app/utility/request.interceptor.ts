import { Inject, Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {localStorageToken} from "./localstorage.token";
import { AuthenticationService } from '../authentication/authentication.service';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {
  constructor(@Inject(localStorageToken) private localStorage : Storage, private authenticationService: AuthenticationService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    if(request.url.includes("api/admin")){
      return this.handleAdminRequests(request, next);
    }

    if(request.url.includes("api/client")){
      return this.handleClientRequests(request, next);
    }

    if(request.url.includes("api/driver")){
      return this.handleDriverRequests(request, next);
    }

    if(request.url.includes("api/auth")){
      return this.handleAuthenticationRequests(request, next);
    }
    if(request.url.includes("googleusercontent")){
      return next.handle(request);
    }
    if(request.url.includes("getMessages")){
      return this.handleMessagesRequests(request, next);
    }
    return next.handle(request);
  }

  private handleAdminRequests(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    this.authenticationService.refreshToken();
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
    return next.handle(request);

  }

  private handleClientRequests(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    this.authenticationService.refreshToken();
    request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
    return next.handle(request);
  }

  private handleMessagesRequests(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    this.authenticationService.refreshToken();
    request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
    return next.handle(request);
  }


  private handleDriverRequests(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if(request.url.includes("availableDrivers") && request.method==="GET"){
      return next.handle(request);
    }

    this.authenticationService.refreshToken();
    request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
    return next.handle(request);
  }

  private handleAuthenticationRequests(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if(request.url.includes("passwordResetToken")){
      return next.handle(request);
    }
    if(request.url.includes("refreshToken") && request.method == 'GET'){
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
      return next.handle(request);
    }
    if((request.url.startsWith("/api/auth") && request.method == 'GET') || (request.url.includes("passwordChange"))){
      this.authenticationService.refreshToken();
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
    }
    return next.handle(request);
  }

}
