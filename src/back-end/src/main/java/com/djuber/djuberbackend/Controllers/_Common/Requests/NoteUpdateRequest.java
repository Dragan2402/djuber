package com.djuber.djuberbackend.Controllers._Common.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteUpdateRequest {

    @NotNull(message = "The id is required.")
    @Positive(message = "Invalid id.")
    long id;

    @Size(max = 500, message = "The note is too long")
    String note;
}
