package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Formation;
import services.ServiceFormation;
import services.ServiceTuteur;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Items1 implements Initializable {

    private ServiceFormation ft = new ServiceFormation();
    private ServiceTuteur st = new ServiceTuteur();


    @FXML
    private Button telechargerPDFButton;

    private String cheminPDF;

    @FXML
    private Label categorie_label;

    @FXML
    private Label date_d_label;

    @FXML
    private Label date_f_label;

    @FXML
    private Label description_label;

    @FXML
    private AnchorPane formation_aff;

    @FXML
    private Label id_formation_label;

    @FXML
    private Label nom_niveau_label;

    @FXML
    private Label nom_prenom_tuteur_label;


    @FXML
    private Button modifierBT;

    @FXML
    private Button supprimerBT;

    @FXML
    private Label titre_label;
    private Formation formation;

    @FXML
    private Hyperlink lien;

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    public Items1() {
    }

    public Items1(ServiceFormation serviceFormation, ServiceTuteur serviceTuteur) {

        this.ft = serviceFormation;
        this.st = serviceTuteur;
    }


    public void setCheminPDF(String cheminPDF) {
        this.cheminPDF = cheminPDF;
    }


    public String getCheminPDF() {
        return cheminPDF;
    }


    @FXML
    void modifier(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("updateFormation.fxml"));
        root = loader.load();
        updateFormation1 updateFormation1Controller = loader.getController();
        updateFormation1Controller.setData(formation);
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    @FXML
    void supprimer(ActionEvent event) throws SQLException {

        ft.deleteFormation(formation.getId_formation());
        //mise a jour de l'affichage
        showFormation1 showFormation1Controller = (showFormation1) formation_aff.getProperties().get("com.example.testuser1");
        showFormation1Controller.formationDisplay();


    }

    public void setData(Formation formation) {
        this.formation = formation;

        id_formation_label.setText(String.valueOf(formation.getId_formation()));
        nom_prenom_tuteur_label.setText(String.valueOf(formation.getId_tuteur()));

        try {
            nom_prenom_tuteur_label.setText(st.getNameByID(formation.getId_tuteur()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        nom_niveau_label.setText(String.valueOf(formation.getNom_niveau()));

        categorie_label.setText(formation.getCategorie());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(formation.getDate_d());
        date_d_label.setText(formattedDate);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate1 = dateFormat1.format(formation.getDate_f());
        date_f_label.setText(formattedDate1);


        titre_label.setText(String.valueOf(formation.getTitre()));
        description_label.setText(String.valueOf(formation.getDescription()));
        lien.setText(formation.getLien());



    }

    @FXML
    void openLink(ActionEvent event) {
        String meetLink = lien.getText(); // Obtenir le lien Meet à partir du texte de l'hyperlien
        if (meetLink != null && !meetLink.isEmpty()) {
            try {
                Desktop.getDesktop().browse(new URI(meetLink)); // Utilisation du lien Meet stocké dans l'attribut lien
            } catch (IOException ex) {
                System.err.println("Erreur lors de l'ouverture du lien : " + ex.getMessage());
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                System.err.println("Le lien est mal formé : " + meetLink);
                ex.printStackTrace();
            }
        } else {
            System.err.println("Le lien Meet est vide.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lien.setOnAction(event -> {
            String lienText = lien.getText(); // Renommez la variable locale pour éviter la confusion
            try {
                Desktop.getDesktop().browse(new URI(lienText));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                // Gérez les erreurs ici
            }
        });
    }



    private void afficherAlerteInformation(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void afficherAlerteErreur(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void telechargerPDFButton(ActionEvent event) {
        String cheminPDF = SharedData.getCheminPDF(); // Récupérer le chemin PDF depuis SharedData

        if (cheminPDF != null && !cheminPDF.isEmpty()) {
            File file = new File(cheminPDF);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF");
            fileChooser.setInitialFileName(file.getName()); // Utilisez le nom du fichier original comme nom par défaut
            File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile != null) {
                try {
                    // Copier le fichier PDF vers l'emplacement de sauvegarde spécifié
                    Files.copy(file.toPath(), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    afficherAlerteInformation("Succès", "Téléchargement réussi", "Le PDF a été téléchargé avec succès.");
                } catch (IOException e) {
                    afficherAlerteErreur("Erreur", "Erreur lors du téléchargement", "Une erreur est survenue lors du téléchargement du PDF.");
                }
            }
        } else {
            // Gérez le cas où aucun fichier PDF n'a été importé ou trouvé
            afficherAlerteErreur("Erreur", "Aucun PDF trouvé", "Aucun PDF associé à cet élément.");
        }
    }








}
