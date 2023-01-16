import { Component,OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AdminService } from '../admin.service';


@Component({
  selector: 'djuber-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements  OnInit {

  page:number=1;
  directionToToggle:string='>>';
  numberOfActiveChats:number;

  url = 'http://localhost:8080'

  socket?: WebSocket;
  stompClient?: Stomp.Client;


  constructor(private router : Router, private adminService:AdminService) {
    this.numberOfActiveChats=0;
  }

  ngOnInit(): void {
    this.adminService.getNumberOfChats()
      .subscribe({
        next:(response)=>{
          this.numberOfActiveChats = response["count"];
          this.connectToChat();
        }
      });

    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['profile']} }],{ skipLocationChange: true });
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

  toggleCustomerSupport(){
    this.router.navigateByUrl('admin');
    this.router.navigate(['admin',{outlets: { ao:['customerSupport']} }],{ skipLocationChange: true });
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

  loadNumberOfChats(){
    this.adminService.getNumberOfChats()
      .subscribe({
        next:(response)=>{
          this.numberOfActiveChats = response["count"] as number;
          this.toggleChange();
          this.toggleChange();
        }
      });
  }

  connectToChat() {
    this.loadNumberOfChats();
    this.socket = new SockJS(this.url + '/chat',{
        transports: ['xhr-streaming']
    });
    this.stompClient = Stomp.over(this.socket);

    this.stompClient.connect({}, (frame) => {
        //func = what to do when connection is established
        this.stompClient!.subscribe(
          '/topic/messages/**' ,
          () => {
            //func = what to do when client receives data (messages)
            this.loadNumberOfChats();
          }
        );
      });

  }

}


