import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Client } from '../../client';
import { ClientService } from '../../client.service';
import { UpdateClientRequest } from '../../updateClientRequest';

@Component({
  selector: 'djuber-client-profile',
  templateUrl: './client-profile.component.html',
  styleUrls: ['./client-profile.component.css']
})
export class ClientProfileComponent implements OnInit {

  selectedCity : string = 'Novi Sad';

  cities:string[] = ['Novi Sad','Beograd']

  loggedClient:Client = {} as Client;

  backUpClient:Client;

  editing:boolean=false;

  controlGroup = this._formBuilder.group({
    firstName : ['', [Validators.required]],
    lastName : ['', [Validators.required]],
    phoneNumber : ['', [Validators.required,Validators.pattern(/^[+]?[0-9]{8,13}$/)]],
  });

  profilePictureString:string='';

  constructor(private _formBuilder: FormBuilder, private clientService:ClientService) {
   }

  ngOnInit(): void {
    this.controlGroup.disable();

    this.clientService.getLoggedClient().subscribe(client=>{
      this.loggedClient = client;
      this.selectedCity = client.city;
    });
    this.clientService.getLoggedClientPicture().subscribe(picture =>{
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
      this.clientService.updateLoggedClientPicture(base64String).subscribe();
      localStorage.setItem("user-picture",base64String.split(',')[1]);
    };

  }


  toggleEditing(){
    this.backUpClient = structuredClone(this.loggedClient);
    this.editing=true;
    this.controlGroup.enable();
  }

  isEditing(){
    return this.editing;
  }

  cancelEditing(){
    this.loggedClient = this.backUpClient;
    this.editing=false;
    this.controlGroup.disable();
  }

  saveEditing(){
    if(this.controlGroup.status ==="VALID"){
      const clientRequest = {firstName:this.loggedClient.firstName, lastName:this.loggedClient.lastName,phoneNumber:this.loggedClient.phoneNumber,city:this.loggedClient.city} as UpdateClientRequest;
      this.clientService.updateLoggedClient(clientRequest).subscribe();
      localStorage.setItem("user-last-name",this.loggedClient.lastName);
      localStorage.setItem("user-first-name",this.loggedClient.firstName);
      this.editing = false;
      this.controlGroup.disable();
    }
  }

}
