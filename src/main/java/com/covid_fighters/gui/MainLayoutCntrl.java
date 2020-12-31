/*
 * 
 * Covid Manager Client
 * 
 */
package com.covid_fighters.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Main controller class for the canvas layout.
 */
public class MainLayoutCntrl {

    /**
     * Holder of a switchable canvas.
     */
    @FXML
    private StackPane canvas;

    /**
     * Replaces the canvas displayed with a new canvas.
     *
     * @param node the canvas node to be swapped in.
     */
    public void setCanvas(Node node) {
        canvas.getChildren().setAll(node);
    }
}
