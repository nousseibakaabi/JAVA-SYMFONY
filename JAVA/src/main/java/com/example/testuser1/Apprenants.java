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
import models.Apprenant;
import services.ApprenantServices;
import services.NiveauServices;

import java.net.URL;
import java.util.ResourceBundle;

public class Apprenants implements Initializable {
    private  ApprenantServices as = new ApprenantServices();
    private NiveauServices ns = new NiveauServices();
    @FXML
    private Label Training_label;

    @FXML
    private ImageView apprenantIV;

    @FXML
    private AnchorPane apprenant_aff;

    @FXML
    private Label email_label;


    @FXML
    private Label name_label;

    @FXML
    private Label status_label;
    private Apprenant apprenant;
    private Image image;

    public Apprenants(){}
    public Apprenants(ApprenantServices apprenantServices) {
        this.as = apprenantServices;
    }
    public Apprenants(NiveauServices niveauServices) {
        this.ns = niveauServices;
    }

    public void setData(Apprenant apprenant)
    {
        this.apprenant = apprenant;

        name_label.setText(String.valueOf(apprenant.getName()));
        email_label.setText(apprenant.getEmail());




        Training_label.setText(String.valueOf(apprenant.getFormationEtudies()));
        status_label.setText(String.valueOf(apprenant.getStatutNiveau()));




        String path = "File:"+apprenant.getImage();
        image = new Image(path,200,170,false,true);
        apprenantIV.setImage(image);
    }
    private Scene scene;
    private Stage primaryStage;
    private Parent root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
