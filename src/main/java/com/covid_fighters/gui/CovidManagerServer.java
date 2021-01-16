/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.codecs.configuration.CodecRegistry;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.conversions.Bson;

  
public class CovidManagerServer 
        extends UnicastRemoteObject implements CovidManagerService {

    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Student> students;
    private static MongoCollection<Courses> courses;
    private static Courses coursesSchedule;
    
    protected CovidManagerServer() throws RemoteException {
        super();
    }

    
    @Override
    public ArrayList<Student> getStudent() throws RemoteException {
        // Retrieves all students from database (very bad practice!!)
        ArrayList<Student> studentsList = students.find().into(
                new ArrayList<>());
        return studentsList; 
    }
    
    @Override
    public void addStudent(Student student) throws RemoteException {
        
        // Increase student Id
        Document query = new Document("_id", "UNIQUE COUNT STUDENT IDENTIFIER");
        Document update = new Document("$inc", new Document("STUDENT_ID", 1));
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        // Creates a new document when no document matches the query criteria
        options.upsert(true);
        Document studentId = db.getCollection("student_id_seq").findOneAndUpdate(query, update, options);
        
        // Set Student Id
        int studentIdNum = studentId.getInteger("STUDENT_ID");
        student.setIdNumber(studentIdNum);
        
        students.insertOne(student);
    }
    
    @Override
    public void deleteStudent(Student student) throws RemoteException {
        Bson filterByStudentId = eq("id_number", student.getIdNumber());
        students.deleteOne(filterByStudentId);
    }

    @Override
    public int login(String userName, String password) throws RemoteException {
      if("secretary".equals(userName) && "pass".equals(password)) {
          return 1; 
      } else {
          // Check if the student id exists in the database
          Student student = students.find(
                  eq("id_number", Integer.parseInt(userName))).first();
          if (student != null) { // if exists validate the password
              if (student.decPassword().equals(password)) {
                  return 2;
              }
          }
          return 0;
      }
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
        }
        catch (RemoteException e) {
            System.out.println("ERROR: Could not create registry");
        }
            
        // Retrieving my MongoDB Atlas URI from the system properties
//        ConnectionString connectionString = new ConnectionString(System.getProperty("mongodb.uri"));
        ConnectionString connectionString = new ConnectionString("mongodb+srv://@cluster0.qj3sm.mongodb.net/covid?w=majority");
        
        // Configure the CodecRegistry to include a codec to handle the translation to and from BSON for our POJOs
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());

        // Add the default codec registry, which contains all the default codecs
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                                                     pojoCodecRegistry);
        
        // Wrap all settings together using MongoClientSettings
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        // Initialise connection with MongoDB
        mongoClient = MongoClients.create(clientSettings);
        
        db = mongoClient.getDatabase("covid");
        students = db.getCollection("students", Student.class);
        courses = db.getCollection("courses", Courses.class);
        
        coursesSchedule = new Courses();
        
        
        coursesSchedule.setId("COURSES");
        HashMap<String, List<DayOfWeek>> schedule;
        schedule = coursesSchedule.getSchedule();
        schedule.put(Courses.CoursesEnum.ProgrammingI.name(), Arrays.asList(DayOfWeek.FRIDAY));
        schedule.put(Courses.CoursesEnum.ProgrammingII.name(), Arrays.asList(DayOfWeek.FRIDAY));
        coursesSchedule.setSchedule(schedule);
        courses.insertOne(coursesSchedule);
        

        Bson filterByCoursesId = eq("_id", "COURSES");
        coursesSchedule = courses.find(filterByCoursesId).first();
        HashMap<String, List<DayOfWeek>> sschedule;
        sschedule = coursesSchedule.getSchedule();
        sschedule.put(Courses.CoursesEnum.ProgrammingI.name(), Arrays.asList(DayOfWeek.SATURDAY));
        coursesSchedule.setSchedule(sschedule);    
        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions()
                                                     .returnDocument(ReturnDocument.AFTER);
        coursesSchedule = courses.findOneAndReplace(filterByCoursesId, coursesSchedule, returnDocAfterReplace);
        
        System.out.println("Server is Ready");

//        // Add dummy data
//        Student newStudent;
//        newStudent = new Student("hacker","hacker1",LocalDate.of(1999, Month.MAY, 21), (int)2);
//        students.insertOne(newStudent);
//        newStudent = new Student("tester","tester1",LocalDate.of(1998, Month.JULY, 21), (int)1);
//        List<Courses> courses = Arrays.asList(Courses.Macroeconomics, Courses.DataStructures);
//        newStudent.setCourses(courses);
//        students.insertOne(newStudent);
    }
}
    
