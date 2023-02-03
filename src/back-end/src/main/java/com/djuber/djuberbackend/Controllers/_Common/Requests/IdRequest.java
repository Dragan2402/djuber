package com.djuber.djuberbackend.Controllers._Common.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdRequest {

    @NotNull(message = "The id is required.")
    @Positive(message = "Invalid id.")
    long id;
}
