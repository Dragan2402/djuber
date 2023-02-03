package com.djuber.djuberbackend.Controllers._Common.Responses;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import com.djuber.djuberbackend.Domain.LiveChat.Chat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatResponse {

    Long id;

    Long subjectIdentityId;

    String subjectFirstName;

    String subjectLastName;

    String subjectPicture;

    public ChatResponse(){};

}
