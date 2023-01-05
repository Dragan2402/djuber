package com.djuber.djuberbackend.Controllers._Common.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageUpdateRequest {

    @NotEmpty(message = "The image is required.")
    String image;
}
