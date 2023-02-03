import { Injectable } from "@angular/core";
import { BehaviorSubject, of } from "rxjs";

@Injectable()
export class SocialAuthServiceConfigMock{
  constructor(){}

  authState;

  signIn(provider: string) {
    return of(null);
  }

}
