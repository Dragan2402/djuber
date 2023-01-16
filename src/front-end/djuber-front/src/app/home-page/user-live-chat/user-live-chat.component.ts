import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { localStorageToken } from 'src/app/utility/localstorage.token';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Message } from 'src/app/_common/model/message';
import {AuthenticationService} from 'src/app/authentication/authentication.service'

@Component({
  selector: 'djuber-user-live-chat',
  templateUrl: './user-live-chat.component.html',
  styleUrls: ['./user-live-chat.component.css']
})
export class UserLiveChatComponent implements OnInit {

  messageToSend:string = '';
  selectedChatMessages:Message[];

  url = 'http://localhost:8080'

  socket?: WebSocket;
  stompClient?: Stomp.Client;

  identityId:number;

  constructor(@Inject(localStorageToken) private localStorage : Storage, private http:HttpClient,private authenticationService:AuthenticationService) {
    this.identityId = 0;
  }

  ngOnInit(): void {
    this.authenticationService.getLoggedUserIdentityId().subscribe({
      next:(v) =>{
        this.identityId=v["id"]
        this.connectToChat(v["id"]);
      }
    })
  }

  connectToChat(id:number) {
    this.loadChatByIdentityId(id);
    this.socket = new SockJS(this.url + '/chat',{
        transports: ['xhr-streaming'],
        headers: {'Authorization': 'Bearer '+ this.localStorage.getItem("jwt")}
    });
    this.stompClient = Stomp.over(this.socket);

    this.stompClient.connect({}, (frame) => {
        //func = what to do when connection is established
        this.stompClient!.subscribe(
          '/topic/messages/' + id,
          () => {
            //func = what to do when client receives data (messages)
            this.loadChatByIdentityId(id);
          }
        );
      });

  }

  sendMessage(){
    if(this.messageToSend !== '' && this.identityId > 0){
      this.stompClient!.send(
        '/app/chat/' +this.identityId,
        {},
        JSON.stringify({
          senderFirstName: this.localStorage.getItem("user-first-name"),
          senderLastName: this.localStorage.getItem("user-last-name"),
          content: this.messageToSend,
          fromAdmin:false
        })
      );
      this.messageToSend='';
    }
    console.log(this.messageToSend, this.identityId);
  }

  loadChatByIdentityId(id:number){
    this.http.get<Message[]>('api/getMessagesByIdentityId?identityId='+id).subscribe({
      next: (response) =>{
        this.selectedChatMessages = response;
        this.selectedChatMessages.sort((a,b) => (a.time > b.time)? 1 : -1);
      }
    });
  }
}
