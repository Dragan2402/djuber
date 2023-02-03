import { async, ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthenticationService } from '../authentication.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SocialAuthServiceConfigMock } from '../soical-auth.service.mock';
import {SocialAuthService } from '@abacritt/angularx-social-login';
import { HashService } from '../../utility/hash-service.service';
import { Observable, of } from 'rxjs';
import { AuthenticationMockService } from '../authentication.service.mock';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        HttpClientTestingModule,
        RouterTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [ LoginComponent ],
      providers: [
        HashService,
        {
          provide: SocialAuthService,
          useValue: SocialAuthServiceConfigMock
        },
        AuthenticationService
     ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call login function on login button click', () => {
    const authService = fixture.debugElement.injector.get(AuthenticationService);
    const spy = spyOn(authService, 'login').and.returnValue(of(null));
    component = fixture.componentInstance;
    component.email.setValue('test@test.com');
    component.password.setValue('password');
    fixture.detectChanges();
    const loginButton = fixture.debugElement.nativeElement.querySelector('.continue-action-button');
    loginButton.click();
    expect(spy).toHaveBeenCalledWith('test@test.com', 'password');
  });

  it('should call facebookSignIn on facebook button click', () => {
    const authService = fixture.debugElement.injector.get(AuthenticationService);
    const spy = spyOn(authService, 'facebookSignIn').and.callThrough();
    component = fixture.componentInstance;
    fixture.detectChanges();
    const facebookIcon = fixture.debugElement.nativeElement.querySelector('.facebook-icon');
    facebookIcon.click();
    expect(spy).toHaveBeenCalled();
  });

  it('should display the correct error messages when password is invalid', waitForAsync(() => {
    component.email = new FormControl('', [Validators.required, Validators.email]);
    component.password = new FormControl('', [Validators.required]);
    fixture.detectChanges();
    const email = fixture.debugElement.nativeElement.querySelector('#emailLoginInput');
    const password = fixture.debugElement.nativeElement.querySelector('#passwordLoginInput');
    expect(email.validationMessage).toEqual("Please fill out this field.");
    expect(password.validationMessage).toEqual("Please fill out this field.");
  }));


});
