import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/authentication/authentication.service';

@Component({
  selector: 'djuber-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent implements OnInit {
  loading! : boolean;
  constructor(private authenticationService : AuthenticationService) {
    this.authenticationService.loading$.subscribe((attr:boolean) =>{
      this.loading = attr;
    });
  }

  ngOnInit(): void {
  }

}
