import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../authentication.service';

@Component({
  selector: 'djuber-password-reset-request',
  templateUrl: './password-reset-request.component.html',
  styleUrls: ['./password-reset-request.component.css']
})
export class PasswordResetRequestComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email]);

  constructor(private authenticationService:AuthenticationService,private route: Router) { }

  ngOnInit(): void {
  }


  getEmailErrorMessage() {
    return this.email.hasError('required') ? '' :
        this.email.hasError('email') ? 'Invalid email' :
            '';
  }

  request(){
    if(this.email.valid){
      this.authenticationService.requestPasswordReset(this.email.value).subscribe(res =>{
        this.route.navigate(['/']);
      })
    }
  }
}
