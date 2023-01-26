package com.djuber.djuberbackend.Infastructure.Util;

import com.djuber.djuberbackend.Application.Services.Driver.IDriverLogService;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scheduler {

    final IDriverRepository driverRepository;

    final IDriverLogService driverLogService;

    final SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 5000)
    @Async("threadPoolTaskScheduler")
    public void scheduleTask() {
        System.out.println("Djuber system scheduler-- checking drivers hours limit.");
        for(Driver driver : driverRepository.getActiveDrivers()){
            if(driverLogService.driverReachedLimit(driver)){
                driver.setActive(false);
                driverRepository.save(driver);
                simpMessagingTemplate.convertAndSend("/topic/active/" + driver.getIdentity().getId(), "You have hours reached limit.");
            }
        }
    }
}
