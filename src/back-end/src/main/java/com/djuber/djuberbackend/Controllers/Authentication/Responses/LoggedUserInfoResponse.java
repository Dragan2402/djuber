package com.djuber.djuberbackend.Controllers.Authentication.Responses;

import com.djuber.djuberbackend.Domain.Admin.Admin;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggedUserInfoResponse {

    String firstName;
    String lastName;
    String email;
}
