import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from '../authentication/authentication.service';
import { SnackbarComponent } from '../snackbar/snackbar.component';

@Component({
  selector: 'djuber-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  constructor(private authenticationService:AuthenticationService,private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

}
