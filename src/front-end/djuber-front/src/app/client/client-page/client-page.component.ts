import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'djuber-client-page',
  templateUrl: './client-page.component.html',
  styleUrls: ['./client-page.component.css']
})
export class ClientPageComponent implements OnInit {

  page:number=1;
  directionToToggle:string='>>';



  ngOnInit(): void {
    this.router.navigateByUrl("/client/(co:profile)");
  }

  constructor(private router: Router) {

  }

  changePassword(){
    this.router.navigate(["/authentication/changePassword"]);
  }

  toggleProfilePage(){
    this.router.navigateByUrl("/client/(co:profile)");
  }

  toggleChange(){
    if(this.directionToToggle===">>"){
      this.directionToToggle= "<<";
    }else{
      this.directionToToggle=">>";
    }
  }

}
