/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * Student Main FXML Controller class
 */
public class StudentMainCntrl implements Initializable {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label covidStatusLabel;
    @FXML
    private DatePicker caseDatePicker;
    
    private static final int QUARANTINE_DAYS = 14;
    
    private int studentId;
    private Student student;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentId = Integer.parseInt(App.userName);
        
        try {
            student = covidMngrService.fetchStudent(studentId);
        } catch (RemoteException ex) {
            Logger.getLogger(StudentMainCntrl.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        userNameLabel.setText(student.toString());
        
        updateStatusLabel();
    } 
    
    private void updateStatusLabel () {
        LocalDate covidCase = student.getCovidCase();
        double covidProbability = student.getCovidProbability(); 
        
        
        if (covidCase != null && 
                (covidCase.isBefore(LocalDate.now()) 
                || covidCase.isEqual(LocalDate.now()))) {
            int daysToRecover = student.daysUntilRecovery();
            if (daysToRecover > 0) {
                covidStatusLabel.setTextFill(Color.RED);
               covidStatusLabel.setText("Days to recover: " + daysToRecover);
            } else {
               covidStatusLabel.setTextFill(Color.GREEN);
               covidStatusLabel.setText("Covid Status: You are recovered"); 
            }
        } else {
            covidStatusLabel.setTextFill(Color.ORANGE);
            covidStatusLabel.setText("Potentially exposed propability: " +
                    covidProbability + "%");
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
                student.setCovidCase(caseDate);
                updateStatusLabel();
            } catch (RemoteException ex) {
                Logger.getLogger(SecretaryScheduleCntrl.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Date is not correct (empty date or date in the future)!");
            alert.showAndWait();
        }
    }
}
