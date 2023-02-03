import { Component, ElementRef, OnInit, AfterViewChecked, Inject} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { FormControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Observable,  of } from 'rxjs';
import { localStorageToken } from '../utility/localstorage.token';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'djuber-live-chat',
  templateUrl: './live-chat.component.html',
  styleUrls: ['./live-chat.component.css']
})
export class LiveChatComponent implements OnInit {

  url = 'http://localhost:8080'
  channelName?: string;
  socket?: WebSocket;
  stompClient?: Stomp.Client;

  constructor(private http:HttpClient, private el: ElementRef, @Inject(localStorageToken) private localStorage : Storage) { }

  ngOnInit(): void {
    this.connectToChat();
  }


  connectToChat() {
    this.channelName = '100000';
    this.loadChat('gage');
    console.log('connecting to chat...');
    this.socket = new SockJS(this.url + '/chat',{
      transports: ['xhr-streaming'],
      headers: {'Authorization': 'Bearer '+ this.localStorage.getItem("jwt")}
  });
    this.stompClient = Stomp.over(this.socket);

    this.stompClient.connect({}, (frame) => {
      //func = what to do when connection is established
      console.log('connected to: ' + frame);
      this.stompClient!.subscribe(
        '/topic/messages/' + this.channelName,
        (response) => {
          //func = what to do when client receives data (messages)
          this.loadChat(response);
        }
      );
    });
  }

  sendMessage() {

    this.stompClient!.send(
        '/app/chat/' + this.channelName,
        {},
        JSON.stringify({
          content: 'gageee',
          senderFirstName: this.localStorage.getItem("user-first-name"),
          senderLastName: this.localStorage.getItem("user-last-name"),
        })
      );
  }

  loadChat(response){

  }
}
