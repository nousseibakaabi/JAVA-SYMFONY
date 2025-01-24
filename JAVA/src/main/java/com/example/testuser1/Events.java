package com.example.testuser1;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import models.Event;
import models.Partner;
import services.EtablissementServices;
import services.EventService;
import services.PartnerService;
import services.serviceReservation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Events implements Initializable {
    private EventService es = new EventService();
    private PartnerService ps = new PartnerService();
    private serviceReservation sr = new serviceReservation();
    private EtablissementServices ests = new EtablissementServices();

    @FXML
    private Label desc_label;

    @FXML
    private Label estab_label;

    @FXML
    private Hyperlink partner_label;

    @FXML
    private Label eventDate_label;

    @FXML
    private ImageView eventIV;

    @FXML
    private Label eventName_label;


    @FXML
    private Label nbrMax_label;

    @FXML
    private Label prix_label;
    private Event event;
    private Image image;

    @FXML
    private ImageView qrIV;

    public Events(){}
    public Events(EventService eventService) {
        this.es = eventService;
    }
    public Events(PartnerService partnerService) {
        this.ps = partnerService;
    }

    public Events(EtablissementServices etablissementServices) {
        this.ests = etablissementServices;
    }

    public void setData(Event event)
    {
        this.event = event;


        eventName_label.setText(event.getNameEvent());
        try {
            partner_label.setText(ps.getNameByID(event.getIdPartner()));
            estab_label.setText(ests.getNameByID(event.getIdEstab()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(event.getDateEvent());
        eventDate_label.setText(formattedDate);

        nbrMax_label.setText(String.valueOf(event.getNbrMax()));
        prix_label.setText(String.valueOf(event.getPrix()));
        desc_label.setText(event.getDescription());

        String path = "File:"+event.getImage();
        image = new Image(path,200,170,false,true);
        eventIV.setImage(image);

        try {
            // Générer le code QR

            if(event != null) {
                String qrContent = null;
                try {
                    qrContent = ests.getAdrByID(event.getIdEstab());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //String cleanedQrContent = qrContent.replaceAll("[^a-zA-Z0-9]", "_");

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, com.google.zxing.BarcodeFormat.QR_CODE, 200, 200);

                // Écrire le code QR dans un fichier
                /*Path pathQR = FileSystems.getDefault().getPath("C:\\Users\\user\\IdeaProjects\\ProjetPIDEV\\src\\main\\resources\\images", cleanedQrContent + ".png");
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", pathQR);*/


                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF));
                Image image = convertToJavaFXImage(bufferedImage);


                qrIV.setImage(image);

            }
            else {
                System.out.println("event est null");
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private Image convertToJavaFXImage(BufferedImage bufferedImage) {
        WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }

        return writableImage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    public void afficherPartner(ActionEvent actionEvent) throws IOException, SQLException {
        Partner selectedPartner = ps.getById(event.getIdPartner());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetPartnerFront.fxml"));
        root = loader.load();
        GetPartnerFront partnerGet = loader.getController();
        partnerGet.setData(selectedPartner);
        scene = new Scene(root);
        primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void book(ActionEvent actionEvent) {
        try {
            // Check if the maximum number of places has been reached for this event
            int totalReservedPlaces = getTotalReservedPlacesForEvent(event.getIdEvent());
            if (totalReservedPlaces >= event.getNbrMax()) {
                // Maximum number of places reached, show a message to the user
                showErrorDialog("This event is fully booked.");
                return;
            }

            // Load the AddReservation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/AddReservation.fxml"));
            Parent root = loader.load();

            // Get the controller associated with AddReservation.fxml
            AddReservation addReservationController = loader.getController();

            // Pass the ID, name, price, and maximum number of places of the event to the AddReservationController
            addReservationController.setEventData(event.getIdEvent(), event.getNameEvent(), event.getPrix(), event.getImage(), event.getNbrMax());

            // Display the AddReservation scene
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getTotalReservedPlacesForEvent(int eventId) {
        return sr.getTotalReservedPlacesForEvent(eventId);
    }


    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}

