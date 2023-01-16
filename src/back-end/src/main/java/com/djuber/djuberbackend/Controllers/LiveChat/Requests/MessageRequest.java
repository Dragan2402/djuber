package com.djuber.djuberbackend.Controllers.LiveChat.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {

    String content;

    String senderFirstName;

    String senderLastName;

    Boolean fromAdmin;
}
