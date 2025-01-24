package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.SessionManager;
import models.User;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class UserDetailsController {
    @FXML
    private TextField NewPassword;
    @FXML
    private Button updatebtn;
    @FXML
    private TextField Address;
    @FXML
    private ImageView logo;
    @FXML
    private TextField ConfirmPassword;
    @FXML
    private TableColumn<User, String> nameT;
    @FXML
    private TableColumn<User, String> addressT;
    @FXML
    private TableColumn<User, String> emailT;
    @FXML
    private TableView<User> UserTable;

    private UserService userService = new UserService();
    private String userEmail;
    @FXML
    private Hyperlink hyperlink;

    public void setEmailAddress(String email) {
        this.userEmail = email;
    }

    @FXML
    public void initialize() {
        nameT.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailT.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressT.setCellValueFactory(new PropertyValueFactory<>("address"));


        // Load current user data initially
        loadCurrentUserData();
    }

    private void loadCurrentUserData() {

        User currentUser = SessionManager.getSession().getUser();
        ObservableList<User> userList = FXCollections.observableArrayList(currentUser);
        UserTable.setItems(userList);
        UserTable.refresh();
    }

    // Event handler for the update button
    @FXML
    public void updatebtn(ActionEvent actionEvent) {
        // Retrieve updated information from text fields
        String newPassword = NewPassword.getText();
        String address = Address.getText();
        String confirmPassword = ConfirmPassword.getText();

        // Validate if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Password Mismatch", "New password and confirm password do not match.");
            return;
        }

        try {
            // Get the current user from the session
            User currentUser = SessionManager.getSession().getUser();

            // Create a User object with updated information


            // Set the new address
            currentUser.setAddress(address);

            // Check if the new password is empty, then set it to the current user's password
            if (newPassword.isEmpty()) {
                currentUser.setPassword(currentUser.getPassword());
            } else {
                currentUser.setPassword(newPassword);
            }

            // Call the UserService update method
            userService.update(currentUser);
            showAlert("Update Successful", "User details updated successfully.");
            // Refresh the user data in the table
            SessionManager.getSession().setUser(currentUser);
            loadCurrentUserData();

        } catch (SQLException e) {
            showAlert("Error", "Error updating user details: " + e.getMessage());
        }
    }


    @FXML
    public void logout(ActionEvent actionEvent) {
        // Clear the session and close the current stage
        try {
            SessionManager.clearSession();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            NewPassword.getScene().setRoot(fxmlLoader.load());
        }catch (IOException e) {

            System.out.println("PROBLEM LOAD");
        }

    }




    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void DesactivateAccount(ActionEvent actionEvent) {
        try {
            // Update the user's status to "Inactive"
            userService.deactivateAccount(SessionManager.getSession().getUser().getEmail());
            showAlert("Account Deactivated", "Your account has been deactivated.");
            logout(actionEvent);


        } catch (SQLException e) {
            showAlert("Error", "Error deactivating account: " + e.getMessage());
        }
    }

    @FXML
    public void home(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetFront.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void res(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowReservation.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void profile(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetFront.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    public void event(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEventFront1.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
