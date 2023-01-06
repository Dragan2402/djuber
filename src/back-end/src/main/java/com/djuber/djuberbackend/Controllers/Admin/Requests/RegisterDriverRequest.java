package com.djuber.djuberbackend.Controllers.Admin.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDriverRequest {
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

    @NotEmpty(message = "The car type is required.")
    String carType;

    @NotEmpty(message = "The license plate type is required.")
    String licensePlate;

    @NotNull(message = "Must provide additional services set. Can be empty.")
    Set<String> additionalServices;

    String picture;

    public RegisterDriverRequest(String email, String password, String confirmPassword, String firstName, String lastName,
                                 String city, String phoneNumber, String carType, String licensePlate,Set<String> services ,String picture) {
        if (!confirmPassword.equals(password)) {
            throw new ValidationException("Passwords must match");
        }
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.carType = carType;
        this.licensePlate = licensePlate;
        this.additionalServices = services;
        this.picture = picture;
    }
}
