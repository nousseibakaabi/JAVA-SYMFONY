package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import utils.SessionManager;
import models.User;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class LoginController {

    @javafx.fxml.FXML
    private TextField EMAILINTF;

    @javafx.fxml.FXML
    private Hyperlink ForgetPassword_btn;

    @javafx.fxml.FXML
    private CheckBox showPassword;

    @javafx.fxml.FXML
    private PasswordField login_password;

    @javafx.fxml.FXML
    private TextField login_showPasssword;

    private UserService userService = new UserService();

    @javafx.fxml.FXML
    public void LOGINUPTF(ActionEvent actionEvent) {
        String email = EMAILINTF.getText();
        String password = login_showPasssword.getText();

        User user = userService.getByEmail(email);

        if (user != null && user.getPassword() != null ) {
            String hashedPasswordFromSymfony = user.getPassword(); // Retrieved from the database

            // User's provided password during login
            String userProvidedPassword = password;

            // Replace the prefix "$2a$" with "$2y$" for compatibility with jBCrypt
            String adjustedHashedPassword = hashedPasswordFromSymfony.replaceFirst("\\$2y\\$", "\\$2a\\$");

            if (BCrypt.checkpw(userProvidedPassword, adjustedHashedPassword)) {
                SessionManager.startSession(user);

                if ("Active".equals(user.getStatus())) {
                    showAlert("Login Successful", "Welcome, " + user.getName() + "!");
                    if (isAdminUser(user.getRoles())) {
                        // Redirect to Admin page (ListUser)
                        redirectToAdminPage();
                    } else {
                        try {
                            // Load the FrontUser.fxml file
                            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("GetFront.fxml"));
                            Parent root = fxmlLoader.load();

                            // Create a new stage and set the scene
                            Stage stage = new Stage();
                            stage.setTitle("Front User");
                            stage.setScene(new Scene(root));
                            stage.show();


                            // Close the login stage
                            ((Stage) EMAILINTF.getScene().getWindow()).close();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    // Set the current user in the session
                    // SessionManager.setCurrentUser(user);

                } else {

                    // Account is inactive
                    showActivationAlert(user);
                }
            }else{
                showAlert("Login failed","failed");
            }
                // Successful login


        } else {
            // Display an error message or handle unsuccessful login
            showAlert("Login Failed", "Invalid email or password.");
        }
    }private boolean isAdminUser(String rolesString) {
        // Check if rolesString is not null or empty
        if (rolesString != null && !rolesString.isEmpty()) {
            // Remove square brackets and double quotes from the rolesString
            String cleanedRolesString = rolesString.replaceAll("[\\[\\]\"]", "");

            // Split the cleanedRolesString into individual roles
            String[] roles = cleanedRolesString.split(",");

            // Check if the roles array contains the "ADMIN" role
            for (String role : roles) {
                if ("ADMIN".equals(role.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void redirectToAdminPage() {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Stat.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Admin - User List");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the login stage
                ((Stage) EMAILINTF.getScene().getWindow()).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }


    // Add this method to your LoginController class
    private void showActivationAlert(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account Inactive");
        alert.setHeaderText(null);
        alert.setContentText("Your account is currently inactive. Do you want to activate it?");

        ButtonType activateButton = new ButtonType("Activate");
        ButtonType leaveButton = new ButtonType("Leave", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(activateButton, leaveButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == activateButton) {
            // User chose to activate the account
            try {
                // Activate the account
                userService.activateAccount(user.getEmail());

                // Show a success alert
                showAlert("Account Activated", "Your account has been activated.");

                // Redirect to UserDetails (or any other appropriate view)
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("GetFront.fxml"));
                Parent root = fxmlLoader.load();



                // Create a new stage and set the scene
                Stage stage = new Stage();
                stage.setTitle("Hello!");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the login stage
                ((Stage) EMAILINTF.getScene().getWindow()).close();

            } catch (SQLException | IOException e) {
                showAlert("Error", "Error activating account: " + e.getMessage());
            }
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @javafx.fxml.FXML
    public void SIGNUPINTF(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();

            showPassword.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void ForgetPassword_btn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgetPasswordForm.fxml"));
            Parent root = loader.load();

            showPassword.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void showPassword(ActionEvent actionEvent) {
        if (showPassword.isSelected()) {
            login_showPasssword.setText(login_password.getText());
            login_showPasssword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPasssword.getText());
            login_showPasssword.setVisible(false);
            login_password.setVisible(true);
        }
    }
}
