/*
 * 
 * Covid Manager Server
 * 
 */
package com.covid_fighters.server;


import com.covid_fighters.comlib.Student;
import com.covid_fighters.comlib.Schedule;
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
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.and;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.pushEach;
import static com.mongodb.client.model.Updates.addEachToSet;
import java.time.LocalDate;
import java.util.List;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.conversions.Bson;

  
public class CovidManagerServer 
        extends UnicastRemoteObject implements CovidManagerService {

    public static final int QUARANTINE_DAYS = 14;

    // Schedule update probability task at 1:00:00 am    
    public static final int TARGET_HOUR = 1;
    public static final int TARGET_MIN = 0;
    public static final int TARGET_SEC = 0;

    
    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Student> studentsColl;
    private static MongoCollection<Schedule> scheduleColl;
    
    private static ProbabilityCalculator probabilityCalculator;
    private static TaskScheduler taskScheduler;
    
    // Constructor
    protected CovidManagerServer() throws RemoteException {
        super();
    }

    
    @Override
    public ArrayList<Student> fetchStudents() throws RemoteException {
        // Retrieves all students from database (very bad practice!!)
        ArrayList<Student> studentsList = studentsColl.find().into(
                new ArrayList<>());
        return studentsList; 
    }
    
    
    @Override
    public Student fetchStudent(int studentId) throws RemoteException {
        Bson studentIdQuery = eq("id_number", studentId);
        Student student = studentsColl.find(studentIdQuery).first();
        return student; 
    }
    
    
    @Override
    public Student addStudent(Student student) throws RemoteException {
        // Increase student Id
        Document query = new Document("_id", "UNIQUE COUNT STUDENT IDENTIFIER");
        Document update = new Document("$inc", new Document("STUDENT_ID", 1));
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        // Creates a new document when no document matches the query criteria
        options.upsert(true);
        Document studentId = db.getCollection("student_id_seq")
                .findOneAndUpdate(query, update, options);
        
        // Set Student Id
        int studentIdNum = studentId.getInteger("STUDENT_ID");
        student.setIdNumber(studentIdNum);
        
        studentsColl.insertOne(student);
        
        return student;
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
        Bson filterCovidNotNull = eq("covid_case", null);
        return String.valueOf(studentsColl.countDocuments(filterCovidNotNull));  
    }
    
    
    @Override
    public String currentCovidCases() throws RemoteException{
        LocalDate start = LocalDate.now().minusDays(QUARANTINE_DAYS);
        LocalDate end = LocalDate.now();
        
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(gte("covid_case", start));
        bsonList.add(lte("covid_case", end));
        
        return String.valueOf(studentsColl.countDocuments(and(bsonList)));
    }
    
    
    @Override
    public String totalStudents() throws RemoteException {
        return String.valueOf(studentsColl.countDocuments());
    }
    
    
    @Override
    public String potentiallyExposed() throws RemoteException {
        List<Bson> bsonList = new ArrayList<Bson>();
        bsonList.add(eq("covid_case", null));
        bsonList.add(gte("covid_probability", 0));
        
        return String.valueOf(studentsColl.countDocuments(and(bsonList)));
    }
    
    
    @Override
    public Schedule fetchSchedule() throws RemoteException {
        Bson filterSchedule = eq("_id", "SCHEDULE");
        
        return scheduleColl.find(filterSchedule).first();
    }
    
    
    @Override
    public void addCovidCase(int studentId, LocalDate caseDate)
            throws RemoteException {
        Bson filterStudentId = eq("id_number", studentId);   
        Bson updateCovidCase = set("covid_case",caseDate);
        
        studentsColl.updateOne(filterStudentId, updateCovidCase);
    }
    
    
    @Override
    public void updateStudentCourses(int studentId, List<Schedule.CoursesEnum> 
            courses) throws RemoteException {
    
        // TODO: Update only Array
//        Bson filterStudentId = eq("id_number", studentId);   
//        Bson updateCourses = pushEach("courses", courses);
//        
//        studentsColl.findOneAndUpdate(filterStudentId, updateCourses);

        Bson studentIdQuery = eq("id_number", studentId);
        Student student = studentsColl.find(studentIdQuery).first();
        student.setCourses(courses);
        studentsColl.replaceOne(studentIdQuery, student);
    }
    
    
    @Override
    public List<Integer> lastCovidCases() throws RemoteException {
        List<Integer> covidCasesList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int covidCases;
        
        for (int i = 0; i < QUARANTINE_DAYS; i++) {
            LocalDate pastDate = today.minusDays(i);
            Bson filterByDate = eq("covid_case", pastDate);
 
            covidCases = (int)studentsColl.countDocuments(filterByDate);
            covidCasesList.add(covidCases);
        }
        return covidCasesList; 
    }
    
    
    @Override
    public void saveSchedule(Schedule schedule) throws RemoteException {
        Bson filterSchedule = eq("_id", "SCHEDULE");
        
        scheduleColl.findOneAndReplace(filterSchedule, schedule);
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
        
//        // get the logger you want to suppress
//        Logger log4j = Logger.getLogger("org.mongodb.driver");
//
//
//        // set the level to suppress your stacktrace respectively
//        log4j.setLevel(Level.WARN);


        String connString = 
                "";
        
//        String connStringDec = new String(
//                Base64.getDecoder().decode(connString),
//                StandardCharsets.ISO_8859_1); 

        // Retrieving my MongoDB Atlas URI from the system properties
        ConnectionString connectionString = new ConnectionString(connString);
        
        // Configure the CodecRegistry to include a codec to handle 
        // the translation to and from BSON for our POJOs
        CodecRegistry pojoCodecRegistry = fromProviders(
                PojoCodecProvider.builder().automatic(true).build());

        // Add the default codec registry, which contains all the default codecs
        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        
        // Wrap all settings together using MongoClientSettings
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        
        // Initialise connection with MongoDB
        try {    
            mongoClient = MongoClients.create(clientSettings);
        } catch (Exception e) {
            System.out.println("Exception mongodb");
        }
        
        // Create database and collections objects
        db = mongoClient.getDatabase("covid");
        studentsColl = db.getCollection("students", Student.class);
        scheduleColl = db.getCollection("schedule", Schedule.class);
        

        // Set Probability calculator Task Scheduler
        probabilityCalculator = 
                new ProbabilityCalculator(studentsColl, scheduleColl);
        taskScheduler =  new TaskScheduler(probabilityCalculator);
        taskScheduler.startAt(TARGET_HOUR, TARGET_MIN, TARGET_SEC);

        System.out.println("Server is Ready");
    }
}
    
