import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  logged:boolean = false;

  constructor(private http:HttpClient) { }

  public login(email:string, password:string){
    this.logged =true;
    const body = {
      email: email,
      password: password
    }
    return this.http.post("/api/auth/login",body);
  }
}
