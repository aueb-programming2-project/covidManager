/*
 * 
 * Covid Manager 
 * 
 */
package com.covid_fighters.gui;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Courses enumerator
 */
public enum Courses {
    ProgrammingI(), 
    ProgrammingII(),  
    DataStructures(),
    Mathematics(),
    Microeconomics(),
    Macroeconomics(),
    Management();
    
    private List<DayOfWeek> weekDays;
 
 
    public List<DayOfWeek> getWeekdays(){
        return weekDays;
    }

    public void setWeekdays(DayOfWeek... weekDays){
        this.weekDays = Arrays.asList(weekDays);
    }

    private Courses(DayOfWeek... weekDays) {
        this.weekDays = Arrays.asList(weekDays);
    }

    private Courses() {
        this.weekDays = Collections.emptyList();
    }
}
