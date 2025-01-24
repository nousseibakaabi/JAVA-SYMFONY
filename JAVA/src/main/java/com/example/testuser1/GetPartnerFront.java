package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Partner;
import services.PartnerService;

import java.io.IOException;

public class GetPartnerFront {
    private PartnerService ps = new PartnerService();


    @FXML
    private Label descP_label;

    @FXML
    private TextArea descTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField idPartnerTF;

    @FXML
    private Label nameP_label;

    @FXML
    private TextField namePartnerTF;

    @FXML
    private TextField telTF;

    @FXML
    private Label typeP_label;
    @FXML
    private Label email_label;
    @FXML
    private Label tel_label;

    @FXML
    private ComboBox<String> typePartnerCB;
    private Partner partner;
    @FXML
    private ImageView partnerIV;
    @FXML
    private ImageView partnerIV1;

    private Image image;
    @FXML
    private AnchorPane mainForm;




    public void setData(Partner partner)
    {
        this.partner = partner;

        nameP_label.setText(String.valueOf(partner.getNamePartner()));
        typeP_label.setText(String.valueOf(partner.getTypePartner()));
        descP_label.setText(partner.getDescription());
        email_label.setText(partner.getEmail());
        tel_label.setText(String.valueOf(partner.getTel()));

        String path = "File:"+partner.getImage();
        image = new Image(path,125,130,false,true);
        partnerIV.setImage(image);
    }


    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    /*@FXML
    void retourEvent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/getEvent1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }*/

    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEventFront1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showHome(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEventFront1.fxml"));
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
