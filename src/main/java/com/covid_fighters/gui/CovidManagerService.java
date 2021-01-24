/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
    
public interface CovidManagerService extends Remote {
    public int login(String userName, String password) throws RemoteException;
    public String totalCovidCases() throws RemoteException;
    public String currentCovidCases() throws RemoteException;
    public String potentiallyExposed() throws RemoteException;
    public Schedule fetchSchedule() throws RemoteException;
    public void saveSchedule(Schedule schedule) throws RemoteException;
    public String totalStudents() throws RemoteException;
    public ArrayList<Student> fetchStudents() throws RemoteException;
    public void addStudent(Student student) throws RemoteException;
    public void deleteStudent(Student student) throws RemoteException;
}

