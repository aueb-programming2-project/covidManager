/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import java.io.IOException;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Login FXML controller class
 */
public class LoginCntrl {

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;

    @FXML
    public void onEnter(ActionEvent ae) throws IOException {
       login();
    }
    
    @FXML
    void loginButtonPushed(ActionEvent event) throws IOException {
        login();
    }
    
    private void login() throws IOException {
        try {
            String userNameInput = userName.getText();
            String passwordInput = password.getText();
            int res = App.covidMngrService.login(userNameInput, 
                    passwordInput);
            
            switch (res) {
                case 1:  
                    App.setRoot("secretaryFrame");;
                    break;
                case 2:
                    App.userName = userNameInput;
                    App.setRoot("studentFrame");
                    break;
                default: 
                    password.clear();
                    break;
            }
        }
        catch (RemoteException x) {
        }
    }
}
