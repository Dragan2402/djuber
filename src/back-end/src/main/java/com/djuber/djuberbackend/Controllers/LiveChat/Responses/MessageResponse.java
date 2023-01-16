package com.djuber.djuberbackend.Controllers.LiveChat.Responses;

import com.djuber.djuberbackend.Domain.LiveChat.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {

    Long id;

    String senderName;

    String content;

    Timestamp time;

    Boolean fromAdmin;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.senderName = message.getSenderName();
        this.content = message.getContent();
        this.time = message.getTime();
        this.fromAdmin = message.getFromAdmin();
    }
}
