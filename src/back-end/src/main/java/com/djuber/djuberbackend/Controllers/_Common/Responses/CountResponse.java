package com.djuber.djuberbackend.Controllers._Common.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountResponse {
    long count;

    public CountResponse(long count) {
        this.count = count;
    }
}
