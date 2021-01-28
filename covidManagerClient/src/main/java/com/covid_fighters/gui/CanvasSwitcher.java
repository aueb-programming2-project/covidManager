/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;

/**
 * Controlling switching between canvas.
 */
public class CanvasSwitcher {

    /**
     * Constants for fxml layouts managed by the navigator.
     */
    public static final String 
            STUDENT_MAIN = "studentMain";
    public static final String 
            STUDENT_COURSES = "studentCourses";
    public static final String 
            SECRETARY_MAIN = "secretaryMain";
    public static final String 
            SECRETARY_SCHEDULE = "secretarySchedule";
    public static final String 
            SECRETARY_STUDENTS_TABLE = "secretaryStudentsTable";
    public static final String 
            SECRETARY_STUDENTS_PROFILE = "secretaryStudentProfile";

    /**
     * The main application layout controller.
     */
    private static MainLayoutCntrl mainCntrl;
    
    /**
     * The current canvas layout controller.
     */
    private static Object controller;
    

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainCntrl the main application layout controller.
     */
    public static void setMainController(MainLayoutCntrl mainCntrl) {
        CanvasSwitcher.mainCntrl = mainCntrl;
    }
    

    /**
     * Loads the canvas specified by the fxml file into the canvas holder pane 
     * of the main application layout.
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadCanvas(String fxml) {
        try {
            URL fxmlUrl = CanvasSwitcher.class.getResource(fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            mainCntrl.setCanvas(fxmlLoader.load());
            controller = fxmlLoader.getController();
        } catch (IOException e) {
        }
    }
    
    
    /**
     * Returns the object of the current canvas layout controller.
     *
     * @return the object of canvas controller.
     */
    public static Object getController() {
        return controller;
    }
}
