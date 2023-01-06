package com.djuber.djuberbackend.Controllers.Authentication.Request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeRequest {
    @NotEmpty(message = "The password is required.")
    String password;

    @NotEmpty(message = "The confirm password is required.")
    String confirmPassword;

    public PasswordChangeRequest( String password, String confirmPassword) {
        if(!confirmPassword.equals(password)){
            throw new ValidationException("Passwords must match");
        }
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
