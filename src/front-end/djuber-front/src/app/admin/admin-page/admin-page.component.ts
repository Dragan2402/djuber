import {MediaMatcher} from '@angular/cdk/layout';
import {ChangeDetectorRef, Component,OnInit } from '@angular/core';

@Component({
  selector: 'djuber-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements  OnInit {

  page:number=1;
  directionToToggle:string='>>';


  
  ngOnInit(): void {
  }

  constructor() {

  }

  toggleNestoPage(){
    this.page=2;
  }

  toggleProfilePage(){
    this.page=1;
  }

  toggleChange(){
    if(this.directionToToggle===">>"){
      this.directionToToggle= "<<";
    }else{
      this.directionToToggle=">>";
    }
  }


}


