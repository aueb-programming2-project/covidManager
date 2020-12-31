/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Secretary Student Profile FXML Controller class
 */
public class SecretaryStudentProfileCntrl implements Initializable {

    private Student selectedStudent;
    
    @FXML private Label idLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;    
    @FXML private Label birthdayLabel;
    @FXML private Label recoverDaysLabel;
    
    /**
     * This method accepts a student to initialize the view
     * @param student 
     */
    public void initData(Student student)
    {
        selectedStudent = student;
        idLabel.setText(String.valueOf(selectedStudent.getIdNumber()));
        firstNameLabel.setText(selectedStudent.getFirstName());
        lastNameLabel.setText(selectedStudent.getLastName());
        birthdayLabel.setText(selectedStudent.getBirthday().toString());
        recoverDaysLabel.setText(Integer.toString(selectedStudent.daysUntilRecovery()));
    }
    
    
    /**
     * When this method is called, it will change the canvas to TableView
     */
    @FXML
    void backButton(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_STUDENTS_TABLE);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }      
}
