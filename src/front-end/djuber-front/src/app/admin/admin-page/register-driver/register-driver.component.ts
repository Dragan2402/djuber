import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import CustomValidators from 'src/app/utility/customValidators';
import { AdminService } from '../../admin.service';
import { DriverRegistrationRequest } from '../../driver.registration.request';

@Component({
  selector: 'djuber-register-driver',
  templateUrl: './register-driver.component.html',
  styleUrls: ['./register-driver.component.css'],
})
export class RegisterDriverComponent implements OnInit {
  selectedCity: string = 'Novi Sad';

  cities: string[] = ['Novi Sad', 'Beograd'];

  selectedCarType: string = 'Sedan';

  extraLuggage: boolean = false;
  pets: boolean = false;
  luggageTransport: boolean = false;
  knowingEnglish: boolean = false;

  carTypes: string[] = ['Sedan', 'Station wagon', 'Van', 'Transporter'];

  profilePicture;

  firstFormGroup = this._formBuilder.group(
    {
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', [Validators.required]],
    },
    {
      validators: [
        CustomValidators.matchPasswords('password', 'confirmPassword'),
      ],
    }
  );
  secondFormGroup = this._formBuilder.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    phoneNumber: [
      '',
      [Validators.required, Validators.pattern(/^[+]?[0-9]{8,13}$/)],
    ],
  });

  thirdFormGroup = this._formBuilder.group({
    licensePlate: ['', [Validators.required]],
  });

  constructor(
    private _formBuilder: FormBuilder,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {}

  registerDriver() {
    if (
      this.firstFormGroup.status === 'VALID' &&
      this.secondFormGroup.status === 'VALID' &&
      this.thirdFormGroup.status === 'VALID'
    ) {
      const additionalServices = this.getAdditionalServices();

      const request = {
        email: this.firstFormGroup.controls['email'].value,
        password: this.firstFormGroup.controls['password'].value,
        confirmPassword: this.firstFormGroup.controls['confirmPassword'].value,
        firstName: this.secondFormGroup.controls.firstName.value,
        lastName: this.secondFormGroup.controls.lastName.value,
        city: this.selectedCity,
        phoneNumber: this.secondFormGroup.controls.phoneNumber.value,
        licensePlate: this.thirdFormGroup.controls.licensePlate.value,
        carType: this.selectedCarType,
        additionalServices: additionalServices,
      } as DriverRegistrationRequest;

      if (this.profilePicture !== undefined) {
        request.picture = this.profilePicture;
      }
      console.log(request);
      this.adminService.registerDriver(request).subscribe((res) => {
        console.log(res);
      });
    }
  }

  private getAdditionalServices(): string[] {
    const services = new Array();
    if (this.pets) {
      services.push('pets');
    }
    if (this.extraLuggage) {
      services.push('extraLuggage');
    }
    if (this.luggageTransport) {
      services.push('luggageTransport');
    }
    if (this.knowingEnglish) {
      services.push('knowingEnglish');
    }
    return services;
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

  clearPicture() {
    this.profilePicture = undefined;
  }

  getErrorMessage(num: number): string {
    if (num === 1) {
      if (this.firstFormGroup.controls['email'].status === 'INVALID') {
        return 'Email is required.';
      }
      if (this.firstFormGroup.controls['password'].status === 'INVALID') {
        return 'Password is required.';
      }
      if (
        this.firstFormGroup.controls['confirmPassword'].status === 'INVALID'
      ) {
        if (
          this.firstFormGroup.controls['confirmPassword'].hasError('required')
        ) {
          return 'Confirm password.';
        } else {
          return 'Passwords must match.';
        }
      }
    } else {
      if (this.secondFormGroup.controls.firstName.status === 'INVALID') {
        return 'First name is required.';
      }
      if (this.secondFormGroup.controls.lastName.status === 'INVALID') {
        return 'Last name is required.';
      }
      if (this.secondFormGroup.controls.phoneNumber.status === 'INVALID') {
        return 'Invalid phone number.';
      }
    }
    return '';
  }
}
