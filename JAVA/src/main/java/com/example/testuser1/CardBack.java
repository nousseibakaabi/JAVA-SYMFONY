package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.Paiement;
import models.Reservation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import utils.SessionManager;

import java.io.IOException;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CardBack implements Initializable {


    @FXML
    private Label nameE;

    @FXML
    private Label email;

    @FXML
    private Label nb;

    @FXML
    private Label username;



    private Reservation reservation;

    @FXML
    private Label montant;
    @FXML
    private Label heure;
    @FXML
    private AnchorPane cardBack;
    @FXML
    private ImageView p;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }

    public void setData(Reservation reservation, Paiement paiement)
       {this.reservation = reservation;
        nameE.setText(reservation.getNameE());
         nb.setText(String.valueOf(reservation.getNb_places()));
         email.setText(reservation.getEmail());
        username.setText(reservation.getName());
         montant.setText(String.valueOf(paiement.getMontant_t()));
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
           String dateStr = formatter.format(paiement.getHeure_P().toLocalDate());
           heure.setText(dateStr);

           /*if (paiement != null) {
               montant.setText(String.valueOf(paiement.getMontant_t()));
               heure.setText(paiement.getHeure_P());
           } else {
               // Handle the case where paiement is null, e.g., set default values or show an error message.
               montant.setText("N/A");
               heure.setText("N/A");
           }*/


    }

    @FXML
    public void pdf(ActionEvent actionEvent) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            float y = 700; // Starting Y-coordinate

            // Reservation Details (Red and Centered)
            contentStream.setNonStrokingColor(255, 0, 0); // Red color
            String text = "Reservation Details:";
            float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000f * 12;
            float startX = (page.getMediaBox().getWidth() - textWidth) / 2; // Centered
            contentStream.newLineAtOffset(startX, y);
            contentStream.showText(text);
            contentStream.newLineAtOffset(-startX, 0); // Adjust for centering
            y -= 20; // Move to the next line
            contentStream.newLineAtOffset(0, -20);

            // Reset color to default (black)
            contentStream.setNonStrokingColor(0, 0, 0);

            // Name
            contentStream.showText("Name: " + username.getText());
            y -= 20; // Move to the next line
            contentStream.newLineAtOffset(0, -20);

            // Email
            contentStream.showText("Email: " + email.getText());
            y -= 20; // Move to the next line
            contentStream.newLineAtOffset(0, -20);

            // Number of Places
            contentStream.showText("Number of Places: " + nb.getText());
            y -= 20; // Move to the next line
            contentStream.newLineAtOffset(0, -20);

            // Event Name
            contentStream.showText("Event Name: " + nameE.getText());
            y -= 20; // Move to the next line
            contentStream.newLineAtOffset(0, -20);

            // Montant
            contentStream.showText("Montant: " + montant.getText());
            y -= 20; // Move to the next line
            contentStream.newLineAtOffset(0, -20);

            // Heure
            contentStream.showText("Heure: " + heure.getText());
            y -= 20; // Move to the next line

            contentStream.endText();
            contentStream.close();

            document.save("Reservation_Details.pdf");
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("PDF file generated successfully!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while generating PDF file!");
            alert.showAndWait();
        }
    }





}





