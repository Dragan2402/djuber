import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AdminService } from './admin.service';
import { DriverRegistrationRequest } from './driver.registration.request';

describe('AdminService', () => {
  let service: AdminService;
  let httpTestingController: HttpTestingController;

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
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminService],
    });
    service = TestBed.inject(AdminService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should make a post request and return object', () => {
    service.registerDriver(exampleRequest).subscribe((response) => {
      expect(response).toEqual({ id: 1 });
    });

    const postRequest = httpTestingController.expectOne(
      '/api/admin/registerDriver'
    );
    expect(postRequest.request.method).toEqual('POST');
    expect(postRequest.request.body).toEqual(exampleRequest);
    postRequest.flush({ id: 1 });
  });

  it('should make a post request and throw error', () => {
    service.registerDriver(exampleRequest).subscribe((response) => {
      expect(response).toThrowError();
    });

    const postRequest = httpTestingController.expectOne(
      '/api/admin/registerDriver'
    );
    expect(postRequest.request.method).toEqual('POST');
    expect(postRequest.request.body).toEqual(exampleRequest);
    expect(service.registerDriver).toThrowError();
  });
});
