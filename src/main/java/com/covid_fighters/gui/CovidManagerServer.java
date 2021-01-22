/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.gui;


import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
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
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.and;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.conversions.Bson;

  
public class CovidManagerServer 
        extends UnicastRemoteObject implements CovidManagerService {

    final int QUARANTINE_DAYS = 14;
    
    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Student> studentsColl;
    private static MongoCollection<Schedule> scheduleColl;
    private static Schedule schedule;
    
    protected CovidManagerServer() throws RemoteException {
        super();
    }

    
    @Override
    public ArrayList<Student> getStudent() throws RemoteException {
        // Retrieves all students from database (very bad practice!!)
        ArrayList<Student> studentsList = studentsColl.find().into(
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
        
        studentsColl.insertOne(student);
    }
    
    @Override
    public void deleteStudent(Student student) throws RemoteException {
        Bson filterByStudentId = eq("id_number", student.getIdNumber());
        studentsColl.deleteOne(filterByStudentId);
    }

    @Override
    public int login(String userName, String password) throws RemoteException {
      if("secretary".equals(userName) && "pass".equals(password)) {
          return 1; 
      } else {
          // Check if the student id exists in the database
          Student student = studentsColl.find(
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
        Bson filterByCovidNotNull = eq("covid_case", "$ne:null");
        return String.valueOf(scheduleColl.countDocuments(filterByCovidNotNull));  
    }
    
    @Override
    public String currentCovidCases() throws RemoteException{
        LocalDate start = LocalDate.now().minusDays(QUARANTINE_DAYS);
        LocalDate end = LocalDate.now();
        
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(gte("covid_case", start));
        bsonList.add(lte("covid_case", end));
        
//        BasicDBObject query = new BasicDBObject("covid_case",
//                new BasicDBObject("$gte", start));
        
//        Bson filterByCoursesId = eq("covid_case", "$gte: start", "$lt: end");
//          {"covid_case": {$gte: start, $lt: end}}  

//        BasicDBObject date = new BasicDBObject();
//        date.append("$gte", start);
//        date.append("$lte", end);
//
//        DBObject query = new BasicDBObject();
//        query.put("date", date);


//        Document query = 
//                new Document("$or", Arrays.asList(
//                new Document("last_name", "Smith"),
//                new Document("first_name", "Joe")));
        return String.valueOf(scheduleColl.countDocuments(and(bsonList)));
    }
    
    
    public static String currentCovidCases1(){
        LocalDate start = LocalDate.now().minusDays(14);
        LocalDate end = LocalDate.now();
        
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(gte("covid_case", start));
        bsonList.add(lte("covid_case", end));
        
//        BasicDBObject query = new BasicDBObject("covid_case",
//                new BasicDBObject("$gte", start));
        
//        Bson filterByCoursesId = eq("covid_case", "$gte: start", "$lt: end");
//          {"covid_case": {$gte: start, $lt: end}}  

//        BasicDBObject date = new BasicDBObject();
//        date.append("$gte", start);
//        date.append("$lte", end);
//
//        DBObject query = new BasicDBObject();
//        query.put("date", date);


//        Document query = 
//                new Document("$or", Arrays.asList(
//                new Document("last_name", "Smith"),
//                new Document("first_name", "Joe")));
        return String.valueOf(studentsColl.countDocuments(and(bsonList)));
    }
    
    @Override
    public String totalStudents() throws RemoteException {
        return String.valueOf(scheduleColl.countDocuments());
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
        ConnectionString connectionString = new ConnectionString("mongodb+srv://covidmanagerserver:covid12345manager@cluster0.qj3sm.mongodb.net/covid?w=majority");
        
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
        studentsColl = db.getCollection("students", Student.class);
        scheduleColl = db.getCollection("schedule", Schedule.class);
        
        schedule = new Schedule();
        
        
//        schedule.setId("SCHEDULE");
//        HashMap<String, List<DayOfWeek>> scheduleMap;
//        scheduleMap = schedule.getScheduleMap();
//        scheduleMap.put(Schedule.CoursesEnum.ProgrammingI.name(), Arrays.asList(DayOfWeek.FRIDAY));
//        scheduleMap.put(Schedule.CoursesEnum.ProgrammingII.name(), Arrays.asList(DayOfWeek.FRIDAY));
//        schedule.setScheduleMap(scheduleMap);
//        scheduleColl.insertOne(schedule);
//        
//
//        Bson filterByCoursesId = eq("_id", "SCHEDULE");
//        schedule = scheduleColl.find(filterByCoursesId).first();
//        HashMap<String, List<DayOfWeek>> scheduleMap1;
//        scheduleMap1 = schedule.getScheduleMap();
//        scheduleMap1.put(Schedule.CoursesEnum.ProgrammingI.name(), Arrays.asList(DayOfWeek.SATURDAY));
//        schedule.setScheduleMap(scheduleMap1);    
//        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions()
//                                                     .returnDocument(ReturnDocument.AFTER);
//        schedule = scheduleColl.findOneAndReplace(filterByCoursesId, schedule, returnDocAfterReplace);
        
        System.out.println("Server is Ready");

        // Add dummy data
        Student newStudent;
        newStudent = new Student("hacker","hacker1",LocalDate.of(1999, Month.MAY, 21), (int)2);
        studentsColl.insertOne(newStudent);
        newStudent = new Student("tester","tester1",LocalDate.of(1998, Month.JULY, 21), (int)1);
        List<Schedule.CoursesEnum> courses = Arrays.asList(Schedule.CoursesEnum.Macroeconomics, Schedule.CoursesEnum.DataStructures);
        newStudent.setCourses(courses);
        newStudent.setCovidCase(LocalDate.now());
        studentsColl.insertOne(newStudent);
        
//        System.out.println("##################################");
//
//            System.out.println(currentCovidCases1());

        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator();
        TaskScheduler taskScheduler =  new TaskScheduler(probabilityCalculator);
        taskScheduler.startAt(22, 54, 0);
    }
}
    
