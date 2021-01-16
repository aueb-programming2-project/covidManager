/*
 * 
 * Covid Manager 
 * 
 */
package com.covid_fighters.gui;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;


/**
 * Courses class
 */
public class Courses implements Serializable {
    @BsonId()
    private String id;
    private HashMap<String, List<DayOfWeek>> schedule;
    
    public enum CoursesEnum {
        ProgrammingI,
        ProgrammingII,
        DataStructures,
        Mathematics,
        Microeconomics,
        Macroeconomics,
        Management;
    }
    
    // Mongodb POJOs must include a public or protected, 
    // empty, no arguments, constructor.
    public Courses() {
        schedule =  new HashMap<>();
                
        for (CoursesEnum course : CoursesEnum.values()) { 
            schedule.put(course.name(), Collections.emptyList());
        }  
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
    
    public HashMap<String, List<DayOfWeek>> getSchedule() {
        return schedule;
    }

    public void setSchedule(HashMap<String, List<DayOfWeek>> schedule) {
        this.schedule = schedule;
    }
}
