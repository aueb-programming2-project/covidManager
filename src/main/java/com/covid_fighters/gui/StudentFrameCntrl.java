/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Student Frame FXML controller class 
 */
public class StudentFrameCntrl extends MainLayoutCntrl {
    
    @FXML
    private ImageView Hamburger;

    @FXML
    public void initialize() {
        MainLayoutCntrl mainCntrl = this;
        CanvasSwitcher.setMainController(mainCntrl);   
        CanvasSwitcher.loadCanvas(CanvasSwitcher.STUDENT_MAIN);
        
        URL url = getClass().getResource("threelines.png");
        Image image = new Image(url.toString());
        Hamburger.setImage(image);
    }
    
    @FXML
    void studentMain(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.STUDENT_MAIN);
    }
    
    @FXML
    void studentCourses(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.STUDENT_COURSES);
    }

    @FXML
    void studentLogout(ActionEvent event) {
        try {
        App.setRoot("login");
        } catch (IOException ex) {
            Logger.getLogger(StudentFrameCntrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
