package com.example.testuser1;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
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
import models.Paiement;
import models.Reservation;
import services.servicePaiement;
import services.serviceReservation;
import utils.MailingService;
import utils.SessionManager;

import java.util.*;

import java.io.IOException;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class AddReservation {
    private final serviceReservation sr = new serviceReservation();
    private final servicePaiement sp = new servicePaiement();

    @FXML
    private TextField CVV;

    @FXML
    private TextField name;


    @FXML
    private DatePicker date;


    @FXML
    private TextField numCard;

    @FXML
    private Label errorCard;

    @FXML
    private Label montant;

    @FXML
    private Label errorName;

    @FXML
    private Label errorCvv;

    @FXML
    private Label errorDate;

    @FXML
    private Label confi;
    @FXML
    private AnchorPane res;

    @FXML
    private Label error;
    @FXML
    private TextField nb_pl;
    private int idUser= SessionManager.getSession().getUser().getId();
    private String nameU= SessionManager.getSession().getUser().getName();

    private String email= SessionManager.getSession().getUser().getEmail();



    private int eventId;
    private String eventName;
    private double eventPrice;
    private String imageSrc;

    private int nbrM;
    @FXML
    private AnchorPane paiement;
    @FXML
    private Button pay;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Hyperlink hyperlink;

    private Events eventsController;

    @FXML
    private TextField CODE;
    @FXML
    private Label   NEWM;
    @FXML
    private Label enjoy;

    public void setEventData(int eventId, String eventName, double eventPrice, String imageSrc, int nbrM ) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventPrice = eventPrice;
        this.imageSrc = imageSrc;
        this.nbrM=nbrM;

    }


    public void initialize() {

        Stripe.apiKey = "sk_test_51Oo7aQLjJljsz2MFpuJDsDFI9BXxos0TFBtXvLTgy0ycVNuHsv7ywF2IoRmrTmglV4OeFtCBeIzjeYsBAghDsULh00jbXIVHgu";
        // Other initialization code...

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            else {
                // Display error message in error label
                error.setText("You should enter a number");

                return null;
            }
        };
        nb_pl.setTextFormatter(new TextFormatter<>(filter));


        UnaryOperator<TextFormatter.Change> filter1 = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            else {
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
        res.setDisable(false);
    }


    @FXML
    public void payer(ActionEvent actionEvent) {
        if (numCard.getText().isEmpty() && CVV.getText().isEmpty() && name.getText().isEmpty() && date.getValue() == null) {
            confi.setText("You should fill all labels");
        } else {
            confi.setText("");

            if (numCard.getText().isEmpty()) {
                errorCard.setText("You should fill the label");
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
                float amount = Float.parseFloat(NEWM.getText());

                Reservation reservation = sr.add(new Reservation(Integer.parseInt(nb_pl.getText())), eventId, eventName, imageSrc,idUser,nameU,email,eventPrice);
                MailingService.SendMail(SessionManager.getSession().getUser().getEmail(),"NOTIFCATION PAIMENT","RESERVATION AVEC  SUCCES \n EVENT : "+ eventName+" Prix : " + amount +"\n " + "Number of places : "+ nb_pl+ "METHODE PAYMENT : PAR CARTE ");

                int reservationId = reservation.getId();
                Paiement paiement = new Paiement(amount);
                sp.add(paiement, amount, reservationId);



                Stripe.apiKey = "sk_test_51Oo7aQLjJljsz2MFpuJDsDFI9BXxos0TFBtXvLTgy0ycVNuHsv7ywF2IoRmrTmglV4OeFtCBeIzjeYsBAghDsULh00jbXIVHgu";

                // Create PaymentIntentCreateParams with desired parameters
                PaymentIntentCreateParams params =
                        PaymentIntentCreateParams.builder()
                                .setAmount((long) amount)
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
                                primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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


        }
    }

    @FXML
    public void pay(ActionEvent actionEvent) {
        if (nb_pl.getText().isEmpty()) {
            error.setText("You should fill the label");
        } else {
            int enteredPlaces = Integer.parseInt(nb_pl.getText());
            int availablePlaces = nbrM; // Assume nbrM holds the total available places
            int reservedPlaces = sr.getTotalReservedPlacesForEvent(eventId); // Get the total reserved places for the event

            int remainingPlaces = availablePlaces - reservedPlaces;

            if (enteredPlaces <= 0) {
                error.setText("You should reserve at least 1 place");
            } else if (enteredPlaces > remainingPlaces) {
                error.setText("The number of places exceeds the available places");
            } else {
                // Calculate the total amount


                // Display confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to make this reservation?");
                confirmationAlert.setTitle("Confirmation");

                ButtonType YESButton = new ButtonType("YES", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                confirmationAlert.getButtonTypes().setAll(YESButton, cancelButton);

                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == YESButton) {
                    float montant_t = (float) (eventPrice * enteredPlaces);
                    montant.setText(String.valueOf(montant_t));
                    error.setVisible(false);
                    res.setDisable(true);

                    String randomCode = sr.getRandomRemiseCode();
                    enjoy.setText(randomCode);
                } else {
                    // User clicked Cancel or closed the dialog, navigate to event
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEventFront1.fxml"));
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

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEventFront1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void showSchools(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtablissFront1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @FXML
    public void event(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEventFront1.fxml"));
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
    public void CODE(ActionEvent actionEvent) {
        String code = CODE.getText();

        // Assuming you have a method in your service class to retrieve the discount percentage based on the code
        double discountPercentage = sr.getDiscountPercentageByCode(code);

        // Calculate the discounted amount
        double amountBeforeDiscount = Double.parseDouble(montant.getText());
        double discountedAmount = amountBeforeDiscount * (1 - discountPercentage / 100);

        // Update the "NEWM" label with the discounted amount
        NEWM.setText(String.valueOf(discountedAmount));
    }
    public void showCourses(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowFormation1Front.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

