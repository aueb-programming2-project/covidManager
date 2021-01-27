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
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

/**
 * Secretary Schedule FXML Controller class
 */
public class SecretaryScheduleCntrl implements Initializable {
    // ComboBox 
    @FXML private ComboBox coursesComboBox;
    
    // CheckBoxes
    @FXML private CheckBox mondayCheckBox;
    @FXML private CheckBox tuesdayCheckBox;
    @FXML private CheckBox wednesdayCheckBox;
    @FXML private CheckBox thursdayCheckBox;
    @FXML private CheckBox fridayCheckBox;
    
    private static Schedule schedule; 
    private static String selectedCourse;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            schedule = covidMngrService.fetchSchedule();
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryScheduleCntrl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        // Configuring the ComboBox
        // .class.getEnumConstants();
        coursesComboBox.getItems().addAll(EnumSet.allOf(CoursesEnum.class)); 
        coursesComboBox.setVisibleRowCount(5);
        coursesComboBox.setEditable(false);
        coursesComboBox.setPromptText("Choose Course");
    }    
    
    
    /**
     * This will update the CheckBoxes when the ComboBox is changed
     */
    public void comboBoxWasUpdated()
    {
        selectedCourse = coursesComboBox.getValue().toString();
        
        
        HashMap<String, List<DayOfWeek>> scheduleMap;
        scheduleMap = schedule.getScheduleMap();
        
        List<DayOfWeek> days = scheduleMap.get(selectedCourse);
        
        mondayCheckBox.setSelected(days.contains(DayOfWeek.MONDAY));
        tuesdayCheckBox.setSelected(days.contains(DayOfWeek.TUESDAY));
        wednesdayCheckBox.setSelected(days.contains(DayOfWeek.WEDNESDAY));
        thursdayCheckBox.setSelected(days.contains(DayOfWeek.THURSDAY));
        fridayCheckBox.setSelected(days.contains(DayOfWeek.FRIDAY));
    }
    
    @FXML
    void saveButtonClicked(ActionEvent event) {
        List<DayOfWeek> days = new ArrayList<>();
        if(mondayCheckBox.isSelected()) {
            days.add(DayOfWeek.MONDAY);
        }
        if(tuesdayCheckBox.isSelected()) {
            days.add(DayOfWeek.TUESDAY);
        }
        if(wednesdayCheckBox.isSelected()) {
            days.add(DayOfWeek.WEDNESDAY);
        }
        if(thursdayCheckBox.isSelected()) {
            days.add(DayOfWeek.THURSDAY);
        }
        if(fridayCheckBox.isSelected()) {
            days.add(DayOfWeek.FRIDAY);
        }

        HashMap<String, List<DayOfWeek>> scheduleMap;
        scheduleMap = schedule.getScheduleMap();
        scheduleMap.put(selectedCourse, days);
        schedule.setScheduleMap(scheduleMap);
        
        try {
            covidMngrService.saveSchedule(schedule);
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryScheduleCntrl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
