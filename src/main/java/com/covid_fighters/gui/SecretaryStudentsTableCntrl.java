/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import static com.covid_fighters.gui.App.covidMngrService;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Secretary Students Table FXML Controller class
 */
public class SecretaryStudentsTableCntrl implements Initializable {

    //configure the table
    @FXML private TableView<Student> tableView;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, LocalDate> birthdayColumn;
    @FXML private TableColumn<Student, LocalDate> covidColumn;
    @FXML private TableColumn<Student, Integer> idNumberColumn;
    
    //These instance variables are used to create new Student objects
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private DatePicker birthdayDatePicker;
    
    @FXML private Button detailedStudentViewButton;
    
    
    /**
     * This method will allow the user to double click on a cell and update
     * the first name of the person
     */
    public void changeFirstNameCellEvent(CellEditEvent edittedCell)
    {
        Student personSelected =  tableView.getSelectionModel().getSelectedItem();
        personSelected.setFirstName(edittedCell.getNewValue().toString());
    }
    
    /**
     * This method will allow the user to double click on a cell and update
     * the last name of the person
     */
    public void changeLastNameCellEvent(CellEditEvent edittedCell)
    {
        Student personSelected =  tableView.getSelectionModel().getSelectedItem();
        personSelected.setLastName(edittedCell.getNewValue().toString());
    }
    
    /**
     * This method will enable the detailed view button once a row in the table is
     * selected
     */
    public void userClickedOnTable()
    {
        this.detailedStudentViewButton.setDisable(false);
    }
    
    /**
     * When this method is called, it will pass the selected Student object to
     * a the detailed view
     */
    public void changeCanvasToDetailedStudentView(ActionEvent event) throws IOException {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_STUDENTS_PROFILE);
        
        //access the controller and call a method
        SecretaryStudentProfileCntrl controller = (SecretaryStudentProfileCntrl)CanvasSwitcher.getController();
        controller.initData(tableView.getSelectionModel().getSelectedItem());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up the columns in the table
        idNumberColumn.setCellValueFactory
        (new PropertyValueFactory<Student, Integer>("idNumber"));
        firstNameColumn.setCellValueFactory
        (new PropertyValueFactory<Student, String>("firstName"));
        lastNameColumn.setCellValueFactory
        (new PropertyValueFactory<Student, String>("lastName"));
        birthdayColumn.setCellValueFactory
        (new PropertyValueFactory<Student, LocalDate>("birthday"));
        covidColumn.setCellValueFactory
        (new PropertyValueFactory<Student, LocalDate>("covid"));
        
        try {
            // cast to ObservableList of Student objects
            tableView.setItems(FXCollections.observableList(covidMngrService.getStudent()));
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryStudentsTableCntrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Update the table to allow for the first and last name fields
        // to be not editable
        tableView.setEditable(false);
        
        // firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // This will disable the table to select multiple rows at once
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Disable the detailed student view button until a row is selected
        this.detailedStudentViewButton.setDisable(true);
    }    
    
    
    /**
     * This method will remove the selected row from the table 
     */
    public void deleteButtonPushed() throws RemoteException
    {
        ObservableList<Student> allStudents;
        Student selectedRows;
        allStudents = tableView.getItems();
        
        // This gives us the row that was selected
        selectedRows = tableView.getSelectionModel().getSelectedItems().get(0);
        
        // Remove the Student object from the table
        allStudents.remove(selectedRows);
        covidMngrService.deleteStudent(selectedRows);
    }
    
    
    
    /**
     * This method will create a new Student object and add it to the table
     */
    public void newStudentButtonPushed() throws RemoteException
    {
        Student newStudent = new Student(firstNameTextField.getText(),
                                      lastNameTextField.getText(),
                                      birthdayDatePicker.getValue(), 1); // TODO
        
        // Get all the items from the table as a list, then add the new student to
        // the list
        tableView.getItems().add(newStudent);
        covidMngrService.addStudent(newStudent);
    }
}
