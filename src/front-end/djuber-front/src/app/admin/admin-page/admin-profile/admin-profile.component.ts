import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'djuber-admin-profile',
  templateUrl: './admin-profile.component.html',
  styleUrls: ['./admin-profile.component.css']
})
export class AdminProfileComponent implements OnInit {
  selectedCity : string = 'Novi Sad';

  cities:string[] = ['Novi Sad','Beograd']

  controlGroup = this._formBuilder.group({
    firstName : ['', [Validators.required]],
    lastName : ['', [Validators.required]],
    phoneNumber : ['', [Validators.required,Validators.pattern(/^[+]?[0-9]{8,13}$/)]],
  });

  profilePicture;

  constructor(private _formBuilder: FormBuilder) { }

  ngOnInit(): void {
  }

  uploadFile(files: FileList) {
    const file = files.item(0);
    const reader = new FileReader();

    reader.readAsDataURL(file);
    reader.onload = () => {
      this.profilePicture = reader.result;
      const base64String = reader.result as string;
    };

  }

  clearPicture(){
    this.profilePicture = undefined;
    this.controlGroup.controls.firstName.disable();
  }

}
