import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'djuber-client-page',
  templateUrl: './client-page.component.html',
  styleUrls: ['./client-page.component.css']
})
export class ClientPageComponent implements OnInit {

  directionToToggle:string='>>';



  ngOnInit(): void {
    this.router.navigateByUrl('client');
    this.router.navigate(['client',{outlets: { co:['profile']} }],{ skipLocationChange: true });
  }

  constructor(private router: Router) {

  }

  changePassword(){
    this.router.navigate(["/authentication/changePassword"]);
  }

  toggleProfilePage(){
    this.router.navigateByUrl('client');
    this.router.navigate(['client',{outlets: { co:['profile']} }],{ skipLocationChange: true });
  }

  toggleChange(){
    if(this.directionToToggle===">>"){
      this.directionToToggle= "<<";
    }else{
      this.directionToToggle=">>";
    }
  }

}
