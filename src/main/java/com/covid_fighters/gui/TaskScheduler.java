/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TaskScheduler {
    ScheduledExecutorService scheduledExecutor = Executors
            .newScheduledThreadPool(1);
    ProbabilityCalculator probabilityCalculator;

    /**
     *
     * @param probabilityCalculator
     */
    public TaskScheduler(ProbabilityCalculator probabilityCalculator) 
    {
        this.probabilityCalculator = probabilityCalculator;
    }

    public void startAt(int targetHour, int targetMin, int targetSec)
    {
        Runnable taskWrapper = new Runnable(){

            @Override
            public void run() 
            {
                probabilityCalculator.execute();
            }

        };
        long delaySec = calcNextDelay(targetHour, targetMin, targetSec);
        scheduledExecutor.schedule(taskWrapper, delaySec, TimeUnit.SECONDS);
    }

    private long calcNextDelay(int targetHour, int targetMin, int targetSec) 
    {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedNow = ZonedDateTime.of(localDateTimeNow, zoneId);
        ZonedDateTime zonedNextTarget = zonedNow
                .withHour(targetHour)
                .withMinute(targetMin)
                .withSecond(targetSec);
        if(zonedNow.compareTo(zonedNextTarget) > 0)
            zonedNextTarget = zonedNextTarget.plusDays(1);

        Duration duration = Duration.between(zonedNow, zonedNextTarget);
        return duration.getSeconds();
    }

    public void stop()
    {
        scheduledExecutor.shutdown();
        try {
            scheduledExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(TaskScheduler.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    } 
}
