package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Paiement;
import models.Reservation;
import services.serviceReservation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import utils.SessionManager;


public class ShowReservation {


    @FXML
    private GridPane grid;

    private final serviceReservation sr = new serviceReservation();
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField searchF;




    private int eventId;

    private double eventPrice;

    private int nbrM;
    @FXML
    private ImageView notFound;



    @FXML
    public void initialize() {
        populateGrid();
        notFound.setVisible(false);
        notFound.setManaged(false);

        searchF.textProperty().addListener((observable, oldValue, newValue) -> {
            String eventName = newValue.trim();
            if (!eventName.isEmpty()) {
                // Perform search and update GridPane with filtered results
                updateGridPane(eventName);
            } else {
                // If the search text is empty, repopulate the GridPane with all reservations
                populateGrid();
            }
        });


    }

    private void updateGridPane(String eventName) {
        List<Reservation> filteredList = filterList(eventName);

        // Clear the existing content of the GridPane
        grid.getChildren().clear();
        if (filteredList.isEmpty()) {
            // Affichez un message indiquant que aucun résultat n'a été trouvé
            notFound.setVisible(true);
            notFound.setManaged(true);
            grid.getChildren().add(notFound);
        } else {
            // Cachez le message "notFound"
            notFound.setVisible(false);
            notFound.setManaged(false);

        // Populate the GridPane with the filtered list of reservations
        int row = 0;
        int col = 0;
        for (Reservation reservation : filteredList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/Card.fxml"));
                Parent root = loader.load();
                Card card = loader.getController();
                card.setData(reservation);

                grid.add(root, col, row);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
    }

    private List<Reservation> filterList(String eventName) {
        List<Reservation> reservations = fetchDataFromDatabase();
        List<Reservation> filteredList = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getNameE().toLowerCase().contains(eventName.toLowerCase())) {
                filteredList.add(reservation);
            }
        }

        return filteredList;
    }


    private void populateGrid() {
        List<Reservation> reservationList = fetchDataFromDatabase();

        int row = 0;
        int col = 0;

        for (Reservation reservation : reservationList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/Card.fxml"));
                Parent root = loader.load();
                Card card = loader.getController();
                card.setRoot(root);
                card.setShowReservationController(this);
                card.setData(reservation);
                grid.add(root, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Reservation> fetchDataFromDatabase() {
        return sr.getCardlist(SessionManager.getSession().getUser().getId());

    }



public void removeGridItem(Card card) {
        grid.getChildren().remove(card.getRoot());

        // Adjust the row and column indices of the remaining nodes
        int rowIndex = GridPane.getRowIndex(card.getRoot());
        int colIndex = GridPane.getColumnIndex(card.getRoot());

        for (Node node : grid.getChildren()) {
        Integer nodeRowIndex = GridPane.getRowIndex(node);
        Integer nodeColIndex = GridPane.getColumnIndex(node);

        if (nodeRowIndex != null && nodeColIndex != null && nodeRowIndex > rowIndex) {
        // Decrement row index of nodes below the removed item
        GridPane.setRowIndex(node, nodeRowIndex - 1);
        }

        if (nodeColIndex != null && nodeRowIndex == rowIndex && nodeColIndex > colIndex) {
        // Decrement column index of nodes in the same row to the right of the removed item
        GridPane.setColumnIndex(node, nodeColIndex - 1);
        }
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


    @FXML
    public void res(ActionEvent actionEvent) {
    }


    @FXML
    public void profile(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/UserDetails.fxml"));
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
    public void showSchools(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtablissFront1.fxml"));
        Scene scene;
        Stage primaryStage;
        Parent root;

        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
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
    public void showCourses(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowFormation1Front.fxml"));
        Scene scene;
        Stage primaryStage;
        Parent root;
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}