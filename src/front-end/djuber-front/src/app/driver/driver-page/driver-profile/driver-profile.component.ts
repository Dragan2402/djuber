import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Driver } from '../../driver';
import { DriverService } from '../../driver.service';
import { UpdateDriverRequest } from '../../updateDriverRequest';

@Component({
  selector: 'djuber-driver-profile',
  templateUrl: './driver-profile.component.html',
  styleUrls: ['./driver-profile.component.css']
})
export class DriverProfileComponent implements OnInit {

  extraLuggage: boolean;
  pets: boolean;
  luggageTransport: boolean;
  knowingEnglish:boolean;

  selectedCity : string = 'Novi Sad';

  cities:string[] = ['Novi Sad','Beograd']

  selectedCarType : string = "Sedan";

  carTypes:string[]= ["Sedan","Station wagon","Van","Transporter"]

  loggedDriver:Driver = {} as Driver;

  backUpDriver:Driver;

  editing:boolean=false;

  controlGroup = this._formBuilder.group({
    firstName : ['', [Validators.required]],
    lastName : ['', [Validators.required]],
    licensePlate: ['', [Validators.required]],
    phoneNumber : ['', [Validators.required,Validators.pattern(/^[+]?[0-9]{8,13}$/)]],
  });

  profilePictureString:string='';

  constructor(private _formBuilder: FormBuilder, private driverService:DriverService) {
   }

  ngOnInit(): void {
    this.controlGroup.disable();
    this.driverService.getLoggedDriver().subscribe(driver=>{
      this.loggedDriver = driver;
      this.selectedCity = driver.city;
      this.selectedCarType = driver.carType.toLowerCase().charAt(0).toUpperCase() + driver.carType.slice(1).toLowerCase();
      this.selectedCarType = this.selectedCarType.replace("_"," ");
      if(driver.additionalService.includes("pets")){
        this.pets = true;
      };
      if(driver.additionalService.includes("knowingEnglish")){
        this.knowingEnglish = true;
      };

      if(driver.additionalService.includes("luggageTransport")){
        this.luggageTransport = true;
      };
      if(driver.additionalService.includes("extraLuggage")){
        this.extraLuggage = true;
      };


    });
    this.driverService.getLoggedDriverPicture().subscribe(picture =>{
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
      this.driverService.updateLoggedDriverPicture(base64String).subscribe();
      localStorage.setItem("user-picture",base64String.split(',')[1]);
    };

  }


  toggleEditing(){
    this.backUpDriver = structuredClone(this.loggedDriver);
    this.editing=true;
    this.controlGroup.enable();
  }

  isEditing(){
    return this.editing;
  }

  cancelEditing(){
    this.loggedDriver = this.backUpDriver;
    this.editing=false;
    this.controlGroup.disable();
  }

  saveEditing(){
    const services = this.getAdditionalServices();
    if(this.controlGroup.status ==="VALID"){
      const driverRequest = {firstName:this.loggedDriver.firstName, lastName:this.loggedDriver.lastName,phoneNumber:this.loggedDriver.phoneNumber,
        city:this.loggedDriver.city, licensePlate:this.loggedDriver.licensePlate,carType:this.selectedCarType, additionalServices:services} as UpdateDriverRequest;
      this.driverService.updateLoggedDriver(driverRequest).subscribe();
      localStorage.setItem("user-last-name",this.loggedDriver.lastName);
      localStorage.setItem("user-first-name",this.loggedDriver.firstName);
      this.editing = false;
      this.controlGroup.disable();
    }
  }

  private  getAdditionalServices():string[]{
    const services = new Array();
    if(this.pets){
      services.push("pets");
    }
    if(this.extraLuggage){
      services.push("extraLuggage");
    }
    if(this.luggageTransport){
      services.push("luggageTransport");
    }
    if(this.knowingEnglish){
      services.push("knowingEnglish");
    }
    return services;

  }

}
