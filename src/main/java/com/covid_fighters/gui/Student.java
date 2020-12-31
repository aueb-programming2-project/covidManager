/*
 * 
 * Covid Manager 
 * 
 */
package com.covid_fighters.gui;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

/**
 * Student class
 */
public class Student implements Serializable {
    private String firstName, lastName;
    private LocalDate birthday;
    private LocalDate covidCase;
    private int idNumber;
    private int password;
    private List<Courses> subjects;

    public Student(String firstName, String lastName, LocalDate birthday, int idNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.idNumber = idNumber;
        this.password = 12345;
    }
    
    public Student(String firstName, String lastName, LocalDate birthday, LocalDate covidCase, int idNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.covidCase = covidCase;
        this.idNumber = idNumber;
        this.password = 12345;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student o = (Student) obj;
            return o.idNumber == this.idNumber;
        }
        return false;
    }
    
    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
    
    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public LocalDate getCovidCase() {
    return covidCase;
    }

    public void setCovidCase(LocalDate covidCase) {
        this.covidCase = covidCase;
    }
    
    public int daysUntilRecovery()
    {
        return Period.between(covidCase, LocalDate.now()).getDays();
    }
    
    public void setSubjects(Courses... subjects) {
       this.subjects = Arrays.asList(subjects);
    }
    
    public List<Courses> getSubjects() {
        return subjects;
    }
    
    public String toString()
    {
        return String.format("%s %s", firstName, lastName);
    }
}
