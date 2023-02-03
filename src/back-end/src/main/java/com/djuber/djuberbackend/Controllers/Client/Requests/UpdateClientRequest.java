package com.djuber.djuberbackend.Controllers.Client.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateClientRequest {
    @NotEmpty(message = "The first name is required.")
    String firstName;

    @NotEmpty(message = "The last name is required.")
    String lastName;

    @NotEmpty(message = "The city is required.")
    String city;

    @NotEmpty(message = "The phone number is required.")
    @Pattern(regexp = "^[+]?[0-9]{8,13}$", message = "The phone number does not match the required pattern.")
    String phoneNumber;
}
