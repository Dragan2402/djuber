import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'djuber-driver-page',
  templateUrl: './driver-page.component.html',
  styleUrls: ['./driver-page.component.css']
})
export class DriverPageComponent implements OnInit {

  directionToToggle:string='>>';



  ngOnInit(): void {
    this.router.navigateByUrl('driver');
    this.router.navigate(['driver',{outlets: { co:['profile']} }],{ skipLocationChange: true });
  }

  constructor(private router: Router) {

  }

  changePassword(){
    this.router.navigate(["/authentication/changePassword"]);
  }

  toggleProfilePage(){
    this.router.navigateByUrl('driver');
    this.router.navigate(['driver',{outlets: { co:['profile']} }],{ skipLocationChange: true });
  }

  rides(){
    this.router.navigateByUrl('driver')
    this.router.navigate(['driver', {outlets: { co:['rides']} }], {skipLocationChange: true})
  }

  toggleChange(){
    if(this.directionToToggle===">>"){
      this.directionToToggle= "<<";
    }else{
      this.directionToToggle=">>";
    }
  }

  reports(){
    this.router.navigateByUrl('driver')
    this.router.navigate(['driver', {outlets: { co:['reports']} }], {skipLocationChange: true})
  }
}
