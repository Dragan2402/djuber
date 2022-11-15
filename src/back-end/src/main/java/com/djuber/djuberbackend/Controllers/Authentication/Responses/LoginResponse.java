package com.djuber.djuberbackend.Controllers.Authentication.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String tokenType = "Bearer ";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
