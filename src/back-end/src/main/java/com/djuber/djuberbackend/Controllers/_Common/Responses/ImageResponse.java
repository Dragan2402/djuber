package com.djuber.djuberbackend.Controllers._Common.Responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageResponse {
    String image;

    public ImageResponse(String image64base) {
        this.image = image64base;
    }
}
