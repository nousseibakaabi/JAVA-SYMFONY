package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontUserController {
    @javafx.fxml.FXML
    public void VisitProfile(ActionEvent actionEvent) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("UserDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage= new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
            // Assuming you have an instance of UserDetailsController
            UserDetailsController userDetailsController = fxmlLoader.getController();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
