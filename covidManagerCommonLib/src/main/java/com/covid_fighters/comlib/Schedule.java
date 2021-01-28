/*
 * 
 * Covid Manager 
 * 
 */
package com.covid_fighters.comlib;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;


/**
 * Schedule class
 */
public class Schedule implements Serializable {
    @BsonId()
    private String id;
    private HashMap<String, List<DayOfWeek>> scheduleMap;
    
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
    public Schedule() {
        scheduleMap =  new HashMap<>();
                
        for (CoursesEnum course : CoursesEnum.values()) { 
            scheduleMap.put(course.name(), Collections.emptyList());
        }  
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
    
    public HashMap<String, List<DayOfWeek>> getScheduleMap() {
        return scheduleMap;
    }

    public void setScheduleMap(HashMap<String, List<DayOfWeek>> scheduleMap) {
        this.scheduleMap = scheduleMap;
    }
}
