/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.CovidManagerServer.QUARANTINE_DAYS;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;


public class ProbabilityCalculator {
    private final double COVID_PROBABILITY_FACTOR = 0.5;
    private final double MAX_PROBABILITY_VALUE = 100;
    private MongoCollection<Student> studentsColl;
    private MongoCollection<Schedule> scheduleColl;
    
    public ProbabilityCalculator(MongoCollection<Student> studentsColl, 
                        MongoCollection<Schedule> scheduleColl) {
    
        this.studentsColl = studentsColl;
        this.scheduleColl = scheduleColl;
    }
    
    private void initializeToZero() {
        // Set covid_probability_tmp to Zero
        Bson gteThanZeroQuery = gte("covid_probability_tmp", 0);
        Bson updateToMax = set("covid_probability_tmp", 0);
        studentsColl.updateMany(gteThanZeroQuery, updateToMax);
    }
    
    private void compProbability(Schedule.CoursesEnum course, LocalDate date) {
        LocalDate fromDate = date.minusDays(QUARANTINE_DAYS);
        LocalDate toDate = date;
        
        // Count all students with covid that attent the course
        List<Bson> covidCasesQuery = new ArrayList<Bson>();
        covidCasesQuery.add(gte("covid_case", fromDate));
        covidCasesQuery.add(lte("covid_case", toDate));
        long studentsWithCovid = studentsColl.countDocuments(
                (and(covidCasesQuery)));
        
        // Compute spread probability 
        double incProbability = studentsWithCovid * COVID_PROBABILITY_FACTOR;

        // Increase infection propability to all students that attent the course
        List<Bson> attendancesQuery = new ArrayList<Bson>();
        attendancesQuery.add(all("courses", course));
        attendancesQuery.add(eq("covid_case", null));
        Bson updateProbability = inc("covid_probability_tmp", incProbability);
        studentsColl.updateMany((and(attendancesQuery)), updateProbability);
    }

    private void checkGteMax() {
        // Set to Max if greater than Max
        Bson outOfRangeQuery = gt("covid_probability_tmp", MAX_PROBABILITY_VALUE);
        Bson updateToMax = set("covid_probability_tmp", MAX_PROBABILITY_VALUE);
        studentsColl.updateMany(outOfRangeQuery, updateToMax);
    }
    
    private void copyValues() {
        Bson query = new Document();
        List<Bson> updatePipeline = Arrays.asList(
                Filters.eq("$set", Filters.eq(
                        "covid_probability", "$covid_probability_tmp")));
        studentsColl.updateMany(query, updatePipeline);
    }
    
    public void execute() {
        LocalDate fromDate = LocalDate.now()
                .minusDays(CovidManagerServer.QUARANTINE_DAYS);
        LocalDate toDate = LocalDate.now();
        
        initializeToZero();
        
        // Itterate for a range of previous days 
        for (LocalDate date = fromDate; 
                date.isBefore(toDate); 
                date = date.plusDays(1)) {
            
            DayOfWeek day = date.getDayOfWeek();
            
            // Itterate for all courses 
            for (Schedule.CoursesEnum course : Schedule.CoursesEnum.values()) { 

                Schedule schedule = new Schedule();
                HashMap<String, List<DayOfWeek>> scheduleMap = schedule
                        .getScheduleMap();

                // If the course take place that day compute spread propability
                if (scheduleMap.get(course.name()).contains(day)) {
                    compProbability(course, date);
                }   
            }    
        }
        
        // Check probability out of bounds conditions and set to Max
        checkGteMax();
        
        // Copy final values from covid_probability_tmp to covid_probability
        copyValues();
    }
}
