package com.djuber.djuberbackend.Application.Services.Driver.Mapper;

import com.djuber.djuberbackend.Application.Services.Driver.Results.DriverResult;
import com.djuber.djuberbackend.Controllers.Driver.Response.DriverLocationResponse;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DriverMapper {

    public List<DriverResult> map(List<Driver> drivers){
        List<DriverResult> driverResults = new ArrayList<>();
        for(Driver driver : drivers){
            driverResults.add(new DriverResult(driver));
        }
        return driverResults;
    }

    public List<DriverLocationResponse> mapDriversLocation(List<Driver> drivers){
        List<DriverLocationResponse> driverResponses = new ArrayList<>();
        for (Driver driver :drivers){
            driverResponses.add(new DriverLocationResponse(driver));
        }
        return driverResponses;
    }

    public Page<DriverResult> map(Page<Driver> driverPage){
        return driverPage.map(DriverResult::new);
    }
}