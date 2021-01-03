/*
 * 
 * Covid Manager 
 * 
 */
package com.covid_fighters.gui;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

/**
 * Student class
 */
public class Student implements Serializable {
    private ObjectId id;
    @BsonProperty(value = "first_name") // Preserve java naming conversion
    private String firstName;
    @BsonProperty(value = "last_name") // Preserve java naming conversion
    private String lastName;
    private LocalDate birthday;
    @BsonProperty(value = "covid_case") // Preserve java naming conversion
    private LocalDate covidCase;
    @BsonProperty(value = "id_number") // Preserve java naming conversion
    private int idNumber;
    private int password;
    private List<Courses> courses;
    
    // Mongodb POJOs must include a public or protected, empty, no arguments, constructor.
    public Student() {
    }

    public Student(String firstName, String lastName, LocalDate birthday, int idNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.idNumber = idNumber;
        this.password = 12345;
        this.courses = Collections.emptyList();
    }
    
    
    public Student(ObjectId id, String firstName, String lastName, LocalDate birthday, LocalDate covidCase, int idNumber, int password, List<Courses> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.covidCase = covidCase;
        this.idNumber = idNumber;
        this.password = 12345;
        this.courses = Collections.emptyList();
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
    
//    public void setCourses(Courses... courses) {
//       this.courses = Arrays.asList(courses);
//    }
    
    public void setCourses(List<Courses> courses) {
       this.courses = courses;
    }
    
    public List<Courses> getCourses() {
        return courses;
    }
    
    public String toString()
    {
        return String.format("%s %s", firstName, lastName);
    }
}
