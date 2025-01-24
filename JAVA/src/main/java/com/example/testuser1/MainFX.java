package com.example.testuser1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("getEvent1.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Home.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("GetEtabliss1.fxml"));
       // FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("GetApprenant1.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Stat.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addFormationFront.fxml"));

        //FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}