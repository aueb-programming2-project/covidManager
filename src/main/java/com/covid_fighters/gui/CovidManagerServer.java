/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

  
public class CovidManagerServer 
        extends UnicastRemoteObject implements CovidManagerService {

    private final ArrayList<Student> studentList;
    
    protected CovidManagerServer() throws RemoteException {
        super();
        
        studentList = new ArrayList<>();
        
        // Add dummy data
        studentList.add(new Student("tester","tester1",LocalDate.of(1998, Month.JULY, 21), (int)1));
        studentList.add(new Student("hacker","hacker1",LocalDate.of(1999, Month.MAY, 21), (int)2));
    }

    
    @Override
    public ArrayList<Student> getStudent() throws RemoteException {
        return studentList;
    }
    
    @Override
    public void addStudent(Student student) throws RemoteException {
        studentList.add(student);
    }
    
    @Override
    public void deleteStudent(Student student) throws RemoteException {
        studentList.remove(student);
    }

    @Override
    public int login(String userName, String password) throws RemoteException {
      if("secretary".equals(userName) && "pass".equals(password)) {
          return 1;
          
      } else if ("student".equals(userName) && "pass".equals(password)) {
          return 2;
      }
      return 0;  
    }
    
    @Override
    public String totalCovidCases() throws RemoteException {
        return "test";
    }
    
    @Override
    public String currentCovidCases() throws RemoteException{
        return "test";
    }
    
    @Override
    public String totalStudents() throws RemoteException {
        return "test";
    }

    public static void main (String[] argv)
    {
        try {
            int port = 1099;
            LocateRegistry.createRegistry(port);
            Registry registry = LocateRegistry.getRegistry();
            CovidManagerServer covidManagerServer = new CovidManagerServer();
            // Register with naming service(bind with registry)
            registry.rebind("CovidManagerService", covidManagerServer); 
            System.out.println("Server is Ready");
        }
        catch (RemoteException e) {
            System.out.println("ERROR: Could not create registry");
        }
    }
}
    
