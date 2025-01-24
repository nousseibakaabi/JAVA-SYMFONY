package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Paiement;
import models.Reservation;
import services.servicePaiement;
import services.serviceReservation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.SessionManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class ShowBack {

    @FXML
    private GridPane gridB;

    private final serviceReservation sr = new serviceReservation();
    private final servicePaiement sp = new servicePaiement();
    @FXML
    private TextField searchF;
    @FXML
    private ImageView excel;
    @FXML
    private ImageView notFound;


    @FXML

    public void initialize () {


        // Populate the initial GridPane
        populateGridPane();
        notFound.setVisible(false);
        notFound.setManaged(false);

        // Add a listener to the search TextField for real-time search
        searchF.textProperty().addListener((observable, oldValue, newValue) -> {
            String eventName = newValue.trim();
            if (!eventName.isEmpty()) {
                // Perform search and update GridPane with filtered results
                updateGridPane(eventName);
            } else {
                // If the search text is empty, repopulate the GridPane with all reservations
                populateGridPane();
            }
        });
    }

    private void updateGridPane(String eventName) {
        List<Reservation> filteredList = filterList(eventName);

        // Clear the existing content of the GridPane
        gridB.getChildren().clear();

        if (filteredList.isEmpty()) {
            // Affichez un message indiquant que aucun résultat n'a été trouvé
            notFound.setVisible(true);
            notFound.setManaged(true);
            gridB.getChildren().add(notFound);
        } else {
            // Cachez le message "notFound"
            notFound.setVisible(false);
            notFound.setManaged(false);
            // Populate the GridPane with the filtered list of reservations
            int row = 0;
            int col = 0;
            for (Reservation reservation : filteredList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/CardBack.fxml"));
                    Parent root = loader.load();
                    CardBack card = loader.getController();
                    Paiement paiement = sp.getPaiement(reservation.getId());
                    card.setData(reservation, paiement);
                    gridB.add(root, col, row);
                    col++;
                    if (col == 1) {
                        col = 0;
                        row++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void populateGridPane() {
        // Fetch data from your service or database
        List<Reservation> reservationList = fetchDataFromDatabase();

        sr.sortByName(reservationList);

        int row = 0;
        int col = 0;

        for (Reservation reservation : reservationList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/CardBack.fxml"));

                Parent root = loader.load();

                CardBack card = loader.getController();
                Paiement paiement = sp.getPaiement(reservation.getId());

                card.setData(reservation, paiement);

                gridB.add(root, col, row);

                col++;
                if (col == 1) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Reservation> fetchDataFromDatabase() {
        return sr.getlistBack();
    }

    @FXML
    public void showEvents(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEvent1.fxml"));
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
    public void resBack(ActionEvent actionEvent) {
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
    public void showPartners(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getPartner1.fxml"));
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
    public void stat(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/Stat.fxml"));
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
    public void gestioncertif(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetCertif1.fxml"));
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
    public void excel(ActionEvent actionEvent) {
        List<Reservation> reservations = fetchDataFromDatabase();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reservations");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Email");
            headerRow.createCell(2).setCellValue("Number of Places");
            headerRow.createCell(3).setCellValue("Event Name");
            headerRow.createCell(4).setCellValue("Montant");
            headerRow.createCell(5).setCellValue("Heure");

            // Populate data rows
            int rowNum = 1;
            for (Reservation reservation : reservations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(reservation.getName());
                row.createCell(1).setCellValue(reservation.getEmail());
                row.createCell(2).setCellValue(reservation.getNb_places());
                row.createCell(3).setCellValue(reservation.getNameE());

                // Fetch paiement for the reservation
                Paiement paiement = sp.getPaiement(reservation.getId());
                if (paiement != null) {
                    row.createCell(4).setCellValue(paiement.getMontant_t());
                    row.createCell(5).setCellValue(paiement.getHeure_P());
                }
            }

            // Write the Excel file
            String excelFilePath = "Reservations.xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Excel file generated successfully!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while generating Excel file!");
            alert.showAndWait();
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

    @FXML
    public void remises(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/REMISE.fxml"));
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
}

