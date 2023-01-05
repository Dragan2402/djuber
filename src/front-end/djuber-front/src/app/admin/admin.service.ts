import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Admin } from './admin';
import { AdminModule } from './admin.module';
import { UpdateAdminRequest } from './update.admin.request';

@Injectable()
export class AdminService {

  constructor(private http:HttpClient) { }

  public getLoggedAdmin(){
    return this.http.get<Admin>("/api/admin/loggedAdmin");
  }

  public getLoggedAdminPicture(){
    return this.http.get<string>("/api/admin/loggedAdminPicture");
  }

  public updateLoggedAdminPicture(image:string){
    return this.http.put("/api/admin/updateLoggedAdminPicture",{"image":image});
  }

  public updateLoggedAdmin(request:UpdateAdminRequest){
    return this.http.put("/api/admin/updateLoggedAdmin",request);
  }
}
