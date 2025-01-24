package com.example.testuser1;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Reservation;
import services.servicePaiement;
import services.serviceReservation;
import utils.MailingService;
import utils.SessionManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class Paiement2 {

    private Card cardController; // Reference to Card controller
    private int idU= SessionManager.getSession().getUser().getId();

    private int reservationId;
    private int place;

    private double montantValue;
    private double newA;

    @FXML
    private AnchorPane anchor;

    @FXML
    private AnchorPane paiement;


    @FXML
    private TextField numCard;

    @FXML
    private Label errorCard;

    @FXML
    private Label montant;

    @FXML
    private TextField CVV;

    @FXML
    private Label errorCvv;

    @FXML
    private TextField name;

    @FXML
    private Label errorName;

    @FXML
    private DatePicker date;

    @FXML
    private Label errorDate;

    @FXML
    private Label confi;

private String nameE;
    private final serviceReservation sr = new serviceReservation();
    private final servicePaiement sp = new servicePaiement();
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private Button payer;

    public void setData(int place, int reservationId, double amount , double newA , String nameE) {
        this.reservationId = reservationId;
        this.place = place;
        this.montantValue = amount;
        montant.setText(String.valueOf(amount));
        this.newA=newA;
        this.nameE=nameE;
    }
    @FXML
    public void initialize(){
        UnaryOperator<TextFormatter.Change> filter1 = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            else {
                // Display error message in error label
                errorCard.setText("You should enter a number");

                return null;
            }
        };

        numCard.setTextFormatter(new TextFormatter<>(filter1));

        UnaryOperator<TextFormatter.Change> filter2 = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            else {
                // Display error message in error label
                errorCvv.setText("You should enter a number");

                return null;
            }
        };

        CVV.setTextFormatter(new TextFormatter<>(filter2));

        UnaryOperator<TextFormatter.Change> Filter3 = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]*")) { // Allow alphabetic characters and spaces
                return change;
            } else {
                // Display error message if the input contains invalid characters
                errorName.setText("Only alphabetic characters and spaces are allowed");
                return null;
            }
        };

        name.setTextFormatter(new TextFormatter<>(Filter3));


    }

    @FXML
    void payer(ActionEvent event) {


        if (numCard.getText().isEmpty() && CVV.getText().isEmpty() && name.getText().isEmpty() && date.getValue() == null) {
            confi.setText("You should fill all labels");
        } else {
            confi.setText("");

            // Handle other validations for individual fields
            if (numCard.getText().isEmpty()) {
                errorCard.setText("You should fill the label");
                // Reset other error messages
                errorCvv.setText("");
                errorName.setText("");
                errorDate.setText("");
            } else if (CVV.getText().isEmpty()) {
                errorCvv.setText("You should fill the label");
                errorName.setText("");
                errorDate.setText("");
                errorCard.setText("");
            } else if (name.getText().isEmpty()) {
                errorName.setText("You should fill the label");
                errorCvv.setText("");
                errorDate.setText("");
                errorCard.setText("");
            } else if (date.getValue() == null) {
                errorDate.setText("You should fill the label");
                errorCvv.setText("");
                errorName.setText("");
                errorCard.setText("");
            } else {

                    Reservation updatedReservation = new Reservation();
                    updatedReservation.setId(reservationId);
                    updatedReservation.setNb_places(Math.abs(place)); // Absolute value of difference
                    sr.update(updatedReservation);
                    sp.updatePaiement(reservationId, newA);
                LocalDateTime localDateTime = LocalDateTime.now();
                java.sql.Date time = java.sql.Date.valueOf(localDateTime.toLocalDate());
                    sp.updateHeure(reservationId, time);

                    Stripe.apiKey = "sk_test_51Oo7aQLjJljsz2MFpuJDsDFI9BXxos0TFBtXvLTgy0ycVNuHsv7ywF2IoRmrTmglV4OeFtCBeIzjeYsBAghDsULh00jbXIVHgu";

                    // Create PaymentIntentCreateParams with desired parameters
                    PaymentIntentCreateParams params =
                            PaymentIntentCreateParams.builder()
                                    .setAmount((long) montantValue)
                                    .setCurrency("gbp")
                                    .setPaymentMethod("pm_card_visa")
                                    .setConfirm(true)
                                    .putExtraParam("automatic_payment_methods[enabled]", true)
                                    .putExtraParam("automatic_payment_methods[allow_redirects]", "never")
                                    .build();

                    try {
                        // Create the PaymentIntent
                        PaymentIntent paymentIntent = PaymentIntent.create(params);

                        // Confirm the PaymentIntent only if it's not already confirmed
                        if (!"succeeded".equals(paymentIntent.getStatus())) {
                            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                                    .setPaymentMethod(paymentIntent.getPaymentMethod())
                                    .build();

                            PaymentIntent confirmedIntent = paymentIntent.confirm(confirmParams);

                            // Handle successful confirmation within your application

                            // Add your logic here to handle the payment confirmation within your application
                            // For example, you can display a success message or update the UI accordingly
                        } else {
                            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationAlert.setHeaderText(null);
                            confirmationAlert.setContentText("Payment confirmed successfully.");
                            confirmationAlert.setTitle("Confirmation");
                            MailingService.SendMail(SessionManager.getSession().getUser().getEmail(),"NOTIFICATION PAYMENT"," RESERVATION UPDATED SUCCESSFULLY \n EVENT : "+ nameE+" Price : " + newA +"\n " + "Number of places : "+ place+ "METHODE PAYMENT : PAR CARTE ");

                            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                            confirmationAlert.getButtonTypes().setAll(okButton);

                            Optional<ButtonType> result = confirmationAlert.showAndWait();

                            if (result.isPresent() && result.get() == okButton) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowReservation.fxml"));
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
                        }
                    } catch (StripeException e) {
                        // Handle error
                        e.printStackTrace();
                        System.out.println("Error confirming PaymentIntent: " + e.getMessage());
                    }
                }
                }}




    public void setCardController(Card card) {
        this.cardController = card;

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
        try{
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
}
