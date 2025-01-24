package com.example.testuser1;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CustomAlert {

    public static boolean showReactivationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Create a button to activate the account
        ButtonType activateButton = new ButtonType("Activate Account", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(activateButton);

        // Handle the button click
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == activateButton;
    }
}
