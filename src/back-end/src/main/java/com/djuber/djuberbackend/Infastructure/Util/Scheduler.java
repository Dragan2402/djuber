package com.djuber.djuberbackend.Infastructure.Util;

import com.djuber.djuberbackend.Application.Services.Driver.IDriverLogService;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageResult;
import com.djuber.djuberbackend.Application.Services.Ride.Results.RideMessageStatus;
import com.djuber.djuberbackend.Domain.Client.Client;
import com.djuber.djuberbackend.Domain.Driver.Driver;
import com.djuber.djuberbackend.Domain.Ride.Reservation;
import com.djuber.djuberbackend.Infastructure.Repositories.Driver.IDriverRepository;
import com.djuber.djuberbackend.Infastructure.Repositories.Ride.IReservationRepository;
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

import java.time.Duration;
import java.time.OffsetDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scheduler {

    final IDriverRepository driverRepository;

    final IDriverLogService driverLogService;

    final SimpMessagingTemplate simpMessagingTemplate;

    final IReservationRepository reservationRepository;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 5000)
    @Async("threadPoolTaskScheduler")
    public void scheduleDriverHoursTask() {
        System.out.println("Djuber system scheduler-- checking drivers hours limit.");
        for(Driver driver : driverRepository.getActiveDrivers()){
            if(driverLogService.driverReachedLimit(driver)){
                driver.setActive(false);
                driverRepository.save(driver);
                simpMessagingTemplate.convertAndSend("/topic/active/" + driver.getIdentity().getId(), "You have hours reached limit.");
            }
        }
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 5000)
    @Async("threadPoolTaskScheduler")
    public void scheduleReservationTask() {
        System.out.println("Djuber system scheduler-- checking reservations.");
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime fifteenMinutesFromNow = now.plusMinutes(15);
        for(Reservation reservation : reservationRepository.getAllEligibleReservationForProcessing(now, fifteenMinutesFromNow)){
            long secondsTillReservation = Duration.between(now , reservation.getStart()).toSeconds();
            if(secondsTillReservation >= 840){
                RideMessageResult result = new RideMessageResult(RideMessageStatus.RESERVATION_15_NOTIFICATION, null);
                for(Client client : reservation.getClients()){
                    simpMessagingTemplate.convertAndSend("/topic/ride/" + client.getIdentity().getId(), result);
                }
            }
            if( 570 <= secondsTillReservation && secondsTillReservation < 630){
                RideMessageResult result = new RideMessageResult(RideMessageStatus.RESERVATION_5_NOTIFICATION, null);
                for(Client client : reservation.getClients()){
                    simpMessagingTemplate.convertAndSend("/topic/ride/" + client.getIdentity().getId(), result);
                }
            }
            if( 270 <= secondsTillReservation && secondsTillReservation < 330){
                RideMessageResult result = new RideMessageResult(RideMessageStatus.RESERVATION_5_NOTIFICATION, null);
                for(Client client : reservation.getClients()){
                    simpMessagingTemplate.convertAndSend("/topic/ride/" + client.getIdentity().getId(), result);
                }
            }
            if( secondsTillReservation < 60){
                RideMessageResult result = new RideMessageResult(RideMessageStatus.RESERVATION_START, null);
                for(Client client : reservation.getClients()){
                    simpMessagingTemplate.convertAndSend("/topic/ride/" + client.getIdentity().getId(), result);
                }
                System.out.println("ADD HERE LOGIC");
            }
        }
    }
}
