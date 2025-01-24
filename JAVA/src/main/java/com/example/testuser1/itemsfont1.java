package com.example.testuser1;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.EtablissementServices;
import models.Etablissement;
import services.CertficatServices;
import models.Certificat;

public class itemsfont1 {

    @FXML
    private Label Date_Obtentionafficher;

    @FXML
    private Label Domaineafficher;

    @FXML
    private Hyperlink ID_Etablissementafficher;

    @FXML
    private Label Niveauafiicher;

    @FXML
    private Label Nom_Certificatafficher;



    @FXML
    private AnchorPane school_aff;



    private Certificat certificat;
    private CertficatServices cs = new CertficatServices();

    public void initialize() {
        try {
            List<Certificat> certificats = cs.getAll();

            if (!certificats.isEmpty()) {
                certificat = certificats.get(0);
                Domaineafficher.setText(certificat.getDomaine_Certificat());
                Date_Obtentionafficher.setText(certificat.getDate_Obtention_Certificat().toString());
                Nom_Certificatafficher.setText(certificat.getNom_Certificat());
                Niveauafiicher.setText(certificat.getNiveau());
                ID_Etablissementafficher.setText(String.valueOf(certificat.getID_Etablissement()));
            }

            ID_Etablissementafficher.setOnMouseClicked(event -> {
                afficherInfosEtablissement(certificat.getID_Etablissement());
            });
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }




    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer ce certificat ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    cs.deleteCertificate(certificat.getID_Certificat());
                    school_aff.setVisible(false); // Supprime visuellement le certificat de l'interface utilisateur
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Une erreur s'est produite lors de la suppression du certificat.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public void setData(Certificat certificat, EventHandler<ActionEvent> deleteEventHandler) {
        this.certificat = certificat;
        Nom_Certificatafficher.setText(certificat.getNom_Certificat());
        Domaineafficher.setText(certificat.getDomaine_Certificat());
        Niveauafiicher.setText(certificat.getNiveau());
        Date_Obtentionafficher.setText(certificat.getDate_Obtention_Certificat().toString());

        int etablissementId = certificat.getID_Etablissement();        // récupérer l'ID etabliss depuis  certif

        try {
            // récupérer le nom de l'établissement correspondant à l'ID
            String nomEtablissement = cs.getEtablissementName(etablissementId);
            // afficher le nom etabliss dans la vue
            ID_Etablissementafficher.setText(nomEtablissement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void afficherInfosEtablissement(int etablissementId) {
        try {
            // Récupérer l'objet Etablissement à partir de son ID
            EtablissementServices etablissementServices = new EtablissementServices();
            Etablissement etablissement = etablissementServices.getById(etablissementId);

            if (etablissement != null) {
                // Si l'établissement est trouvé, afficher ses informations dans l'alerte
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informations sur l'établissement");
                alert.setHeaderText(null);
                alert.setContentText("Nom: " + etablissement.getNom_Etablissement() + "\n" +
                        "Directeur: " + etablissement.getDirecteur_Etablissement() + "\n" +
                        "Date de fondation: " + etablissement.getDate_Fondation() + "\n" +
                        "Type: " + etablissement.getType_Etablissement() + "\n" +
                        "Téléphone: " + etablissement.getTel_Etablissement());

                alert.showAndWait();
            } else {
                // Si l'établissement n'est pas trouvé, afficher un message d'erreur
                afficherAlerteErreur("Erreur", "L'établissement correspondant à l'ID " + etablissementId + " n'a pas été trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerteErreur("Erreur", "Impossible de récupérer les informations sur l'établissement.");
        }
    }

    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setCertificateData(Certificat certificat) {
        this.certificat = certificat;
        Nom_Certificatafficher.setText(certificat.getNom_Certificat());
        Domaineafficher.setText(certificat.getDomaine_Certificat());
        Niveauafiicher.setText(certificat.getNiveau());
        Date_Obtentionafficher.setText(certificat.getDate_Obtention_Certificat().toString());

        int etablissementId = certificat.getID_Etablissement();

        try {
            String nomEtablissement = cs.getEtablissementName(etablissementId);
            ID_Etablissementafficher.setText(nomEtablissement);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerteErreur("Erreur", "Erreur lors de la récupération du nom de l'établissement.");
        }


    }


}

