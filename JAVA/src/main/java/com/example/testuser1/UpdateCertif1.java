package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Certificat;
import services.CertficatServices;
import services.EtablissementServices;
import utils.SessionManager;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UpdateCertif1 {

    private final CertficatServices cs = new CertficatServices();
    private final EtablissementServices es = new EtablissementServices();

    @FXML
    private DatePicker Date_Obtention_Certificatmodif;

    @FXML
    private ChoiceBox<String> Domaine_Certificatmodif;

    @FXML
    private TextField ID_Certificatmodif;

    @FXML
    private ChoiceBox<String> ID_Etablissementmodif;

    @FXML
    private ChoiceBox<String> Niveaumodif;

    @FXML
    private TextField Nom_Certificatmodif;
    @FXML
    private Label dateObtentionErrorLabel;

    @FXML
    private Label nomCertificatErrorLabel;

    public void initialize() {
        // Charger les noms des établissements dans le ChoiceBox
        try {
            List<String> nomsEtablissements = es.getNoms();
            ID_Etablissementmodif.setItems(FXCollections.observableArrayList(nomsEtablissements));
        } catch (SQLException e) {
            afficherAlerteErreur("Erreur SQL", "Une erreur est survenue lors du chargement des établissements.");
            e.printStackTrace();
        }
        // Écouteur pour le champ Nom_Certificat
        Nom_Certificatmodif.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidString(newValue)) {
                afficherErreurNomCertificat("Veuillez entrer un nom valide pour le certificat (sans chiffres).");
            } else {
                nomCertificatErrorLabel.setVisible(false);
            }
        });

        // Écouteur pour le DatePicker Date_Obtention_Certificat
        Date_Obtention_Certificatmodif.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isAfter(LocalDate.now())) {
                afficherErreurDateObtention("La date d'obtention ne peut pas être postérieure à aujourd'hui.");
            } else {
                dateObtentionErrorLabel.setVisible(false);
            }
        });
    }

    public void setCertificateData(Certificat certificate) throws SQLException {
        ID_Certificatmodif.setText(String.valueOf(certificate.getID_Certificat()));
        ID_Etablissementmodif.setValue(cs.getEtablissementName(certificate.getID_Etablissement()));
        Nom_Certificatmodif.setText(certificate.getNom_Certificat());
        Domaine_Certificatmodif.setValue(certificate.getDomaine_Certificat());
        Niveaumodif.setValue(certificate.getNiveau());
        Date_Obtention_Certificatmodif.setValue(certificate.getDate_Obtention_Certificat().toLocalDate());
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            if (Nom_Certificatmodif.getText().isEmpty() || Domaine_Certificatmodif.getValue() == null || Niveaumodif.getValue() == null || Date_Obtention_Certificatmodif.getValue() == null || ID_Etablissementmodif.getValue() == null || ID_Certificatmodif.getText().isEmpty()) {
                afficherAlerteErreur("Champs manquants", "Veuillez remplir tous les champs pour modifier le certificat.");
                return;
            }

            Date dateObtention = Date.valueOf(Date_Obtention_Certificatmodif.getValue());

            if (!isValidString(Nom_Certificatmodif.getText())) {
                afficherErreur(nomCertificatErrorLabel, "Veuillez entrer un nom valide pour le certificat (sans chiffres).");
                return;
            } else {
                clearError(nomCertificatErrorLabel);
            }

            if (!isValidDate(dateObtention)) {
                afficherErreur(dateObtentionErrorLabel, "La date d'obtention doit être antérieure ou égale à aujourd'hui.");
                return;
            } else {
                clearError(dateObtentionErrorLabel);
            }

            int idCertificat = Integer.parseInt(ID_Certificatmodif.getText());            // covertir de id certif en int

            // creer un certificat avec les valeurs des champs de modification
            Certificat updatedCertif = new Certificat(
                    idCertificat,
                    Nom_Certificatmodif.getText(),
                    Domaine_Certificatmodif.getValue(),
                    Niveaumodif.getValue(),
                    dateObtention,
                    es.getIDByNom(ID_Etablissementmodif.getValue())
            );

            cs.updateCertificate(updatedCertif, idCertificat);
            afficherSuccess(nomCertificatErrorLabel, "Le certificat a été mis à jour avec succès.");
            afficherSuccess(dateObtentionErrorLabel, "");
            afficherAlerteInformation("Mise à jour réussie", "Le Certificat a été mis à jour avec succès.");

        } catch (NumberFormatException e) {
            afficherErreur(nomCertificatErrorLabel, "Veuillez entrer des valeurs valides.");
            e.printStackTrace();
        } catch (SQLException e) {
            afficherErreur(nomCertificatErrorLabel, "Une erreur SQL est survenue. Veuillez réessayer.");
            e.printStackTrace();
        } catch (Exception e) {
            afficherErreur(nomCertificatErrorLabel, "Une erreur est survenue lors de la mise à jour. Veuillez réessayer.");
            e.printStackTrace();
        }
    }


    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidString(String str) {

        return str.matches("[a-zA-Z\\s]+");
    }

    private boolean isValidDate(Date date) {

        return !date.toLocalDate().isAfter(LocalDate.now());
    }

    private void afficherErreur(Label label, String message) {
        label.setTextFill(javafx.scene.paint.Color.RED);
        label.setText(message);
    }

    private void afficherSuccess(Label label, String message) {
        label.setTextFill(javafx.scene.paint.Color.GREEN);
        label.setText(message);
    }
    private void clearError(Label label) {
        label.setText("");
    }
    @FXML
    private void afficherErreurNomCertificat(String message) {
        nomCertificatErrorLabel.setText(message);
        nomCertificatErrorLabel.setVisible(true);
    }

    @FXML
    private void afficherErreurDateObtention(String message) {
        dateObtentionErrorLabel.setText(message);
        dateObtentionErrorLabel.setVisible(true);
    }
    private void afficherAlerteInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void clear(ActionEvent event) {
        Nom_Certificatmodif.setText("");
        Domaine_Certificatmodif.setValue("choose a domain");
        ID_Etablissementmodif.setValue("choose an establishment");
        Niveaumodif.setValue("choose a level");
        Date_Obtention_Certificatmodif.setValue(null);


    }


    private Scene scene;
    private Stage primaryStage;
    private Parent root;


    public void showPartners(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getPartner1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEvent1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void affi(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtabliss1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void gestioncertif(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetCertif1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void res(ActionEvent actionEvent) throws RuntimeException,IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowBack.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void overview(ActionEvent actionEvent) throws RuntimeException,IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/Stat.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void logout(ActionEvent actionEvent) {
        try {
            SessionManager.clearSession();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = fxmlLoader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e) {

            System.out.println("PROBLEM LOAD");
        }
    }
    @FXML
    public void user(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ListUser.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
