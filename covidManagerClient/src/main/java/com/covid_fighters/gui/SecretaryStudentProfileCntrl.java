/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import com.covid_fighters.comlib.Student;
import static com.covid_fighters.gui.App.covidMngrService;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * Secretary Student Profile FXML Controller class
 */
public class SecretaryStudentProfileCntrl implements Initializable {

    private Student selectedStudent;
    private int studentId;
    
    @FXML private Label idLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;    
    @FXML private Label birthdayLabel;
    @FXML private Label covidStatusLabel;
    @FXML private Label covidStatusValueLabel;
    
    @FXML private DatePicker caseDatePicker;
    
    private static final int QUARANTINE_DAYS = 14;
    
    /**
     * This method accepts a student to initialize the view
     * @param student 
     */
    public void initData(Student student)
    {
        selectedStudent = student;
        studentId = selectedStudent.getIdNumber();
        
        idLabel.setText(String.valueOf(selectedStudent.getIdNumber()));
        firstNameLabel.setText(selectedStudent.getFirstName());
        lastNameLabel.setText(selectedStudent.getLastName());
        birthdayLabel.setText(selectedStudent.getBirthday().toString());
   
        updateStatusLabel ();
    }
    
    private void updateStatusLabel () {
        LocalDate covidCase = selectedStudent.getCovidCase();
        double covidProbability = selectedStudent.getCovidProbability(); 
        
        
        if (covidCase != null) {
            int daysToRecover = selectedStudent.daysUntilRecovery();
            if (daysToRecover > 0) {
               covidStatusLabel.setTextFill(Color.RED);
               covidStatusLabel.setText("Days to recover");
               covidStatusValueLabel.setTextFill(Color.RED);
               covidStatusValueLabel.setText("" + daysToRecover);
            } else {
               covidStatusLabel.setTextFill(Color.GREEN);
               covidStatusLabel.setText("Covid Status"); 
               covidStatusValueLabel.setTextFill(Color.GREEN);
               covidStatusValueLabel.setText("recovered"); 
            }
        } else {
            covidStatusLabel.setTextFill(Color.ORANGE);
            covidStatusLabel.setText("Potentially exposed");
            covidStatusValueLabel.setTextFill(Color.ORANGE);
            covidStatusValueLabel.setText(covidProbability + "%");
        }
    }
    
    @FXML
    void confirmCaseClicked(ActionEvent event) {
        LocalDate caseDate = caseDatePicker.getValue();

        if (caseDate != null && 
                (caseDate.isBefore(LocalDate.now()) 
                || caseDate.isEqual(LocalDate.now()))) {
            try {
                covidMngrService.addCovidCase(studentId, caseDate);
                selectedStudent.setCovidCase(caseDate);
                
                updateStatusLabel ();
            } catch (RemoteException ex) {
                Logger.getLogger(SecretaryScheduleCntrl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Date is not correct (empty date or date in the future)!");
            alert.showAndWait();
        }
    }
    
    
    /**
     * When this method is called, it will change the canvas to TableView
     */
    @FXML
    void backButtonClicked(ActionEvent event) {
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
