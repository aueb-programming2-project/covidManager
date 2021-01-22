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
    volatile boolean isStopIssued;

    /**
     *
     * @param probabilityCalculator
     */
    public TaskScheduler(ProbabilityCalculator probabilityCalculator) 
    {
        this.probabilityCalculator = probabilityCalculator;
    }

    public void startExecutionAt(int targetHour, int targetMin, int targetSec)
    {
        Runnable taskWrapper = new Runnable(){

            @Override
            public void run() 
            {
                probabilityCalculator.execute();
                //startExecutionAt(targetHour, targetMin, targetSec);
            }

        };
        long delay = calcNextDelay(targetHour, targetMin, targetSec);
        scheduledExecutor.schedule(taskWrapper, delay, TimeUnit.SECONDS);
    }

    private long calcNextDelay(int targetHour, int targetMin, int targetSec) 
    {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
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
