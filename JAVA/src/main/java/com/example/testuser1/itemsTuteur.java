package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Tuteur;
import services.ServiceTuteur;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class itemsTuteur implements Initializable {

    private ServiceTuteur st = new ServiceTuteur();

    @FXML
    private Label Nom_label;

    @FXML
    private Label tlf_label;

    @FXML
    private Label date_naisc_label;

    @FXML
    private AnchorPane tuteur_aff;

    @FXML
    private Button modifierBT;

    @FXML
    private Label prenom_label;

    @FXML
    private Label profession_label;

    @FXML
    private Label email_label;

    @FXML
    private Button supprimerBT;

    @FXML
    private ImageView tuteurIV;

    @FXML
    private Label tuteur_label;

    private Tuteur tuteur;
    private Image image;

    public itemsTuteur() {
    }

    public itemsTuteur(ServiceTuteur serviceTuteur) {
        this.st = serviceTuteur;
    }

    @FXML
    void modifier(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/updateTuteur.fxml"));
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
    void supprimer(ActionEvent event) throws SQLException {

        st.deleteTuteur(tuteur.getId_tuteur());
        //mise a jour de l'affichage
        showTuteur1 showTuteur1Controller = (showTuteur1) tuteur_aff.getProperties().get("controller");
        showTuteur1Controller.tuteurDisplay();


    }

    public void setData(Tuteur tuteur) {
        this.tuteur = tuteur;

        tuteur_label.setText(String.valueOf(tuteur.getId_tuteur()));
        Nom_label.setText(tuteur.getNom());
        prenom_label.setText(tuteur.getPrenom());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(tuteur.getDate_naisc());
        date_naisc_label.setText(formattedDate);
        tlf_label.setText(String.valueOf(tuteur.getTlf()));
        profession_label.setText(String.valueOf(tuteur.getProfession()));
        email_label.setText(String.valueOf(tuteur.getEmail()));

        String path = "File:" + tuteur.getImage();
        image = new Image(path, 125, 130, false, true);
        tuteurIV.setImage(image);


    }


    private Scene scene;
    private Stage primaryStage;
    private Parent root;


    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}