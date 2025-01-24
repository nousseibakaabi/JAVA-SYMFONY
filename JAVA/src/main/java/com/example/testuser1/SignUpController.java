package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpController implements Initializable {

    @javafx.fxml.FXML
    private TextField ADDTF;
    @javafx.fxml.FXML
    private TextField nameTF;
    @javafx.fxml.FXML
    private ComboBox<String> ROLETF;
    @javafx.fxml.FXML
    private TextField EMAILUPTF;
    @javafx.fxml.FXML
    private TextField CPASSUPTF;
    @javafx.fxml.FXML
    private TextField passUPTF;
    @javafx.fxml.FXML
    private Label errors;
    @javafx.fxml.FXML
    private TextField Answer;
    @javafx.fxml.FXML
    private ComboBox<String> Question;
    @javafx.fxml.FXML
    private Button SignUP;

    private UserService us = new UserService();
    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    @javafx.fxml.FXML
    public void SignUP(ActionEvent actionEvent) {
        String name = nameTF.getText();
        String address = ADDTF.getText();
        String role = ROLETF.getValue();
        String question = Question.getValue();
        String answer = Answer.getText();
        String email = EMAILUPTF.getText();
        String password = passUPTF.getText();
        String confirmPassword = CPASSUPTF.getText();

        // Validation
        if (!User.isValidName(name)) {
            errors.setText("Invalid name. Please use only letters.");
            return;
        }

        if (!User.isValidEmail(email)) {
            errors.setText("Invalid email format.");
            return;
        }

        try {
            if (us.userExist(email)) {
                errors.setText("Email already exists");
                return;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user exists", e);
            errors.setText("An error occurred. Please try again.");
            return;
        }

        if (!User.isValidAddress(address)) {
            errors.setText("Invalid address format.");
            return;
        }

        if (!User.isValidPassword(password)) {
            errors.setText("Invalid password. Password must have at least 6 characters.");
            return;
        }

        if (name.isEmpty() || address.isEmpty() || role == null || question == null || answer.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errors.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errors.setText("Passwords do not match.");
            return;
        }

        try {
            String status = "Active";
            String rolesArray = role;
            User user = new User(rolesArray, name, email, password, address, question, answer, status);
            us.add(user);  // Add user to the database

            // Clear input fields after signup
            clearFields();

            // Load the login scene
            loadLoginScene();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error signing up user", e);
            errors.setText("An error occurred. Please try again.");
        }
    }

    private void clearFields() {
        nameTF.clear();
        ADDTF.clear();
        ROLETF.getSelectionModel().clearSelection();
        Question.getSelectionModel().clearSelection();
        Answer.clear();
        EMAILUPTF.clear();
        passUPTF.clear();
        CPASSUPTF.clear();
    }

    private void loadLoginScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) ROLETF.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading login scene", e);
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void LOGINUPTF(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            passUPTF.getScene().setRoot(root);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading login scene from LOGINUPTF", e);
            throw new RuntimeException(e);
        }
    }

    private final String[] questionList = {
            "What is your favorite food?",
            "What is your favorite color?",
            "What is the name of your pet?",
            "What is your most favorite sport?"
    };

    private final String[] roleList = {
            "[\"Apprenant\"]",
            "[\"Tuteur\"]",
            "[\"Responsable d'etablissement\"]"
    };


    private void populateQuestions() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        Question.setItems(listData);
    }

    private void populateRoles() {
        List<String> listR = new ArrayList<>();
        for (String data : roleList) {
            listR.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listR);
        ROLETF.setItems(listData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateQuestions();
        populateRoles();
    }
}
