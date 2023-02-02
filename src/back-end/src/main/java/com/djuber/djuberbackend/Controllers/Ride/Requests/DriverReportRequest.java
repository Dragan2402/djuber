package com.djuber.djuberbackend.Controllers.Ride.Requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverReportRequest {

    @Size(max = 500, message = "The reason is too long")
    String reason;
}
