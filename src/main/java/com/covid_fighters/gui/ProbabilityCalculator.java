/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.CovidManagerServer.QUARANTINE_DAYS;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.elemMatch;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bson.conversions.Bson;


public class ProbabilityCalculator {
    private final double COVID_PROBABILITY_FACTOR = 0.5;
    private MongoCollection<Student> studentsColl;
    private MongoCollection<Schedule> scheduleColl;
    
    public ProbabilityCalculator(MongoCollection<Student> studentsColl, 
                        MongoCollection<Schedule> scheduleColl) {
    
        this.studentsColl = studentsColl;
        this.scheduleColl = scheduleColl;
    }
    
    private void compProbability(Schedule.CoursesEnum course, LocalDate date) {
        LocalDate fromDate = date.minusDays(QUARANTINE_DAYS);
        LocalDate toDate = date;
        
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(gte("covid_case", fromDate));
        bsonList.add(lte("covid_case", toDate));

    }
    
    public void execute() {
        LocalDate fromDate = LocalDate.now()
                .minusDays(CovidManagerServer.QUARANTINE_DAYS);
        LocalDate toDate = LocalDate.now();
        
        for (LocalDate date = fromDate; 
                date.isBefore(toDate); 
                date = date.plusDays(1)) {
            
            DayOfWeek day = date.getDayOfWeek();
            
            for (Schedule.CoursesEnum course : Schedule.CoursesEnum.values()) { 

                Schedule schedule = new Schedule();
                HashMap<String, List<DayOfWeek>> scheduleMap = schedule
                        .getScheduleMap();

                if (scheduleMap.get(course.name()).contains(day)) {
                    compProbability(course, date);
                }   
            }    
        }
    }
}
