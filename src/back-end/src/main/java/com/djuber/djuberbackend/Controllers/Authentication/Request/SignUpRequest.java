package com.djuber.djuberbackend.Controllers.Authentication.Request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.validation.ValidationException;
import javax.validation.constraints.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotEmpty(message = "The email is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    String email;

    @NotEmpty(message = "The password is required.")
    String password;

    @NotEmpty(message = "The confirm password is required.")
    String confirmPassword;

    @NotEmpty(message = "The first name is required.")
    String firstName;

    @NotEmpty(message = "The last name is required.")
    String lastName;

    @NotEmpty(message = "The city is required.")
    String city;

    @NotEmpty(message = "The phone number is required.")
    @Pattern(regexp = "^[+]?[0-9]{8,13}$", message = "The phone number does not match the required pattern.")
    String phoneNumber;

    String picture;

    public SignUpRequest(String email, String password, String confirmPassword, String firstName, String lastName,
                         String city, String phoneNumber, String picture) {
        if(!confirmPassword.equals(password)){
            throw new ValidationException("Passwords must match");
        }
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
    }

}