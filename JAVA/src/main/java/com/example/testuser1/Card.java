package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import models.Reservation;
import services.servicePaiement;
import services.serviceReservation;
import utils.MailingService;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class Card implements Initializable {

    @FXML
    private Label nameEvent;

    @FXML
    private ImageView image;

    @FXML
    private Button apply;

    @FXML
    private Spinner<Integer> nb_pl;

    private serviceReservation rs = new serviceReservation();
    private servicePaiement sp = new servicePaiement();

    private ShowReservation showReservationController;
    private Reservation reservation;
    private int reservationId;
    private int originalNumberOfPlaces;
    private int currentNumberOfPlaces;
    private double eventPrice;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apply.setVisible(false);

        nb_pl.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentNumberOfPlaces = newValue;
            apply.setVisible(currentNumberOfPlaces != originalNumberOfPlaces);
        });
    }

    public void setData(Reservation reservation) {
        this.reservation = reservation;
        this.reservationId = reservation.getId();
        Double eventPriceObject = reservation.getEventPrice(); // Retrieve eventPrice as a Double object

        if (eventPriceObject != null) {
            this.eventPrice = eventPriceObject.doubleValue(); // Convert to primitive double
        } else {
            // Handle the case where eventPrice is null, perhaps by assigning a default value or showing an error message
            // For example:
            this.eventPrice = 0.0; // Assign a default value
            System.err.println("Event price is null for reservation ID: " + reservationId);
        }

        nameEvent.setText(reservation.getNameE());

        String path = "File:"+reservation.getImageSrc();
        Image im = new Image(path, 200, 170, false, true);
        image.setImage(im);

        originalNumberOfPlaces = reservation.getNb_places();

        if (nb_pl.getValueFactory() != null) {
            nb_pl.getValueFactory().setValue(originalNumberOfPlaces);
        } else {
            nb_pl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, originalNumberOfPlaces));
        }
    }



    public void setShowReservationController(ShowReservation controller) {
        this.showReservationController = controller;
    }

    @FXML
    public void apply(ActionEvent actionEvent) {

        double a = originalNumberOfPlaces * eventPrice;
        int difference = currentNumberOfPlaces - originalNumberOfPlaces;
        double amount = difference * eventPrice;// Calculate amount using eventPrice
        int place = originalNumberOfPlaces + difference;
        double newA = amount + a;

        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(reservationId);
        updatedReservation.setNb_places(Math.abs(place)); // Absolute value of difference
        if (difference <= 0) {
            rs.update(updatedReservation);
            sp.updatePaiement(reservationId, newA);
            LocalDateTime localDateTime = LocalDateTime.now();
            java.sql.Date time = java.sql.Date.valueOf(localDateTime.toLocalDate());
            sp.updateHeure(reservationId, time);
            MailingService.SendMail(SessionManager.getSession().getUser().getEmail(),"NOTIFICATION PAYMENT"," RESERVATION UPDATED SUCCESSFULLY \n EVENT : "+ nameEvent+" Price : " + newA +"\n " + "Number of places : "+ place+ "METHODE PAYMENT : PAR CARTE ");


        } else {

            try {
                FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/com/example/testuser1/Paiement2.fxml"));
                Parent root = loader.load();
                Paiement2 paiementController = loader.getController();
                paiementController.setCardController(this); // Pass reference to the Card controller
                paiementController.setData(Math.abs(place), reservationId, amount, newA, nameEvent.getText());

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Error occurred while loading Paiement2.fxml: " + e.getMessage());
            }
        }

    }
    @FXML
    public void delete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Reservation");
        alert.setContentText("Are you sure you want to delete this reservation?");

        ButtonType yesButton = new ButtonType("YES", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(yesButton, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            try {
                // Delete the reservation
                rs.delete(reservationId);

                // Remove the grid item
                if (showReservationController != null) {
                    showReservationController.removeGridItem(this);
                }

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Delete Reservation successful.");
                successAlert.showAndWait();
            } catch (Exception e) {
                System.err.println("Error occurred while deleting reservation: " + e.getMessage());
            }
        }
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public Parent getRoot() {
        return root;
    }
}
