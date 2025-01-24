package com.example.testuser1;
import java.lang.String;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ListUserController implements Initializable {
    UserService us=new UserService();
    @javafx.fxml.FXML
    private TableView<User> tableuser;
    @javafx.fxml.FXML
    private Button deleteuser;
    @javafx.fxml.FXML
    private TableColumn<User,String> address;
    @javafx.fxml.FXML
    private TableColumn <User,String>roles;
    @javafx.fxml.FXML
    private TableColumn <User,String>email;
    @javafx.fxml.FXML
    private TableColumn <User,String> username;
    @javafx.fxml.FXML
    private TextField UserNameTF;
    @javafx.fxml.FXML
    private TableColumn question;
    @javafx.fxml.FXML
    private TableColumn Answer;
    @javafx.fxml.FXML
    private Button Tri_btn;
    @javafx.fxml.FXML
    private TableColumn Status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        roles.setCellValueFactory(new PropertyValueFactory<>("roles"));
        question.setCellValueFactory(new PropertyValueFactory<>("question"));
        Answer.setCellValueFactory(new PropertyValueFactory<>("answer"));
        Status.setCellValueFactory(new PropertyValueFactory<>("Status"));

        // Load all users into the TableView initially
        ObservableList<User> originalUserList = FXCollections.observableArrayList(us.getAll());
        tableuser.getItems().addAll(originalUserList);

        // Add a listener to the UserNameTF for dynamic search
        UserNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear previous selection
            tableuser.getSelectionModel().clearSelection();

            if (newValue != null && !newValue.isEmpty()) {
                // Filter the original list based on the entered text
                ObservableList<User> filteredList = originalUserList.filtered(user ->
                        user.getName().toLowerCase().contains(newValue.toLowerCase()));

                // Set the filtered items to the table
                tableuser.setItems(filteredList);
            } else {
                // If the search text is empty, show all original users
                tableuser.setItems(originalUserList);
            }
        });
    }
    private void findAndHighlightUsers(String searchText) {
        tableuser.getSelectionModel().clearSelection();

        ObservableList<User> userList = FXCollections.observableArrayList(tableuser.getItems());

        userList.forEach(user -> {
            if (user.getName().contains(searchText)) {
                tableuser.getSelectionModel().select(user);
            }
        });

        // Sort the items based on the username column in ascending order
        userList.sort(Comparator.comparing(User::getName));

        // Set the sorted items to the table
        tableuser.setItems(userList);
    }


    private List<User> findUsers(String searchText) {
        List<User> matchingUsers = new ArrayList<>();
        for (User user : tableuser.getItems()) {
            // Check if the searchText matches with username or any role
            if (user.getName().contains(searchText) || containsRole(user.getRoles(), searchText)) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }

    private boolean containsRole(String roles, String searchText) {
        String[] roleArray = roles.split(",");
        for (String role : roleArray) {
            if (role.trim().equals(searchText)) {
                return true;
            }
        }
        return false;
    }



    private void selectUsers(List<User> users) {
        // Clear previous selection
        tableuser.getSelectionModel().clearSelection();

        // Select the found users
        for (User user : users) {
            tableuser.getSelectionModel().select(user);
        }

        // Scroll to the first selected row
        if (!users.isEmpty()) {
            tableuser.scrollTo(users.get(0));
        }
    }
    private User findUserByUsername(String username) {
        for (User user : tableuser.getItems()) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }




    @javafx.fxml.FXML
    public void deleteuser(ActionEvent actionEvent) throws SQLException {
        User selectedUser = tableuser.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete the selected user?");
            alert.setContentText("User: " + selectedUser.getName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the user from the TableView and database
                tableuser.getItems().remove(selectedUser);
                us.delete(selectedUser.getEmail()); // Assuming you have a delete method in your UserService
            }
        } else {
            // No user selected, display a message
            showAlert("Please select a user to delete.", "User account has been deactivated.");
        }
    }

    private void showAlert(String message, String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @javafx.fxml.FXML
    public void Tri(ActionEvent actionEvent) {
        // Get the TableColumn for the username
        TableColumn<User, String> usernameColumn = username;

        // Get the current items from the table
        ObservableList<User> userList = FXCollections.observableArrayList(tableuser.getItems());

        // Sort the items based on the username column in ascending order
        userList.sort(Comparator.comparing(user -> usernameColumn.getCellData(user)));

        // Set the sorted items to the table
        tableuser.setItems(userList);
    }


    @javafx.fxml.FXML
    public void Desactivatebtn(ActionEvent actionEvent) {
        User selectedUser = tableuser.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            try {
                // Deactivate the selected user's account
                us.deactivateAccount(selectedUser.getEmail());

                showAlert("Account Deactivated", "User account has been deactivated.");

                tableuser.getItems().clear();
                tableuser.getItems().addAll(us.getAll());
                tableuser.refresh();

            } catch (SQLException e) {
                showAlert("Error", "Error deactivating account: " + e.getMessage());
            }
        } else {
            showAlert("Please select a user to deactivate.", "User account has been deactivated.");
        }
    }

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    @FXML
    public void overview(ActionEvent actionEvent) throws RuntimeException,IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/Stat.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void user(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ListUser.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void affi(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtabliss1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void gestioncertif(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetCertif1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEvent1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void showPartners(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getPartner1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void tutors(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/showTuteur1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    void courses(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/showFormation1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    @FXML
    void GoStidents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetApprenant1.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void GoLevels(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ListUser.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void res(ActionEvent actionEvent) throws RuntimeException,IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowBack.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @FXML
    public void logout(ActionEvent actionEvent) {
        try {
            SessionManager.clearSession();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = fxmlLoader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e) {

            System.out.println("PROBLEM LOAD");
        }
    }

}
