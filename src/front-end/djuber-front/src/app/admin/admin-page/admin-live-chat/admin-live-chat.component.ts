import { Component, Inject, OnInit } from '@angular/core';
import { localStorageToken } from 'src/app/utility/localstorage.token';
import { Chat } from 'src/app/_common/model/chat';
import { AdminService } from '../../admin.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Message } from 'src/app/_common/model/message';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'djuber-admin-live-chat',
  templateUrl: './admin-live-chat.component.html',
  styleUrls: ['./admin-live-chat.component.css']
})
export class AdminLiveChatComponent implements OnInit {

  selectedChat:Chat;
  selectedChatMessages:Message[];

  url = 'http://localhost:8080'

  socket?: WebSocket;
  stompClient?: Stomp.Client;

  chats:Chat[];

  messageToSend:string = '';

  constructor(private adminService : AdminService, @Inject(localStorageToken) private localStorage : Storage, private http:HttpClient) {
    this.selectedChat = undefined;


  }

  ngOnInit(): void {
    this.adminService.getChats().subscribe({
      next: (chatsResponses) => {
        this.chats = chatsResponses;
      }
    })
  }

  selectChat(chat:Chat){
    this.selectedChat = chat;
    this.connectToChat(chat);
  }

  connectToChat(chat:Chat) {
    if(chat !==undefined){
      this.loadChat(chat);
      this.socket = new SockJS(this.url + '/chat',{
        transports: ['xhr-streaming'],
        headers: {'Authorization': 'Bearer '+ this.localStorage.getItem("jwt")}
    });
      this.stompClient = Stomp.over(this.socket);

      this.stompClient.connect({}, (frame) => {
        //func = what to do when connection is established
        this.stompClient!.subscribe(
          '/topic/messages/' + this.selectedChat.subjectIdentityId,
          () => {
            //func = what to do when client receives data (messages)
            this.loadChat(this.selectedChat);
          }
        );
      });
    }
  }

  sendMessage(){
    if(this.selectChat !==undefined && this.messageToSend !== ''){
      this.stompClient!.send(
        '/app/chat/' + this.selectedChat.subjectIdentityId,
        {},
        JSON.stringify({
          senderFirstName: this.localStorage.getItem("user-first-name"),
          senderLastName: this.localStorage.getItem("user-last-name"),
          content: this.messageToSend,
          fromAdmin:true
        })
      );
      this.messageToSend='';
    }
  }

  loadChat(chat:Chat){
    this.http.get<Message[]>('api/getMessages?chatId='+chat.id).subscribe({
      next: (response) =>{
        this.selectedChatMessages = response;
        this.selectedChatMessages.sort((a,b) => (a.time > b.time)? 1 : -1);
      }
    });
  }

  deleteSelectedChat(chat:Chat){
    this.adminService.deleteChat(chat.id).subscribe({
      complete: ()=>{
        this.selectedChat = undefined;
        this.adminService.getChats().subscribe({
          next: (chatsResponses) => {
            this.chats = chatsResponses;
          }
        })
      }
    });
  }
}
