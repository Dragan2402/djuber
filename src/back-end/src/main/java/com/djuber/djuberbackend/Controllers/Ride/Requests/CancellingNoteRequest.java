package com.djuber.djuberbackend.Controllers.Ride.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancellingNoteRequest {

    @Size(max = 500, message = "The note is too long")
    String note;
}
