package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.Etablissement;
import services.EtablissementServices;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetEtabliss1 {

    @FXML
    private GridPane event_gridPane;
    @FXML
    private TextField EtablissRecherche;
    @FXML
    private ChoiceBox<String> triChoiceBox;
    @FXML
    private AnchorPane event_AP;
    @FXML
    private ScrollPane event_scrollPane;

    private final EtablissementServices es = new EtablissementServices();

    @FXML
    public void initialize() throws SQLException, IOException {
        try {
            // Mettez votre code ici s'il y a quelque chose à exécuter dans le bloc try
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'initialisation.");
        }

        // Définissez les éléments de la ChoiceBox
        triChoiceBox.setItems(FXCollections.observableArrayList("Trier par nom", "Trier par nombre de certificats", "Trier par type d'établissement"));

        // Définissez une option par défaut si nécessaire
        triChoiceBox.setValue(" "); // Notez que je laisse une chaîne vide ici, mais vous pouvez choisir une valeur par défaut différente si nécessaire

        // Vérifiez si une valeur est sélectionnée dans le ChoiceBox avant d'afficher les établissements non triés
        if (triChoiceBox.getValue() != null) {
            afficherEtablissementsNonTries();
        }
    }


    @FXML
    void ajouterSchool(ActionEvent event) {
        loadFXML("/com/example/testuser1/AddEtabliss1.fxml", event);
    }

    @FXML
    void gestionSchool(ActionEvent event) {
        loadFXML("/GetEtabliss1.fxml", event);
        afficherAlerteInformation("Gestion des établissements", "Vous êtes déjà dans la gestion des établissements.");
    }


    private void loadFXML(String path, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du chargement de la vue.");
        }
    }

    @FXML
    void rechercherEtablissement(ActionEvent event) {
        String nomEtablissement = EtablissRecherche.getText();
        if (!nomEtablissement.isEmpty()) {
            try {
                List<Etablissement> etablissements = es.getByPartialName(nomEtablissement);
                if (!etablissements.isEmpty()) {
                    afficherEtablissements(etablissements);
                } else {
                    afficherAlerteInformation("Information", "Aucun établissement trouvé avec ce nom.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur est survenue lors de la recherche des établissements.");
            }
        } else {
            afficherAlerteErreur("Erreur", "Veuillez saisir un nom d'établissement pour effectuer la recherche.");
        }
    }

    private void afficherEtablissements(List<Etablissement> etablissements) {
        event_gridPane.getChildren().clear();
        int row = 0;
        for (Etablissement etablissement : etablissements) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/itemsE.fxml"));
            try {
                AnchorPane pane = loader.load();
                ItemsE itemsEController = loader.getController();
                itemsEController.setData(etablissement, e -> {
                    event_gridPane.getChildren().remove(pane);
                    try {
                        es.deleteSchool(etablissement.getID_Etablissement());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Erreur", "Échec de suppression de l'établissement de la base de données.");
                    }
                });
                event_gridPane.add(pane, 0, row++);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Échec du chargement de la vue de l'établissement.");
            }
        }
    }

    private void afficherEtablissementsNonTries() throws IOException, SQLException {
        List<Etablissement> etablissements = es.getAll();
        afficherEtablissements(etablissements);
    }

    @FXML
    void triEtablissements(ActionEvent event) throws SQLException {
        String choixTri = triChoiceBox.getValue();
        if (choixTri != null) {
            switch (choixTri) {
                case "Trier par nom":
                    trierParNom();
                    break;
                case "Trier par nombre de certificats":
                    trierParNbCertificats();
                    break;
                case "Trier par type d'établissement":
                    trierParType();
                    break;
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un critère de tri.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerteInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //  tri par nom, domaine et niveau
    private void trierParNom() {
        try {
            List<Etablissement> etablissements = es.sortByNom();
            afficherEtablissements(etablissements);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par nom.");
        }
    }

    private void trierParNbCertificats() {
        try {
            List<Etablissement> etablissements = es.sortByNombreCertificats();
            afficherEtablissements(etablissements);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par domaine.");
        }
    }

    private void trierParType() {
        try {
            List<Etablissement> etablissements = es.sortByTypeEtablissement();
            afficherEtablissements(etablissements);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par niveau.");
        }
    }

    private Scene scene;
    private Stage primaryStage;
    private Parent root;
    @FXML
    public void showPartners(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getPartner1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEvent1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void affi(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtabliss1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
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
    @FXML
    void GoLevels(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetNiveau1.fxml"));
        Scene scene;
        Stage primaryStage;
        Parent root;

        root = loader.load();

        scene = new Scene(root);
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }

    @FXML
    void GoStidents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetApprenant1.fxml"));
            Scene scene;
            Stage primaryStage;
            Parent root;

            root = loader.load();

            scene = new Scene(root);
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void tutors(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/showTuteur1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    void courses(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/showFormation1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
