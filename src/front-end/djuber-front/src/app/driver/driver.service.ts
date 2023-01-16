import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Driver } from './driver';
import { UpdateDriverRequest } from './updateDriverRequest';

@Injectable()
export class DriverService {

  constructor(private http:HttpClient) { }

  public getLoggedDriver(){
    return this.http.get<Driver>("/api/driver/loggedDriver");
  }

  public getLoggedDriverPicture(){
    return this.http.get<string>("/api/driver/loggedDriverPicture");
  }

  public updateLoggedDriverPicture(image:string){
    return this.http.put("/api/driver/updateLoggedDriverPicture",{"image":image});
  }

  public submitDriverUpdateRequest(request:UpdateDriverRequest){
    return this.http.put("/api/driver/submitDriverUpdateRequest",request);
  }
}
