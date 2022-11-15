package com.djuber.djuberbackend.Infastructure.Exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorObject {
    Integer statusCode;
    String message;
    Date timeStamp;
}
