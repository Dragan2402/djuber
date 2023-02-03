import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit,AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { catchError, Observable, of } from 'rxjs';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'djuber-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrls: ['./verify-account.component.css']
})
export class VerifyAccountComponent implements OnInit, AfterViewInit {

  token:string;
  verified:boolean;

  constructor(private route: ActivatedRoute, private authenticationSerivce: AuthenticationService) {
    this.verified = false;
  }

  ngAfterViewInit(): void {
    this.authenticationSerivce.verifyClientAccount(this.token).subscribe(res =>{
      this.verified =true
    }),catchError((error :HttpErrorResponse): Observable<any> => {
      this.verified = false;
      return of(null);
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get("token");
  }

}
