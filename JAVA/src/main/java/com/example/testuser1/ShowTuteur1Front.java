package com.example.testuser1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Tuteur;
import services.ServiceTuteur;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ShowTuteur1Front {
    private final ServiceTuteur st = new ServiceTuteur();


    @FXML
    private AnchorPane event_AP;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane event_scrollPane;
    private Tuteur tuteur;

    private ObservableList<Tuteur> tuteursList;

    @FXML
    private Label Nom_label;

    @FXML
    private Label date_naisc_label;

    @FXML
    private Label email_label;


    @FXML
    private Label prenom_label;

    @FXML
    private Label profession_label;

    @FXML
    private Label tlf_label;

    @FXML
    private ImageView tuteurIV;
    private Image image;



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

    private Scene scene;
    private Stage primaryStage;
    private Parent root;
    public void showHome(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetFront.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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

    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEventFront1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
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
