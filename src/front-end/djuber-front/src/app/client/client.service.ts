import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Client } from './client';
import { UpdateClientRequest } from './updateClientRequest';

@Injectable()
export class ClientService {


  constructor(private http:HttpClient) { }

  public getLoggedClient(){
    return this.http.get<Client>("/api/client/loggedClient");
  }

  public getLoggedClientPicture(){
    return this.http.get<string>("/api/client/loggedClientPicture");
  }

  public updateLoggedClientPicture(image:string){
    return this.http.put("/api/client/updateLoggedClientPicture",{"image":image});
  }

  public updateLoggedClient(request:UpdateClientRequest){
    return this.http.put("/api/client/updateLoggedClient",request);
  }

  public getRidesPage(index:number, size:number, filter:string){
    return this.http.get(`/api/ride?page=${index.toString()}&size=${size.toString()}&filter=${filter}`);
  }

  public addLoggedClientFunds(amount:number){
    return this.http.put("/api/client/addLoggedClientFunds",{amount:amount});
  }

}
