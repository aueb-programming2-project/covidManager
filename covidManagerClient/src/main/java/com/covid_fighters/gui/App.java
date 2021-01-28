/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import com.covid_fighters.server.CovidManagerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * Covid manager Client main class
 */
public class App extends Application {

    public static CovidManagerService covidMngrService;
    public static String userName;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Covid Manager - "
        + "Act now with the most powerful solution!");
        scene = new Scene(loadFXML("login"), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set as root the given fxml layout
     *
     * @param fxml the layout file name.
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads the given fxml layout
     *
     * @param fxml the layout file name.
     *
     * @return the loaded Parent.
     */
    private static Pane loadFXML(String fxml) throws IOException {
        URL fxmlUrl;
        fxmlUrl = App.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        
        Pane mainPane = (Pane) fxmlLoader.load();

        return mainPane;
    }

    public static void main(String[] args) {
        try {
            // null or 127.0.0.1 for localhost
            Registry registry = LocateRegistry.getRegistry("127.0.0.1"); 
            covidMngrService = (CovidManagerService) 
                    registry.lookup("CovidManagerService");
            launch();
        }
        catch (NotBoundException | RemoteException x) {
        }
    }
}