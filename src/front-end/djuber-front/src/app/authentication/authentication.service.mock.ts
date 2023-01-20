import { Injectable } from "@angular/core";
import { of } from "rxjs";


@Injectable()
export class AuthenticationMockService{
  constructor(){}

  public login(email: string, password: string) {
    return of(null);
  }

}
