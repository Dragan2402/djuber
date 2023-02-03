package com.djuber.djuberbackend.Controllers.Authentication.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String tokenType = "Bearer ";
    Date expiringDate;

    public LoginResponse(Pair<String,Date> tokenData) {
        this.accessToken = tokenData.getFirst();
        this.expiringDate = tokenData.getSecond();
    }
}
