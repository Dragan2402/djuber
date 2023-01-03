package com.djuber.djuberbackend.Controllers.Authentication.Request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyClientAccountRequest {
    String token;
}
