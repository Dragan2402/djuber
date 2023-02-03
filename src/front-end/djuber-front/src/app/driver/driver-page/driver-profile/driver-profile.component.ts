import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
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

  selectedCarTypeBackUp:string;

  selectedCityBackUp:string;

  extraLuggageBackUp: boolean;
  petsBackUp: boolean;
  luggageTransportBackUp: boolean;
  knowingEnglishBackUp:boolean;

  editing:boolean=false;

  controlGroup = this._formBuilder.group({
    firstName : ['', [Validators.required]],
    lastName : ['', [Validators.required]],
    licensePlate: ['', [Validators.required]],
    phoneNumber : ['', [Validators.required,Validators.pattern(/^[+]?[0-9]{8,13}$/)]],
  });

  profilePictureString:string='';

  constructor(private _formBuilder: FormBuilder, private driverService:DriverService, private _snackBar: MatSnackBar) {
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
    this.generateBackUp();
    this.editing=true;
    this.controlGroup.enable();
  }

  private generateBackUp(){
    this.backUpDriver = structuredClone(this.loggedDriver);
    this.selectedCarTypeBackUp = this.selectedCarType;
    this.selectedCityBackUp = this.selectedCity;
    this.extraLuggageBackUp = this.extraLuggage;
    this.petsBackUp = this.pets;
    this.luggageTransportBackUp= this.luggageTransport;
    this.knowingEnglishBackUp = this.knowingEnglish;
  }

  private backUp(){
    this.loggedDriver = this.backUpDriver;
    this.selectedCarType = this.selectedCarTypeBackUp;
    this.selectedCity = this.selectedCityBackUp;
    this.extraLuggage = this.extraLuggageBackUp;
    this.pets = this.petsBackUp;
    this.luggageTransport= this.luggageTransportBackUp;
    this.knowingEnglish = this.knowingEnglishBackUp;
  }

  isEditing(){
    return this.editing;
  }

  cancelEditing(){
    this.backUp();
    this.editing=false;
    this.controlGroup.disable();
  }



  saveEditing(){
    const services = this.getAdditionalServices();
    if(this.controlGroup.status ==="VALID"){
      const driverRequest = {firstName:this.loggedDriver.firstName, lastName:this.loggedDriver.lastName,phoneNumber:this.loggedDriver.phoneNumber,
        city:this.loggedDriver.city, licensePlate:this.loggedDriver.licensePlate,carType:this.selectedCarType, additionalServices:services} as UpdateDriverRequest;
      this.driverService.submitDriverUpdateRequest(driverRequest).subscribe({
        error: (err) =>{
          console.log(err);
          this._snackBar.openFromComponent(SnackbarComponent, {data:"You have already submitted request."});
        },
        complete:()=>{
          this._snackBar.openFromComponent(SnackbarComponent, {data:"Request submitted."});
        }
      });
      this.backUp();
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
