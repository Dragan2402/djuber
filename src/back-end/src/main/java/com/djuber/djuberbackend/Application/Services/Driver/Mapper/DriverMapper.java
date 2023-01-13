package com.djuber.djuberbackend.Application.Services.Driver.Mapper;

import com.djuber.djuberbackend.Application.Services.Client.Results.ClientResult;
import com.djuber.djuberbackend.Application.Services.Driver.Results.DriverResult;
import com.djuber.djuberbackend.Domain.Client.Client;
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

    public Page<DriverResult> map(Page<Driver> driverPage){
        return driverPage.map(DriverResult::new);
    }
}