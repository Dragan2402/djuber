package com.djuber.djuberbackend.Controllers.Driver.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDriverRequest {
    @NotEmpty(message = "The first name is required.")
    String firstName;

    @NotEmpty(message = "The last name is required.")
    String lastName;

    @NotEmpty(message = "The city is required.")
    String city;

    @NotEmpty(message = "The license plate is required.")
    String licensePlate;

    @NotEmpty(message = "The car type is required.")
    String carType;

    @NotEmpty(message = "The phone number is required.")
    @Pattern(regexp = "^[+]?[0-9]{8,13}$", message = "The phone number does not match the required pattern.")
    String phoneNumber;

    @NotNull(message = "Must provide additional services set. Can be empty.")
    Set<String> additionalServices;
}
