package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Formation;
import services.NiveauServices;
import services.ServiceFormation;
import services.ServiceTuteur;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class updateFormationFront {

    private final ServiceFormation ft = new ServiceFormation();
    private final ServiceTuteur st = new ServiceTuteur();

    @FXML
    private ChoiceBox<String> categorie1;

    @FXML
    private DatePicker date_d1;

    @FXML
    private DatePicker date_f1;

    @FXML
    private TextArea description1;

    @FXML
    private TextField id_formation1;

    @FXML
    private ChoiceBox<String> nom_prenom_tuteur1;

    @FXML
    private TextField lien1;
    @FXML
    private ComboBox<String> comboLevel;
    @FXML
    private AnchorPane mainForm;

    @FXML
    private TextField titre1;

    @FXML
    private Button updateFormation;

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    @FXML
    void initialize() {
        NiveauServices sn = new NiveauServices();

        List<String> niveauNames = null;
        try {
            niveauNames = sn.getAllNiveauNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Populate the ComboBox with the fetched niveau names
        comboLevel.getItems().addAll(niveauNames);

    }


    @FXML
    void modifier(ActionEvent event) {

        try {
            Date selectedStartDate = Date.valueOf(date_d1.getValue());
            Date selectedEndDate = Date.valueOf(date_f1.getValue());



            String selectedCategorie = categorie1.getValue();
            if (selectedCategorie == null) {
                showAlert("Erreur", "Catégorie non sélectionnée", "Veuillez sélectionner une catégorie.");
                return;
            }

            String selectedTuteur = nom_prenom_tuteur1.getValue();
            if (selectedTuteur == null) {
                showAlert("Erreur", "ID non sélectionnée", "Veuillez sélectionner l'id.");
                return;
            }

            int id = Integer.parseInt(id_formation1.getText());
            String[] parts = selectedTuteur.split(" "); // Divise la chaîne à chaque espace
            String nom = parts[0]; // Le premier élément du tableau est le nom
            String prenom = parts[1];
            int idTuteur = st.getIDByNom(nom,prenom);



            Formation updatedFormation = new Formation(
                    idTuteur,
                    comboLevel.getValue(),
                    selectedCategorie,
                    titre1.getText(),
                    description1.getText(),
                    selectedStartDate,
                    selectedEndDate,
                    lien1.getText()
            );
            System.out.println(updatedFormation);

            ft.updateFormation(updatedFormation,id);

            afficherAlerteInformation("Mise à jour réussie", "La formation a été mis à jour avec succès.");
        } catch (NumberFormatException e) {

            afficherAlerteErreur("Erreur de format", "Veuillez entrer des valeurs valides.");
            e.printStackTrace();
        } catch (SQLException e) {

            afficherAlerteErreur("Erreur SQL", "Une erreur est survenue lors de la mise à jour de formation. Veuillez réessayer.");
            e.printStackTrace();
        } catch (Exception e) {

            afficherAlerteErreur("Erreur", "Une erreur est survenue lors de la mise à jour de formation. Veuillez réessayer.");
            e.printStackTrace();
        }
    }

    private void afficherAlerteInformation(String succès, String opérationRéussie) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(succès);
        alert.setHeaderText(opérationRéussie);
        alert.showAndWait();
    }

    private void afficherAlerteErreur(String titre, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();


    }

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void clear(ActionEvent event) {

        id_formation1.setText("");
        nom_prenom_tuteur1.setValue("tuteur name");
        categorie1.setValue("");
        titre1.setText("");
        description1.setText("");
        date_d1.setValue(null);
        date_f1.setValue(null);
        lien1.setText("");
        comboLevel.setValue("");

    }


    @FXML
    void retour(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowFormation1Front.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            afficherAlerteErreur("Erreur de chargement", "Une erreur est survenue lors du chargement de la vue.");
        }

    }

    public void setData(Formation formation) {

        id_formation1.setText(String.valueOf(formation.getId_formation()));
        try {
            List<String> nomsTuteurs = st.getNoms();
            ObservableList<String> observableNoms = FXCollections.observableArrayList(nomsTuteurs);
            nom_prenom_tuteur1.setItems(observableNoms);
            nom_prenom_tuteur1.setValue(st.getNameByID(formation.getId_tuteur()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        categorie1.setValue(String.valueOf(formation.getCategorie()));


        if (formation.getDate_d() != null) {
            date_d1.setValue(formation.getDate_d().toLocalDate());
        } else {

            date_d1.setValue(null);
        }

        if (formation.getDate_f() != null) {
            date_f1.setValue(formation.getDate_f().toLocalDate());
        } else {

            date_f1.setValue(null);
        }

        NiveauServices sn = new NiveauServices();

        comboLevel.setValue(formation.getNom_niveau());


        titre1.setText(formation.getTitre());
        description1.setText(formation.getDescription());
        lien1.setText(formation.getLien());

    }



    public void showFormation1(ActionEvent event) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowFormation1Front.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void importerPDF(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer un fichier PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );

        // Afficher la boîte de dialogue de sélection de fichiers
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            // Traiter le fichier PDF sélectionné (par exemple, stocker son chemin ou son contenu)
            System.out.println("Fichier PDF sélectionné : " + selectedFile.getAbsolutePath());
            SharedData.setCheminPDF(selectedFile.getAbsolutePath()); // Stocker le chemin du fichier PDF dans SharedData
        } else {
            // Gérer le cas où aucun fichier n'a été sélectionné
            System.out.println("Aucun fichier sélectionné.");
        }
    }
}