import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthenticationService } from './authentication.service';
import { SocialAuthService, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { HashService } from '../utility/hash-service.service';
import { Router } from '@angular/router';
import { localStorageToken } from '../utility/localstorage.token';
import { SocialAuthServiceConfigMock } from './soical-auth.service.mock';

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpMock: HttpTestingController;
  let socialAuthService: SocialAuthService;
  let hashService: HashService;
  let storage:Storage;
  let route: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [AuthenticationService, SocialAuthService, HashService,
        { provide: localStorageToken, useValue: localStorage },
        { provide: Router, useValue: route },
        {
          provide: SocialAuthService,
          useValue: SocialAuthServiceConfigMock
        }]
    });

    service = TestBed.inject(AuthenticationService);
    httpMock = TestBed.inject(HttpTestingController);
    socialAuthService = TestBed.inject(SocialAuthService);
    hashService = TestBed.inject(HashService);
    route = TestBed.inject(Router);
    storage = TestBed.inject(localStorageToken);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send a post request to login and navigate to home page', () => {
    let spy2 = spyOn(service,'setToken');
    let response = {accessToken: 'abcdefg', expiringDate: '2022-01-20'};
    service.login('test@example.com', 'password').subscribe(res => {
      expect(res).toEqual(response);
    });
    let req = httpMock.expectOne('/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(response);
    expect(spy2).toHaveBeenCalledWith(response);
  });

  it('should send a post request to login and call handleError method', () => {
    let spy2 = spyOn(service,'handleError');
    service.login('test@example.com', 'password').subscribe(res => {
      expect(res).toBe(null);
    });
    let req = httpMock.expectOne('/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(null, { status: 400, statusText: 'Bad Request' });
    expect(spy2).toHaveBeenCalled();
  });



  })
