package com.djuber.djuberbackend.Controllers.Authentication.Responses;

import com.djuber.djuberbackend.Domain.Admin.Admin;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggedUserInfoResponse {

    String firstName;
    String lastName;
    String email;
    String picture;
    String role;
}
