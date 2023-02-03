package com.djuber.djuberbackend.Controllers._Common.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteResponse {
    String note;

    public NoteResponse(String note){
        this.note = note;
    }
}
