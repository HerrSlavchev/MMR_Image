/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mmr.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author root
 */
public class MainClass extends Application {

    //the primary stage
    private static Stage stage = null;

    /**
     * Overrides the basic Application method in order to prepare the UI. Sets
     * title and the scene that we will use as primary.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //bind the primary stage to this controller
        stage = primaryStage;
        //prepare the UI
        primaryStage.setTitle("Programming task 1: IndexerFX");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 
     * @return the primary stage
     */
    public static Stage getStage() {
        return stage;
    }

}
