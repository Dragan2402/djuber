package com.djuber.djuberbackend.Application.Services.Driver;

import com.djuber.djuberbackend.Domain.Driver.Driver;

public interface IDriverLogService {
    void logDriverDeactivation(Driver driver);

    boolean driverReachedLimit(Driver driver);
}
