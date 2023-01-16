import { Timestamp } from "rxjs";

export interface Message{
  id:number;
  senderName:string;
  content:string;
  time:Date;
  fromAdmin:boolean;
}
