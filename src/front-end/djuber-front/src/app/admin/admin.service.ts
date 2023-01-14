import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Admin } from './admin';
import { AdminModule } from './admin.module';
import { DriverRegistrationRequest } from './driver.registration.request';
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

  public getDriversPage(index:number, size:number, filter:string){
    return this.http.get(`/api/driver?page=${index.toString()}&size=${size.toString()}&filter=${filter}`);
  }

  public filterSearch(pageSize:number, filter:string){
    return this.http.get(`/api/driver?page=0&size=${pageSize.toString()}&filter=${filter}`);
  }

  public getClientsPage(index:number, size:number, filter:string){
    return this.http.get(`/api/client?page=${index.toString()}&size=${size.toString()}&filter=${filter}`);
  }

  public filterSearchClients(pageSize:number, filter:string){
    return this.http.get(`/api/client?page=0&size=${pageSize.toString()}&filter=${filter}`);
  }

  public registerDriver(request:DriverRegistrationRequest){
    return this.http.post("/api/admin/registerDriver",request);
  }

  public unblockDriver(id:number){
    return this.http.put("/api/driver/unblockDriver",{"id":id});
  }

  public blockDriver(id:number){
    return this.http.put("/api/driver/blockDriver",{"id":id});
  }

  public unblockClient(id:number){
    return this.http.put("/api/client/unblockClient",{"id":id});
  }

  public blockClient(id:number){
    return this.http.put("/api/client/blockClient",{"id":id});
  }

  public updateClientNote(id:number, note:string){
    return this.http.put("/api/client/updateClientNote", {id:id, note:note});
  }

  public getClientNote(id:number){
    return this.http.get("/api/client/getClientNote?id="+id);
  }

  public updateDriverNote(id:number, note:string){
    return this.http.put("/api/driver/updateDriverNote", {id:id, note:note});
  }

  public getDriverNote(id:number){
    return this.http.get("/api/driver/getDriverNote?id="+id);
  }
}
