import {MediaMatcher} from '@angular/cdk/layout';
import {ChangeDetectorRef, Component,OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'djuber-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements  OnInit {

  page:number=1;
  directionToToggle:string='>>';



  ngOnInit(): void {
    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['profile']} }],{ skipLocationChange: true });
  }

  constructor(private router : Router) {

  }

  toggleRegisterNewDriverPage(){
    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['registerDriver']} }],{ skipLocationChange: true });
  }

  toggleDriversPreview(){
    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['previewDrivers']} }],{ skipLocationChange: true });
  }

  toggleClientsPreview(){
    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['previewClients']} }],{ skipLocationChange: true });
  }

  changePassword(){
    this.router.navigate(["/authentication/changePassword"]);
  }

  toggleProfilePage(){
    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['profile']} }],{ skipLocationChange: true });
  }

  toggleChange(){
    if(this.directionToToggle===">>"){
      this.directionToToggle= "<<";
    }else{
      this.directionToToggle=">>";
    }
  }


}


