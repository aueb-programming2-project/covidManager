/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.App.covidMngrService;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Secretary Main FXML Controller class
 *
 */
public class SecretaryMainCntrl implements Initializable {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label currentCovidCasesLabel;
    @FXML
    private Label totalCovidCasesLabel;
    @FXML
    private Label potentiallyExposedLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            totalStudentsLabel.setText(
                    covidMngrService.totalStudents());
            currentCovidCasesLabel.setText(
                    covidMngrService.currentCovidCases());
            totalCovidCasesLabel.setText(
                    covidMngrService.totalCovidCases());
            potentiallyExposedLabel.setText(
                    covidMngrService.potentiallyExposed());
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryMainCntrl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }    
}
