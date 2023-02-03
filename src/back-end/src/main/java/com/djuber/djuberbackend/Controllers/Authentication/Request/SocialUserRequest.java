package com.djuber.djuberbackend.Controllers.Authentication.Request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialUserRequest {
    String provider;
    String id;
    String email;
    String photoUrl;
    String firstName;
    String lastName;
    String name;
    String idToken;
}
