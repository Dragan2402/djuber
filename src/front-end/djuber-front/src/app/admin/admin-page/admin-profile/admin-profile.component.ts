import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Admin } from '../../admin';
import { AdminService } from '../../admin.service';
import { UpdateAdminRequest } from '../../update.admin.request';

@Component({
  selector: 'djuber-admin-profile',
  templateUrl: './admin-profile.component.html',
  styleUrls: ['./admin-profile.component.css']
})
export class AdminProfileComponent implements OnInit {
  selectedCity : string = 'Novi Sad';

  cities:string[] = ['Novi Sad','Beograd']

  loggedAdmin:Admin = {} as Admin;

  backUpAdmin:Admin;

  editing:boolean=false;

  controlGroup = this._formBuilder.group({
    firstName : ['', [Validators.required]],
    lastName : ['', [Validators.required]],
    phoneNumber : ['', [Validators.required,Validators.pattern(/^[+]?[0-9]{8,13}$/)]],
  });

  profilePictureString:string='';

  constructor(private _formBuilder: FormBuilder, private adminService:AdminService) {
   }

  ngOnInit(): void {
    this.controlGroup.disable();

    this.adminService.getLoggedAdmin().subscribe(admin=>{
      this.loggedAdmin = admin;
      this.selectedCity = admin.city;
    });
    this.adminService.getLoggedAdminPicture().subscribe(picture =>{
      this.profilePictureString = 'data:image/png;base64,'+picture["image"];
    })
  }

  uploadFile(files: FileList) {
    const file = files.item(0);
    const reader = new FileReader();

    reader.readAsDataURL(file);
    reader.onload = () => {
      const base64String = reader.result as string;
      this.profilePictureString = base64String;
      this.adminService.updateLoggedAdminPicture(base64String).subscribe();
      localStorage.setItem("user-picture",base64String.split(',')[1]);
    };

  }


  toggleEditing(){
    this.backUpAdmin = structuredClone(this.loggedAdmin);
    this.editing=true;
    this.controlGroup.enable();
  }

  isEditing(){
    return this.editing;
  }

  cancelEditing(){
    this.loggedAdmin = this.backUpAdmin;
    this.editing=false;
    this.controlGroup.disable();
  }

  saveEditing(){
    if(this.controlGroup.status ==="VALID"){
      const adminRequest = {firstName:this.loggedAdmin.firstName, lastName:this.loggedAdmin.lastName,phoneNumber:this.loggedAdmin.phoneNumber,city:this.loggedAdmin.city} as UpdateAdminRequest;
      this.adminService.updateLoggedAdmin(adminRequest).subscribe();
      localStorage.setItem("user-last-name",this.loggedAdmin.lastName);
      localStorage.setItem("user-first-name",this.loggedAdmin.firstName);
      this.editing = false;
      this.controlGroup.disable();
    }
  }

}
