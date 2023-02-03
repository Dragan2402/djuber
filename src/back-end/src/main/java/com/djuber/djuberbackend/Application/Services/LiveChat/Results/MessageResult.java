package com.djuber.djuberbackend.Application.Services.LiveChat.Results;

import com.djuber.djuberbackend.Domain.LiveChat.Message;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.sql.Timestamp;

@Value
@AllArgsConstructor
public class MessageResult {

    Long id;

    Long chatId;

    String senderName;

    String content;

    Timestamp time;

    public MessageResult(Message message){
        this.id = message.getId();
        this.chatId = message.getChat().getId();
        this.senderName = message.getSenderName();
        this.content = message.getContent();
        this.time = message.getTime();
    }

}
