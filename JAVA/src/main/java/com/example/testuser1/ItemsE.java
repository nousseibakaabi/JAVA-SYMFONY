package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Etablissement;
import services.CertficatServices;
import services.EtablissementServices;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ItemsE {

    @FXML
    private Label Adresse_Etablissementafficher;

    @FXML
    private Label Date_Fondationafficher;

    @FXML
    private Label Nb_Certifafficher;

    @FXML
    private Label Directeur_Etablissementafficher;

    @FXML
    private Label Nom_Etablissafficher;

    @FXML
    private ImageView SchoolIV;

    @FXML
    private Label Tel_Etablissementafficher;

    @FXML
    private Label Type_Etablissementafiicher;

    @FXML
    private AnchorPane school_aff;

    @FXML
    private Button modifierBT;

    @FXML
    private Button supprimerBT;

    private Etablissement etablissement;
    private Image image;

    private EtablissementServices es = new EtablissementServices(); // Initialisation du service
    private CertficatServices cs = new CertficatServices(); // Initialisation du service
    public void initialize() {
        try {
            List<Etablissement> etablissements = es.getAll();

            for (Etablissement pe : etablissements) {
                Adresse_Etablissementafficher.setText(pe.getAdresse_Etablissement());
                Date_Fondationafficher.setText(pe.getDate_Fondation().toString());
                Directeur_Etablissementafficher.setText(pe.getDirecteur_Etablissement());
                Nom_Etablissafficher.setText(pe.getNom_Etablissement());
                Tel_Etablissementafficher.setText(String.valueOf(pe.getTel_Etablissement()));
                Type_Etablissementafiicher.setText(pe.getType_Etablissement());

                File imageFile = new File(pe.getImg_Etablissement());
                Image image = new Image(imageFile.toURI().toString());
                SchoolIV.setImage(image);

                // Afficher le nombre de certificats dans le label Nb_Certifafficher
                Map<String, Integer> nombreCertificatsParEtablissement = es.getNombreCertificatsParEtablissement();
                int nombreCertificats = nombreCertificatsParEtablissement.getOrDefault(pe.getNom_Etablissement(), 0);
                Nb_Certifafficher.setText(String.valueOf(nombreCertificats));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    


    public void setData(Etablissement etablissement, EventHandler<ActionEvent> deleteEventHandler) {
        this.etablissement = etablissement;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(etablissement.getDate_Fondation());

        Date_Fondationafficher.setText(formattedDate);
        Directeur_Etablissementafficher.setText(etablissement.getDirecteur_Etablissement());
        Adresse_Etablissementafficher.setText(etablissement.getAdresse_Etablissement());
        Nom_Etablissafficher.setText(etablissement.getNom_Etablissement());
        Tel_Etablissementafficher.setText(String.valueOf(etablissement.getTel_Etablissement()));
        Type_Etablissementafiicher.setText(etablissement.getType_Etablissement());

        String path = "File:" + etablissement.getImg_Etablissement();
        image = new Image(path, 125, 130, false, true);
        SchoolIV.setImage(image);
        // afficher le nb de certif dans le label
        try {
            Map<String, Integer> nombreCertificatsParEtablissement = es.getNombreCertificatsParEtablissement();
            int nombreCertificats = nombreCertificatsParEtablissement.getOrDefault(etablissement.getNom_Etablissement(), 0);
            Nb_Certifafficher.setText(String.valueOf(nombreCertificats));
        } catch (SQLException e) {
            e.printStackTrace();
            Nb_Certifafficher.setText("N/A");
        }


    }


    @FXML
    private void supprimer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cet établissement ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    es.deleteSchool(etablissement.getID_Etablissement());
                    school_aff.setVisible(false); // Supprime visuellement l'établissement de l'interface utilisateur
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Une erreur s'est produite lors de la suppression de l'établissement.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    private void modifier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/UpdateEtabliss1.fxml"));
            root = loader.load();
            UpdateEtabliss1 controller = loader.getController();
            controller.setSchoolData(etablissement);
            scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Une erreur s'est produite lors du chargement de la vue d'édition.");
            errorAlert.showAndWait();
        }
    }

    public void setSchoolData(Etablissement etablissement) {
        this.etablissement = etablissement;

        // Définir les données de l'établissement dans les composants visuels
        Adresse_Etablissementafficher.setText(etablissement.getAdresse_Etablissement());
        Date_Fondationafficher.setText(etablissement.getDate_Fondation().toString());
        Directeur_Etablissementafficher.setText(etablissement.getDirecteur_Etablissement());
        Nom_Etablissafficher.setText(etablissement.getNom_Etablissement());
        Tel_Etablissementafficher.setText(String.valueOf(etablissement.getTel_Etablissement()));
        Type_Etablissementafiicher.setText(etablissement.getType_Etablissement());

        // Charger l'image de l'établissement
        File imageFile = new File(etablissement.getImg_Etablissement());
        Image image = new Image(imageFile.toURI().toString());
        SchoolIV.setImage(image);

        // Afficher le nombre de certificats dans le label Nb_Certifafficher
        try {
            int nombreCertificats = es.getNombreCertificatsByEtablissement(etablissement.getID_Etablissement());
            Nb_Certifafficher.setText(String.valueOf(nombreCertificats));
        } catch (SQLException e) {
            e.printStackTrace();
            Nb_Certifafficher.setText("N/A");
        }
    }


}
