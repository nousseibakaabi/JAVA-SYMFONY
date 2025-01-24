package com.example.testuser1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.User;
import services.UserService;
import utils.Generator;
import utils.MailingService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ForgetPasswordFormController implements Initializable {
    @javafx.fxml.FXML
    private TextField Answer;
    @javafx.fxml.FXML
    private TextField emailCode;
    @javafx.fxml.FXML
    private TextField code;
    @javafx.fxml.FXML
    private ChoiceBox Question;
    @javafx.fxml.FXML
    private Button verifBtn;
    @javafx.fxml.FXML
    private AnchorPane get_verif;
    @javafx.fxml.FXML
    private Button GetCode;
    @FXML
    private RadioButton avecCodeId;
    @FXML
    private ToggleGroup choixDeRecup;
    @FXML
    private RadioButton avecQuestionId;
    private String choix="";
    @FXML
    private Label labelGetCode;
    private String GeneratedCode="";
    @FXML
    private Button ConfirmBTN;
    @FXML
    private AnchorPane ConfirmNewPasswordPage;
    @FXML
    private TextField ConfirmNewPassword;
    @FXML
    private TextField NewPassword1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the ChoiceBox with your question list
        Question.getItems().addAll("What is your favorite food?", "What is your favorite color?",
                "What is the name of your pet?", "What is your most favorite sport?");
        avecQuestionId.setSelected(true);
        avecCodeId.setSelected(false);
        code.setDisable(true);
        GetCode.setDisable(true);
        choix="avecQuestion";

        Question.setDisable(false);
        Answer.setDisable(false);
    }



    private UserService userService = new UserService(); // Replace with your actual service

    @FXML
    public void verifbtn(ActionEvent actionEvent) {
        if(choix.equals("avecQuestion")) {
            String selectedQuestion = (String) Question.getValue();
            String userAnswer = Answer.getText();

            if (selectedQuestion == null || userAnswer.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Question and Answer Required");
                alert.setContentText("Please select a security question and provide an answer.");
                alert.showAndWait();
            } else {
                // Assuming you have the email of the user to update
                String userEmail = emailCode.getText(); // Fix: Use emailCode.getText() to get the email

                // Get the user from the database
                User existingUser = userService.getByEmail(userEmail);

                // Check if the user exists
                if (existingUser == null) {
                    System.out.println("User not found with email: " + userEmail);
                    return;
                }

                // Check if the selected question and answer match
                if (existingUser.getQuestion().equals(selectedQuestion) &&
                        existingUser.getAnswer().equals(userAnswer)) {
                    // Security question and answer are correct, allow password change
                    ConfirmNewPasswordPage.setVisible(true);
                    get_verif.setVisible(false);
                } else {
                    // Incorrect security question or answer
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Incorrect Security Question or Answer");
                    alert.setContentText("The selected security question or answer is incorrect.");
                    alert.showAndWait();
                }

            }
        }else{
            if(code.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Code REQUIRED ! ");
                alert.setContentText("Code REQUIRED ");
                alert.showAndWait();
            }
            else{
                if(GeneratedCode.equals(code.getText()))
                {
                    ConfirmNewPasswordPage.setVisible(true);
                    get_verif.setVisible(false);

                }else{

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Code NOT VALID ! ");
                    alert.setContentText("Code NOT VALID ");
                    alert.showAndWait();

                }
            }
        }
    }

    

    
    @FXML
    public void avecQuestionFunction(ActionEvent actionEvent) {
        code.setDisable(true);
        GetCode.setDisable(true);
        choix="avecQuestion";

        Question.setDisable(false);
        Answer.setDisable(false);
    }

    @FXML
    public void avecCodeFunction(ActionEvent actionEvent) {
        code.setDisable(false);
        GetCode.setDisable(false);
        Question.setDisable(true);
        Answer.setDisable(true);
        choix="avecCode";

    }

    @FXML
    public void GetCodeClick(ActionEvent actionEvent) {
        String email=emailCode.getText();
        if(email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MAIL REQUIRED ! ");
            alert.setContentText("MAIL REQUIRED ");
            alert.showAndWait();

        }else{
            User u=userService.getByEmail(email);
            if(u==null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("NOt valid mail ! ");
                alert.setContentText("NOT VALID MAIL ");
                alert.showAndWait();

            }else{
                GeneratedCode= Generator.generateCode(5);
                MailingService.SendMail(email,"ResetPassword",GeneratedCode);
                labelGetCode.setText("Check your email");

            }
        }
    }

    @FXML
    public void ConfirmBTN(ActionEvent actionEvent) {
        // Retrieve updated information from text fields
        String newPassword = NewPassword1.getText();
        String confirmNewPassword = ConfirmNewPassword.getText();

        // Validate if the new password and confirm password match
        if (!newPassword.equals(confirmNewPassword)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Mismatch");
            alert.setContentText("New password and confirm password do not match.");
            alert.showAndWait();
            return;
        }

        // Assuming you have the email of the user to update
        String userEmail = emailCode.getText(); // Fix: Use emailCode.getText() to get the email

        try {
            // Get the user from the database
            User existingUser = userService.getByEmail(userEmail);

            // Check if the user exists
            if (existingUser == null) {
                System.out.println("User not found with email: " + userEmail);
                return;
            }

            // Create a User object with updated information
            User updatedUser = new User();
            updatedUser.setPassword(newPassword);

            // Call the UserService update method
            userService.updatePassword(updatedUser, userEmail);
            System.out.println("User details updated successfully.");
            System.out.println(updatedUser);

            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Login.fxml"));
            emailCode.getScene().setRoot(fxmlLoader.load());

        } catch (SQLException e) {
            System.out.println("Error updating user details: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}




