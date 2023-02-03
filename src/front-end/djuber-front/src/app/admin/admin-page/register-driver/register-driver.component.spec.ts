import {
  HttpClient,
  HttpClientModule,
  HttpHandler,
} from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of, throwError } from 'rxjs';
import { AdminService } from '../../admin.service';
import { DriverRegistrationRequest } from '../../driver.registration.request';
import { RegisterDriverComponent } from './register-driver.component';

describe('RegisterDriverComponent', () => {
  let fixture: ComponentFixture<RegisterDriverComponent>;
  let component: RegisterDriverComponent;
  let adminServiceSpy: jasmine.SpyObj<AdminService>;

  const exampleRequest = {
    email: 'abc@mail.com',
    password: 'abc123',
    confirmPassword: 'abc123',
    firstName: 'Abc',
    lastName: 'Def',
    phoneNumber: '0601234567',
    city: 'Novi Sad',
    licensePlate: 'NS 000-AA',
    carType: 'Sedan',
    additionalServices: ['pets'],
  } as DriverRegistrationRequest;

  beforeEach(() => {
    adminServiceSpy = jasmine.createSpyObj<AdminService>('AdminService', [
      'registerDriver',
    ]);
    // adminServiceSpy.registerDriver.and.returnValue(of({ id: 1}));
    adminServiceSpy.registerDriver.and.callFake((request) => {
      if (request == exampleRequest) {
        return of({ id: 1 });
      } else {
        return of(throwError(() => new Error('400 Bad Request')));
      }
    });

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule],
      declarations: [RegisterDriverComponent],
      providers: [
        FormBuilder,
        HttpClient,
        HttpHandler,
        { provide: AdminService, useValue: adminServiceSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterDriverComponent);
    component = fixture.componentInstance;
  });

  it('firstFormGroup should be valid', () => {
    component.firstFormGroup.controls.email.setValue('abc@mail.com');
    component.firstFormGroup.controls.password.setValue('abc123');
    component.firstFormGroup.controls.confirmPassword.setValue('abc123');

    component.secondFormGroup.controls.firstName.setValue('Abc');
    component.secondFormGroup.controls.lastName.setValue('Def');
    component.secondFormGroup.controls.phoneNumber.setValue('0601234567');

    component.thirdFormGroup.controls.licensePlate.setValue('NS 000-AA');

    expect(component.firstFormGroup.valid).toBeTruthy();
    expect(component.secondFormGroup.valid).toBeTruthy();
    expect(component.thirdFormGroup.valid).toBeTruthy();
  });

  it('should create registration request and call service method', () => {
    component.firstFormGroup.controls.email.setValue('abc@mail.com');
    component.firstFormGroup.controls.password.setValue('abc123');
    component.firstFormGroup.controls.confirmPassword.setValue('abc123');

    component.secondFormGroup.controls.firstName.setValue('Abc');
    component.secondFormGroup.controls.lastName.setValue('Def');
    component.secondFormGroup.controls.phoneNumber.setValue('0601234567');
    component.selectedCity = 'Novi Sad';

    component.thirdFormGroup.controls.licensePlate.setValue('NS 000-AA');
    component.selectedCarType = 'Sedan';
    component.pets = true;
    component.knowingEnglish = false;
    component.extraLuggage = false;
    component.luggageTransport = false;

    component.registerDriver();
    expect(adminServiceSpy.registerDriver).toHaveBeenCalledWith(exampleRequest);
    expect(adminServiceSpy.registerDriver).toHaveBeenCalledTimes(1);
    console.log(adminServiceSpy.registerDriver(exampleRequest));
  });

  it('should fail to create a request and not call service method', () => {
    component.firstFormGroup.controls.email.setValue('abc');
    component.firstFormGroup.controls.password.setValue('abc123');
    component.firstFormGroup.controls.confirmPassword.setValue('abc123');

    component.secondFormGroup.controls.firstName.setValue('Abc');
    component.secondFormGroup.controls.lastName.setValue('Def');
    component.secondFormGroup.controls.phoneNumber.setValue('0601234567');
    component.selectedCity = 'Novi Sad';

    component.thirdFormGroup.controls.licensePlate.setValue('NS 000-AA');
    component.selectedCarType = 'Sedan';
    component.pets = true;
    component.knowingEnglish = false;
    component.extraLuggage = false;
    component.luggageTransport = false;

    component.registerDriver();
    expect(adminServiceSpy.registerDriver).toHaveBeenCalledTimes(0);
  });
});
