package com.djuber.djuberbackend.Application.Services.Driver.Implementation;

import com.djuber.djuberbackend.Application.Services.Driver.IDriverLogService;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Driver.DriverActiveLog;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverLogRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverLogService implements IDriverLogService {

    final IDriverLogRepository driverLogRepository;

    final IDriverRepository driverRepository;

    @Override
    public void logDriverDeactivation(Driver driver) {
        DriverActiveLog driverActiveLog = new DriverActiveLog();
        OffsetDateTime now = OffsetDateTime.now();
        driverActiveLog.setLogStart(driver.getLastActivationTime());
        driverActiveLog.setLogEnd(now);
        driverActiveLog.setDriver(driver);
        System.out.println("Logging driver time: "+ driverActiveLog);

        driverLogRepository.save(driverActiveLog);
    }

    @Override
    public boolean driverReachedLimit(Driver driver) {
        OffsetDateTime  now = OffsetDateTime.now();
        OffsetDateTime  yesterday = now.minusDays(1);
        List<DriverActiveLog> logs = driverLogRepository.getDriverActiveLogsByTimeWindow(driver.getId(), yesterday);

        long minutesActive = 0;

        if(driver.getActive()){
            minutesActive += Duration.between(driver.getLastActivationTime(), now).toMinutes();
        }
        for(DriverActiveLog log : logs){
            if(log.getLogStart().compareTo(yesterday) >= 0){
                minutesActive += Duration.between(log.getLogStart(), log.getLogEnd()).toMinutes();
            }else{
                minutesActive += Duration.between(yesterday, log.getLogEnd()).toMinutes();
            }
        }
        return minutesActive > 480;
    }
}
