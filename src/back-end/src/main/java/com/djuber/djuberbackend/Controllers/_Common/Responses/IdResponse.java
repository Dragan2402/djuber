package com.djuber.djuberbackend.Controllers._Common.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdResponse {
    Long id;

    public IdResponse(Long id){
        this.id = id;
    }
}
