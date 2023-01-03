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

    if(request.url.includes("refreshToken") && request.method == 'GET'){
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
      return next.handle(request);
    }
    if(request.url.startsWith("/api/auth") && request.method == 'GET'){
      this.authenticationService.refreshToken();
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.localStorage.getItem("jwt")}`
        }
      });
      return next.handle(request);
    }
    return next.handle(request);
  }
}
