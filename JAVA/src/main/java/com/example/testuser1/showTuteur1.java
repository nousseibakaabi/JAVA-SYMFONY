package com.example.testuser1;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Tuteur;
import services.ServiceTuteur;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class showTuteur1 implements Initializable {

    private final ServiceTuteur st = new ServiceTuteur();

    @FXML
    private AnchorPane tuteur_AP;

    @FXML
    public GridPane tuteur_gridPane;

    @FXML
    private ScrollPane tuteur_scrollPane;

    @FXML
    private ChoiceBox<String> triChoiceBox;

    @FXML
    private TextField recherche;

    private Tuteur tuteur;

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    private ObservableList<Tuteur> tuteurList;

    public void tuteurDisplay() throws SQLException {
        tuteurList = FXCollections.observableList(st.getAll());
        int row = 0;
        int column = 0;
        tuteur_gridPane.getRowConstraints().clear();
        tuteur_gridPane.getColumnConstraints().clear();
        tuteur_gridPane.getChildren().clear();
        for (int i = 0; i < tuteurList.size(); i++)
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("itemsTuteur.fxml"));
                AnchorPane pane = load.load();
                itemsTuteur items = load.getController();
                items.setData(tuteurList.get(i));

                //mise a jour de l'affichage
                pane.getProperties().put("controller", this);

                if (column == 1) {
                    column = 0;
                    row += 1;
                }

                tuteur_gridPane.add(pane, column++, row);

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tuteurDisplay();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Ajoutez les options de tri au ChoiceBox
        triChoiceBox.setItems(FXCollections.observableArrayList("default","Trier par nom", "Trier par prénom", "Trier par matière"));

        // Définissez une option par défaut si nécessaire
        triChoiceBox.setValue("default"); // Notez que je laisse une chaîne vide ici, mais vous pouvez choisir une valeur par défaut différente si nécessaire

        // Vérifiez si une valeur est sélectionnée dans le ChoiceBox avant d'afficher les certificats non triés
        if (triChoiceBox.getValue() == "default") {
            afficherTuteursNonTries();
        }
    }

    @FXML
    void ajouterTuteur(ActionEvent event) {

        loadAddTuteurInterface();

    }

    private void loadAddTuteurInterface() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("addTuteur.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);

            // Récupérer la fenêtre actuelle et la modifier pour afficher la nouvelle scène
            Stage stage = (Stage) tuteur_scrollPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }




    private void afficherTuteurs(List<Tuteur> tuteurs) {
        int row = 0;
        int column = 0;
        tuteur_gridPane.getRowConstraints().clear();
        tuteur_gridPane.getColumnConstraints().clear();
        tuteur_gridPane.getChildren().clear();
        for (int i = 0; i < tuteurs.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("itemsTuteur.fxml"));
                AnchorPane pane = load.load();
                itemsTuteur items = load.getController();
                items.setData(tuteurs.get(i));

                // Mise à jour de l'affichage
                pane.getProperties().put("com.example.testuser1", this);
                if (column == 1) {
                    column = 0;
                    row += 1;
                }
                tuteur_gridPane.add(pane, column++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void recherche(ActionEvent event) {
        String searchText = recherche.getText().trim().toLowerCase(); // Récupérer le texte entré dans le champ de recherche

        ObservableList<Tuteur> tuteursTrouves = FXCollections.observableArrayList();

        for (Tuteur tuteur : tuteurList) {

            if (tuteur.getNom().toLowerCase().contains(searchText) ||
                    tuteur.getPrenom().toLowerCase().contains(searchText) ||
                    tuteur.getProfession().toLowerCase().contains(searchText) ||
                    tuteur.getEmail().toLowerCase().contains(searchText) ||
                    String.valueOf(tuteur.getTlf()).contains(searchText) ||
                    formatDate(tuteur.getDate_naisc()).contains(searchText)) {
                tuteursTrouves.add(tuteur);
            }
        }

        afficherTuteursTrouves(tuteursTrouves);
    }

    // Méthode pour formater la date dans un format de chaîne spécifique
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }


    private void afficherTuteursTrouves(ObservableList<Tuteur> tuteursTrouves) {
        int row = 0;
        int column = 0;
        tuteur_gridPane.getRowConstraints().clear();
        tuteur_gridPane.getColumnConstraints().clear();
        tuteur_gridPane.getChildren().clear();
        for (int i = 0; i < tuteursTrouves.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("Items.fxml"));
                AnchorPane pane = load.load();
                itemsTuteur items = load.getController();
                items.setData(tuteursTrouves.get(i));

                pane.getProperties().put("com.example.testuser1", this);
                if (column == 1) {
                    column = 0;
                    row += 1;
                }
                tuteur_gridPane.add(pane, column++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void triTuteurs(ActionEvent event) {
        String choixTri = triChoiceBox.getValue();
        if (choixTri != null) {
            switch (choixTri) {
                case "Trier par nom":
                    trierParNom();
                    break;
                case "Trier par prénom":
                    trierParPrenom();
                    break;
                case "Trier par matière":
                    trierParProfession();
                    break;
                case "default":
                    afficherTuteursNonTries();
                    break;
                default:
                    showAlert("Erreur", "Choix de tri non valide.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un critère de tri.");
        }
    }


    private void showAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private void trierParNom() {
        try {
            List<Tuteur> tuteurs = st.trierParNom();
            System.out.println(tuteurs.size());
            afficherTuteurs(tuteurs);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par nom.");
        }
    }

    private void trierParPrenom() {
        try {
            List<Tuteur> tuteurs = st.trierParPrenom();
            System.out.println(tuteurs.size());
            afficherTuteurs(tuteurs);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par prénom.");
        }
    }

    private void trierParProfession() {
        try {
            List<Tuteur> tuteurs = st.trierParProfession();
            System.out.println(tuteurs.size());
            afficherTuteurs(tuteurs);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par matière.");
        }
    }


    private void afficherTuteursNonTries() {
        try {
            List<Tuteur> tuteurs= st.getAll();
            afficherTuteurs(tuteurs);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'affichage des formations.");
        }
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

}