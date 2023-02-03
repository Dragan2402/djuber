package com.djuber.djuberbackend.Controllers.Client.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddLoggedClientFundsRequest {

    @NotNull(message = "The amount is required.")
    @Positive(message = "Invalid amount.")
    Double amount;
}
