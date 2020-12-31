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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

/**
 * Secretary Schedule FXML Controller class
 */
public class SecretaryScheduleCntrl implements Initializable {
    // ComboBox 
    @FXML private ComboBox courses;
    
    // CheckBoxes
    @FXML private CheckBox monday;
    @FXML private CheckBox tuesday;
    @FXML private CheckBox wednesday;
    @FXML private CheckBox thursday;
    @FXML private CheckBox friday;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
                
        // Configuring the ComboBox
        // .class.getEnumConstants();
        courses.getItems().addAll("test1","test2","test3"); 
        courses.setVisibleRowCount(5);
        courses.setEditable(false);
        courses.setPromptText("Choose Course");
    }    
    
    
    /**
     * This will update the CheckBoxes when the ComboBox is changed
     */
    public void comboBoxWasUpdated()
    {
        courses.getValue().toString();
                
        monday.setSelected(true);
        tuesday.setSelected(true);
        wednesday.setSelected(true);
        thursday.setSelected(true);
        friday.setSelected(true);
    }
    
    @FXML
    void save(ActionEvent event) {
        monday.isSelected();
        tuesday.isSelected();
        wednesday.isSelected();
        thursday.isSelected();
        friday.isSelected();
    }
}
