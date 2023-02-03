package com.djuber.djuberbackend.Infastructure.Util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateCalculator {

    public Date getDate24HoursFromNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }

    public Date getDate2HoursFromNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return calendar.getTime();
    }

    public boolean isWithinThreeDays(OffsetDateTime timeToCheck){
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime threeDaysAgo = now.minus(3, ChronoUnit.DAYS);
        return (timeToCheck.isAfter(threeDaysAgo) && timeToCheck.isBefore(now));
    }

    public boolean isDateInThePast(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(new Date());
    }
}
