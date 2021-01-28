/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import com.covid_fighters.comlib.Student;
import static com.covid_fighters.gui.App.covidMngrService;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.types.ObjectId;

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
    
    private final int MIN_CHARS = 3;
    private final int MIN_YEARS_OLD = 17;
    
    
    /**
     * This method will allow the user to double click on a cell and update
     * the first name of the person
     * @param edittedCell
     */
    public void changeFirstNameCellEvent(CellEditEvent edittedCell) {
        Student personSelected =  tableView.getSelectionModel().
                getSelectedItem();
        personSelected.setFirstName(edittedCell.getNewValue().toString());
    }
    
    /**
     * This method will allow the user to double click on a cell and update
     * the last name of the person
     * @param edittedCell
     */
    public void changeLastNameCellEvent(CellEditEvent edittedCell) {
        Student personSelected =  tableView.getSelectionModel().
                getSelectedItem();
        personSelected.setLastName(edittedCell.getNewValue().toString());
    }
    
    /**
     * This method will enable the detailed view button once a row in the table 
     * is selected
     */
    public void userClickedOnTable()
    {
        this.detailedStudentViewButton.setDisable(false);
    }
    
    /**
     * When this method is called, it will pass the selected Student object to
     * a the detailed view
     * @param event
     */
    public void changeCanvasToDetailedStudentView(ActionEvent event) {
        CanvasSwitcher.loadCanvas(CanvasSwitcher.SECRETARY_STUDENTS_PROFILE);
        
        //access the controller and call a method
        SecretaryStudentProfileCntrl controller = 
                (SecretaryStudentProfileCntrl)CanvasSwitcher.getController();
        controller.initData(tableView.getSelectionModel().getSelectedItem());
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
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
        (new PropertyValueFactory<Student, LocalDate>("covidCase"));
        
        try {
            // cast to ObservableList of Student objects
            tableView.setItems(FXCollections.observableList(
                    covidMngrService.fetchStudents()));
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryStudentsTableCntrl.class.getName()).
                    log(Level.SEVERE, null, ex);
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
    public void deleteButtonPushed() {
        ObservableList<Student> allStudents;
        Student selectedRow;
        allStudents = tableView.getItems();
        
        // This gives us the row that was selected
        selectedRow = tableView.getSelectionModel().getSelectedItems().get(0);
        

        try {
            covidMngrService.deleteStudent(selectedRow);
            
            // Remove the Student object from the table
            allStudents.remove(selectedRow);
        } catch (RemoteException ex) {
            Logger.getLogger(SecretaryStudentsTableCntrl.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * This method will create a new Student object and add it to the table
     */
    public void newStudentButtonPushed() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate birthdayDate = birthdayDatePicker.getValue();
        
        // Firts Name Validation
        if (firstName != null && (firstName.length() < MIN_CHARS)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "First Name is not correct "
                            + "(empty name or shorter that three characters)!");
            alert.showAndWait();
            
            return;
        }
        
        // Last Name Validation
        if (lastName != null && (lastName.length() < MIN_CHARS)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Last Name is not correct "
                            + "(empty name or shorter that three characters)!");
            alert.showAndWait();
            
            return;
        }
        
        // Date Validation
        if (birthdayDate == null || birthdayDate.isAfter(
                LocalDate.now().minusYears(MIN_YEARS_OLD))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Date is not correct "
                            + "(empty date or student < 17 years old)!");
            alert.showAndWait();
            
            return;
        }
        
        // Construct new student
        Student newStudent = new Student(firstName, lastName, birthdayDate);
        
        try {
            newStudent = covidMngrService.addStudent(newStudent);
            
            // Get all the items from the table as a list, then add the 
            // new student to the list
            tableView.getItems().add(newStudent);
        } catch (RemoteException ex) {
            Logger.getLogger(StudentMainCntrl.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}
