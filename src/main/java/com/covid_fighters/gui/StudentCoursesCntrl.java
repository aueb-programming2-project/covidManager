/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

/**
 * Student Courses FXML Controller class
 */
public class StudentCoursesCntrl implements Initializable {

    
    //These items are for the ListView and TextArea
    @FXML private ListView coursesListView;
    @FXML private TextArea coursesTextArea;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // listView.getSelectionModel().select(0);
        
        //These items are for configuring the ListArea
        coursesListView.getItems().addAll("test1","test2","test3");
        coursesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    /**
     * This method will copy the Strings from the ListView and put them in the text area
     */
     public void listViewButtonPushed()
     {
        String textAreaString = "";
        
        ObservableList listOfItems = coursesListView.getSelectionModel().getSelectedItems();
        
        for (Object item : listOfItems)
        {
            textAreaString += String.format("%s%n",(String) item);
        }
        
        this.coursesTextArea.setText(textAreaString);
     }

    @FXML
    void selectButtonPushed(ActionEvent event) {
        // TODO
    }      
}
