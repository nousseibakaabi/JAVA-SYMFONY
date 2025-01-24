package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Etablissement;
import services.EtablissementServices;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddEtabliss1 {

    private final EtablissementServices es = new EtablissementServices();

    @FXML
    private TextField Adresse_Etablissement;

    @FXML
    private DatePicker Date_Fondation;

    @FXML
    private TextField Directeur_Etablissement;

    @FXML
    private Label addressLabelError;

    @FXML
    private Label datefondLabelError;

    @FXML
    private Label directorLabelError;

    @FXML
    private Label nomLabelError;

    @FXML
    private Label telLabelError;
    @FXML
    private ChoiceBox<String> typeEtablissement1;

    @FXML
    private TextField Nom_Etablissement;

    @FXML
    private TextField Tel_Etablissement;
    @FXML
    private Button importBT;

    @FXML
    private ImageView importIV;

    @FXML
    private AnchorPane mainForm;
    private Image image;

    @FXML
    private void initialize() {
        // Écouteur pour le champ Nom_Etablissement
        Nom_Etablissement.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidString(newValue)) {
                afficherErreur(nomLabelError, "Veuillez entrer un nom valide pour l'établissement (sans chiffres).");
            } else {
                clearError(nomLabelError);
            }
        });

        // Écouteur pour le DatePicker Date_Fondation
        Date_Fondation.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isAfter(LocalDate.now())) {
                afficherErreur(datefondLabelError, "La date de fondation ne peut pas être postérieure à aujourd'hui.");
            } else {
                clearError(datefondLabelError);
            }
        });
        // Écouteur pour le champ Directeur_Etablissement
        Directeur_Etablissement.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidString(newValue)) {
                afficherErreur(directorLabelError, "Veuillez entrer un nom de directeur valide pour l'établissement (sans chiffres).");
            } else {
                clearError(directorLabelError);
            }
        });

        // Écouteur pour le champ Tel_Etablissement
        Tel_Etablissement.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidPhoneNumber(newValue)) {
                afficherErreur(telLabelError, "Veuillez saisir un numéro de téléphone valide composé de 8 chiffres.");
            } else {
                clearError(telLabelError);
            }
        });


    }



    @FXML
    void ajouter(ActionEvent event) {
        try {
            Date dateFondation = Date.valueOf(Date_Fondation.getValue());

            String typeEtablissement = typeEtablissement1.getValue();

            // Vérification des champs obligatoires
            if (Nom_Etablissement.getText().isEmpty() || Adresse_Etablissement.getText().isEmpty() || typeEtablissement == null ||
                    Tel_Etablissement.getText().isEmpty() || Directeur_Etablissement.getText().isEmpty() || Date_Fondation.getValue() == null) {
                afficherAlerteErreur("Champs manquants", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            if (!isValidString(Nom_Etablissement.getText())) {
                afficherAlerteErreur("Erreur de saisie", "Veuillez entrer un nom valide pour l'établissement (sans chiffres).");
                return;
            }



            if (!isValidString(typeEtablissement)) {
                afficherAlerteErreur("Erreur de saisie", "Veuillez entrer un type valide pour l'établissement (sans chiffres).");
                return;
            }

            if (!isValidPhoneNumber(Tel_Etablissement.getText())) {
                return;
            }

            if (!isValidString(Directeur_Etablissement.getText())) {
                afficherAlerteErreur("Erreur de saisie", "Veuillez entrer un nom de directeur valide pour l'établissement (sans chiffres).");
                return;
            }

            if (!isValidDate(dateFondation)) {
                afficherAlerteErreur("Erreur de saisie", "La date de fondation ne peut pas être ultérieure à aujourd'hui.");
                return;
            }

            if (!es.isUniqueEtablissement(Nom_Etablissement.getText(), Adresse_Etablissement.getText(), typeEtablissement, Integer.parseInt(Tel_Etablissement.getText()), Directeur_Etablissement.getText(), dateFondation)) {
                afficherAlerteErreur("Erreur d'ajout", "Un établissement avec les mêmes données existe déjà.");
                return;
            }

// Ajouter l'établissement
            es.addSchool(new Etablissement(
                    // Remplacez les arguments suivants par les valeurs correspondantes à partir de vos champs
                    data.pathE,
                    Nom_Etablissement.getText(),
                    Adresse_Etablissement.getText(),
                    typeEtablissement,
                    Integer.parseInt(Tel_Etablissement.getText()),
                    Directeur_Etablissement.getText(),
                    dateFondation
            ));
            afficherAlerteInformation("Ajout réussi", "L'établissement a été ajouté avec succès.");

        } catch (NumberFormatException e) {
            afficherAlerteErreur("Erreur de format", "Veuillez entrer des valeurs valides pour l'établissement.");
            e.printStackTrace();
        } catch (SQLException e) {
            afficherAlerteErreur("Erreur lors de l'ajout", "Une erreur est survenue lors de l'ajout de l'établissement. Veuillez réessayer.");
            e.printStackTrace();
        }
    }


    private boolean isValidString(String str) {
        return str.matches("[\\p{L}.\\s]+");
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("\\d{8}")) {
            return false;
        }
        return true;
    }


    private boolean isValidDate(Date date) {
        return date.toLocalDate().isBefore(LocalDate.now());
    }

    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerteInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //BOUTTON IMPORT
    public void importer(ActionEvent event) {
        Etablissement e = new Etablissement();
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if(file != null)
        {
            data.pathE = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 125,130,false , true);
            importIV.setImage(image);
        }
    }
    //BOUTTON CLEAR
    public void clear(ActionEvent event) {
        data.pathE = "";
        importIV.setImage(null);
        Nom_Etablissement.setText("");
        Adresse_Etablissement.setText("");
        typeEtablissement1.setValue("choisir un type");
        Tel_Etablissement.setText("");
        Directeur_Etablissement.setText("");
        Date_Fondation.setValue(null);

    }

    // Méthode pour afficher une erreur dans un label
    private void afficherErreur(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    // Méthode pour effacer un message d'erreur dans un label
    private void clearError(Label label) {
        label.setVisible(false);
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
