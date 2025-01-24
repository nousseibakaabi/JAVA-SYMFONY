package com.example.testuser1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Tuteur;
import services.ServiceTuteur;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Tuteurs implements Initializable {

    private ServiceTuteur st = new ServiceTuteur();

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    @FXML
    private Label Nom_label;

    @FXML
    private Label date_naisc_label;

    @FXML
    private Label email_label;

    @FXML
    private AnchorPane tuteur_aff;

    @FXML
    private Label prenom_label;

    @FXML
    private Label profession_label;

    @FXML
    private Label tlf_label;

    @FXML
    private ImageView tuteurIV;

    private Tuteur tuteur;
    private Image image;

    public Tuteurs(){}
    public Tuteurs(ServiceTuteur serviceTuteur) {
        this.st = serviceTuteur;
    }

    public void setData(Tuteur tuteur)
    {
        this.tuteur = tuteur;

        Nom_label.setText(tuteur.getNom());
        prenom_label.setText(tuteur.getPrenom());
        tlf_label.setText(String.valueOf(tuteur.getTlf()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(tuteur.getDate_naisc());
        date_naisc_label.setText(formattedDate);

        profession_label.setText(String.valueOf(tuteur.getProfession()));
        email_label.setText(String.valueOf(tuteur.getEmail()));

        String path = "File:" + tuteur.getImage();
        image = new Image(path, 125, 130, false, true);
        tuteurIV.setImage(image);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    public void setRoot(Parent root) {
        this.root = root;
    }

    public  Parent getRoot() {
        return root;
    }



}
