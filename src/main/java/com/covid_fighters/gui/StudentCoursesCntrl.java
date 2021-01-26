/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.App.covidMngrService;
import com.covid_fighters.gui.Schedule.CoursesEnum;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * Student Courses FXML Controller class
 */
public class StudentCoursesCntrl implements Initializable {

    
    // These items are for the ListView 
    @FXML private ListView allCoursesList;
    @FXML private ListView selectedCoursesList;
    
    private final EnumSet<CoursesEnum> allCourses = EnumSet.allOf(
            Schedule.CoursesEnum.class);
    
    private int studentId;
    List<CoursesEnum> selectedCourses;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentId = Integer.parseInt(App.userName);
        
        try {
            Student student = covidMngrService.fetchStudent(studentId);
            selectedCourses = student.getCourses();
        } catch (RemoteException ex) {
            Logger.getLogger(StudentMainCntrl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        // These items are for configuring the ListArea
        allCourses.removeAll(selectedCourses);
        allCoursesList.getItems().addAll(allCourses);
        allCoursesList.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);
        
        selectedCoursesList.getItems().addAll(selectedCourses);
        selectedCoursesList.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);
    }
    

    @FXML
    void selectButtonClicked(ActionEvent event) {
        ObservableList listOfItems = allCoursesList.getSelectionModel().
                getSelectedItems();
        
        selectedCourses.addAll(listOfItems);
        
        allCourses.removeAll(listOfItems);
        allCoursesList.getItems().clear();
        allCoursesList.getItems().addAll(allCourses);
        selectedCoursesList.getItems().clear();
        selectedCoursesList.getItems().addAll(selectedCourses);
    }    
    
    @FXML
    void deselectButtonClicked(ActionEvent event) {
        ObservableList listOfItems = selectedCoursesList.getSelectionModel().
                getSelectedItems();
        
        selectedCourses.removeAll(listOfItems);
        
        allCourses.addAll(listOfItems);
        allCoursesList.getItems().clear();
        allCoursesList.getItems().addAll(allCourses);
        selectedCoursesList.getItems().clear();
        selectedCoursesList.getItems().addAll(selectedCourses);
    }    
    
    @FXML
    void saveButtonClicked(ActionEvent event) {
        try {
            covidMngrService.updateStudentCourses(
                    studentId, selectedCourses);
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryScheduleCntrl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }  
}
