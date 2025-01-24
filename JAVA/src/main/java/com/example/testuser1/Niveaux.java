package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Niveau;
import services.NiveauServices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Niveaux implements Initializable {
    private NiveauServices ns = new NiveauServices();
    @FXML
    private Label certificatLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Label nameNLabel;

    @FXML
    private Label nbformationLabel;

    @FXML
    private ImageView niveauIV;

    @FXML
    private AnchorPane niveau_aff;

    @FXML
    private Label preNLabel;
    private Niveau niveau;
    private Image image;

    public Niveaux(){}
    public Niveaux(NiveauServices niveauServices) {
        this.ns = niveauServices;
    }
    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    public void setData(Niveau niveau)
    {
        this.niveau = niveau;


        nameNLabel.setText(niveau.getName());
        preNLabel.setText(niveau.getPrerequis());
        durationLabel.setText(niveau.getDuree());
        nbformationLabel.setText(String.valueOf(niveau.getNbformation()));
        certificatLabel.setText(String.valueOf(niveau.getCertificat()));
        descLabel.setText(niveau.getDescription());

        String path = "File:"+niveau.getImage();
        image = new Image(path,323,270,false,true);
        niveauIV.setImage(image);
    }

    @FXML
    void deleteNiveau(ActionEvent actionEvent) throws SQLException {
        ns.delete(niveau.getId());
        //mise a jour de l'affichage
        GetNiveau1 getNiveau1Controller = (GetNiveau1) niveau_aff.getProperties().get("com.example.testuser1");
        getNiveau1Controller.niveauDisplay();

    }
    @FXML
    void editNiveau(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("updateNiveau1.fxml"));
        root = loader.load();
        UpdateNiveau1 updateNiveau1Controller = loader.getController();
       updateNiveau1Controller.setNiveauData(niveau);
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //public void importer(ActionEvent actionEvent) {
    //}
}
