/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.App.covidMngrService;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
    
public interface CovidManagerService extends Remote {
    public int login(String userName, String password) 
            throws RemoteException;
    public String totalCovidCases() 
            throws RemoteException;
    public String currentCovidCases() 
            throws RemoteException;
    public String potentiallyExposed() 
            throws RemoteException;
    public Schedule fetchSchedule() 
            throws RemoteException;
    public void saveSchedule(Schedule schedule) 
            throws RemoteException;
    public String totalStudents() 
            throws RemoteException;
    public ArrayList<Student> fetchStudents() 
            throws RemoteException;
    public Student fetchStudent(int studentId) 
            throws RemoteException;
    public Student addStudent(Student student) 
            throws RemoteException;
    public void deleteStudent(Student student) 
            throws RemoteException;
    public void addCovidCase(int studentId, LocalDate caseDate) 
            throws RemoteException;
    public void updateStudentCourses(int studentId, List<Schedule.CoursesEnum> 
            courses) 
            throws RemoteException;
    public List<Integer> lastCovidCases() 
            throws RemoteException;
}

