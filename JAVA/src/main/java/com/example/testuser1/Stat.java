package com.example.testuser1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Reservation;
import services.CertficatServices;
import services.UserService;
import services.serviceReservation;
import utils.SessionManager;

public class Stat {
    private final serviceReservation rs = new serviceReservation();
    private UserService userService = new UserService();

    @FXML
    private PieChart pie;

    @FXML
    private PieChart rolePieChart;
    @FXML
    private BarChart<String, Number> barChart;
    private CertficatServices cs = new CertficatServices();
    @FXML
    public void initialize() throws SQLException {

            Stat();

        loadPieChartData();

        pie.getData().clear();
pie();
        // Get all reservations

    }
private void pie(){
    List<Reservation> reservations = rs.getCardlist();

    // Create a map to store the total number of places for each event name
    Map<String, Integer> eventPlacesMap = new HashMap<>();

    // Iterate over each reservation to aggregate the number of places for each event name
    for (Reservation reservation : reservations) {
        String eventName = reservation.getNameE();
        int places = reservation.getNb_places();

        // Update the total number of places for the event name
        eventPlacesMap.put(eventName, eventPlacesMap.getOrDefault(eventName, 0) + places);
    }

    // Create an observable list to hold PieChart.Data objects
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    // Iterate over the aggregated event names and places to add data to the pieChartData list
    for (Map.Entry<String, Integer> entry : eventPlacesMap.entrySet()) {
        String eventName = entry.getKey();
        int totalPlaces = entry.getValue();

        // Create PieChart.Data object with the event name as the label and total number of places as the value
        PieChart.Data data = new PieChart.Data(eventName, totalPlaces);

        // Add data to pieChartData list
        pieChartData.add(data);
    }

    // Set the data to the PieChart
    pie.setData(pieChartData);

    // Set the label format to display the actual value of each slice
    for (PieChart.Data data : pieChartData) {
        // Customize the label text to display the value of the slice
        data.setName(data.getName()+"::"+(int)data.getPieValue()); // Append value to the existing label
    }
}
    private void loadPieChartData() {
        // Clear existing data in the rolePieChart
        rolePieChart.getData().clear();

        // Get all user roles counts
        Map<String, Long> roleCounts = userService.getCountsByRoles();

        // Create an observable list to hold PieChart.Data objects
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Iterate over the user roles and counts to add data to the pieChartData list
        for (Map.Entry<String, Long> entry : roleCounts.entrySet()) {
            String roleName = entry.getKey();
            long count = entry.getValue();

            // Create PieChart.Data object with the role name as the label and count as the value
            PieChart.Data data = new PieChart.Data(roleName, count);

            // Add data to pieChartData list
            pieChartData.add(data);
        }

        // Set the data to the rolePieChart
        rolePieChart.setData(pieChartData);

        // Set the label format to display the actual value of each slice
        for (PieChart.Data data : pieChartData) {
            // Customize the label text to display the value of the slice
            data.setName(data.getName() + "::" + (int) data.getPieValue()); // Append value to the existing label
        }
    }

    public void Stat() throws SQLException {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Répartition des Types");

        Map<String, Integer> domaineStats = cs.getDomaineStatistics();

        for (Map.Entry<String, Integer> entry : domaineStats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().addAll(series);
    }



    @FXML
    public void stat(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/chart.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = new Stage();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetNiveau1.fxml"));
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
    public void res(ActionEvent actionEvent)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowBack.fxml"));
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
