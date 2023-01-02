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

@Injectable()
export class RequestInterceptor implements HttpInterceptor {
  constructor(@Inject(localStorageToken) private localStorage : Storage) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    if(request.url.startsWith("/api/auth") && request.method == 'GET'){
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
