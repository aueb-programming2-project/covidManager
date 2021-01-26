/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.App.covidMngrService;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
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
    
    @FXML
    private BarChart<String, Integer> covidCasesChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private CategoryAxis yAxis;
    
    private static final int QUARANTINE_DAYS = 14;
    
    
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
            potentiallyExposedLabel.setText(
                    covidMngrService.potentiallyExposed());
            totalCovidCasesLabel.setText(
                    covidMngrService.totalCovidCases());
            List<Integer> covidCases = covidMngrService.lastCovidCases();
            
            XYChart.Series dataSeries = new XYChart.Series();
            dataSeries.setName("Covid Cases");

            for (int i = 0; i < QUARANTINE_DAYS; i++) {
                dataSeries.getData().add(0, new XYChart.Data(
                        LocalDate.now().minusDays(i).toString(),
                        covidCases.get(i)));
            }

            covidCasesChart.getData().add(dataSeries);
            
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryMainCntrl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }    
}
