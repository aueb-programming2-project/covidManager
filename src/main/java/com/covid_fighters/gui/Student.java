/*
 * 
 * Covid Manager 
 * 
 */
package com.covid_fighters.gui;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

/**
 * Student class
 */
public class Student implements Serializable {
    public static final int QUARANTINE_DAYS = 14;
    
    private ObjectId id;
    @BsonProperty(value = "first_name") // Preserve naming conversion
    private String firstName;
    @BsonProperty(value = "last_name") // Preserve naming conversion
    private String lastName;
    private LocalDate birthday;
    @BsonProperty(value = "covid_case") // Preserve naming conversion
    private LocalDate covidCase;
    @BsonProperty(value = "id_number") // Preserve naming conversion
    private int idNumber;
    private String password;
    private List<Schedule.CoursesEnum> courses;
    @BsonProperty(value = "covid_probability") // Preserve naming conversion
    private double covidProbability;
    @BsonProperty(value = "covid_probability_tmp") // Preserve naming conversion
    private double covidProbabilityTmp;
    
    // Mongodb POJOs must include a public or protected, 
    // empty, no arguments, constructor.
    public Student() {
    }

    public Student(
            String firstName, 
            String lastName, 
            LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.covidCase = null; 
        this.idNumber = 0;
        this.password = Base64.getEncoder().encodeToString(
                "12345".getBytes(StandardCharsets.ISO_8859_1));
        this.courses = Collections.emptyList();
        this.covidProbability = 0;
        this.covidProbabilityTmp = 0;
    }
    
    public Student(
            ObjectId id, 
            String firstName, 
            String lastName, 
            LocalDate birthday, 
            LocalDate covidCase,  
            String password, 
            List<Schedule.CoursesEnum> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.covidCase = covidCase;
        this.idNumber = 0;
        this.password = Base64.getEncoder().encodeToString(
                password.getBytes(StandardCharsets.ISO_8859_1));
        this.courses = courses;
        this.covidProbability = 0;
        this.covidProbabilityTmp = 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student o = (Student) obj;
            return o.idNumber == this.idNumber;
        }
        return false;
    }
    
    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }
    
    public String getPassword() {
        // Returns Base64 encoded password
        return password;  
    }
    
    public void setPassword(String password) {
        // Sets Base64 encoded Password
        this.password =  password;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }
    
    public double getCovidProbability() {
        return covidProbability;
    }

    public void setCovidProbability(double covidProbability) {
        this.covidProbability = covidProbability;
    }
    
    public double getCovidProbabilityTmp() {
        return covidProbabilityTmp;
    }

    public void setCovidProbabilityTmp(double covidProbabilityTmp) {
        this.covidProbabilityTmp = covidProbabilityTmp;
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
        int daystoRecover = 0;
        int daysBetween = Period.between(covidCase, LocalDate.now()).getDays();
        if (daysBetween < QUARANTINE_DAYS) {
            daystoRecover = QUARANTINE_DAYS - daysBetween;
        }
        return daystoRecover;
    }
    
//    public void setCourses(Schedule.CoursesEnum... courses) {
//       this.courses = Arrays.asList(courses);
//    }
    
    public void setCourses(List<Schedule.CoursesEnum> courses) {
        this.courses = courses;
    }
    
    public List<Schedule.CoursesEnum> getCourses() {
        return courses;
    }
    
    public void encPasswordAndSet(String password) {
        // Password encode to Base64
        this.password = Base64.getEncoder().encodeToString(
                password.getBytes(StandardCharsets.ISO_8859_1)); 
    }
    
    public String decPassword() {
        // Password decode from Base64
        String decodedPass = new String(
                Base64.getDecoder().decode(password),
                StandardCharsets.ISO_8859_1); 
        
        return decodedPass;
    }
    
    public String toString()
    {
        return String.format("%s %s", firstName, lastName);
    }
}
