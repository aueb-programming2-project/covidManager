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
 * Secretary Frame FXML controller class
 */
public class SecretaryFrameCntrl extends MainLayoutCntrl {
    
    @FXML
    private ImageView Hamburger;
    
    @FXML
    public void initialize() throws IOException {
        MainLayoutCntrl mainCntrl = this;
        CanvasSwitcher.setMainController(mainCntrl);   
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_MAIN);
        
        
        URL url = getClass().getResource("threelines.png");
        Image image = new Image(url.toString());
        Hamburger.setImage(image);
    }
    
    
    @FXML
    void secretaryMain(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_MAIN);
    }
    
    @FXML
    void secretaryStudents(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_STUDENTS_TABLE);
    }
    
    @FXML
    void secretarySchedule(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_SCHEDULE);
    }
    
    @FXML
    void secretaryLogout(ActionEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException ex) {
            Logger.getLogger(StudentFrameCntrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
