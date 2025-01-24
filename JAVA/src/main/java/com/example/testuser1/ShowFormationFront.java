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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Formation;
import services.ServiceFormation;
import services.ServiceTuteur;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;



public class ShowFormationFront implements Initializable {

    private ServiceFormation ft = new ServiceFormation();
    private final ServiceTuteur st = new ServiceTuteur();


    @FXML
    private TextField recherche;

    @FXML
    private ChoiceBox<String> triChoiceBox;

    @FXML
    private AnchorPane event_AP;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane formation_scrollPane;
    private Formation formation;

    private ObservableList<Formation> formationsList;


    private Scene scene;
    private Stage primaryStage;
    private Parent root;



    public void formationDisplay() throws SQLException {
        formationsList = FXCollections.observableList(ft.getAll());
        int row = 0;
        int column = 0;
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().clear();
        for(int i=0;i<formationsList.size();i++)
        {
            try{
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("formations.fxml"));
                AnchorPane pane = load.load();
                Formations formationsCard = load.getController();
                formationsCard.setData(formationsList.get(i));

                //mise a jour de l'affichage
                pane.getProperties().put("com.example.testuser1", this);

                if(column == 1)
                {
                    column=0;
                    row+=1;
                }

                grid.add(pane,column++,row);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    private void afficherFormations(List<Formation> formations) {
        int row = 0;
        int column = 0;
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().clear();
        for (int i = 0; i < formations.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("formations.fxml"));
                AnchorPane pane = load.load();
                Formations items = load.getController();
                items.setData(formations.get(i));

                // Mise à jour de l'affichage
                pane.getProperties().put("com.example.testuser1", this);
                if (column == 1) {
                    column = 0;
                    row += 1;
                }
                grid.add(pane, column++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            formationDisplay();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        triChoiceBox.setItems(FXCollections.observableArrayList("default","Trier par categorie", "Trier par niveau"));


        triChoiceBox.setValue("default");

        if (triChoiceBox.getValue() == "default") {
            afficherFormationsNonTries();
        }
    }

    @FXML
    void addFormation(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/addFormationFront.fxml"));
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
    void showFormation(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowFormationFront.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    @FXML
    void recherche(ActionEvent event) {
        String searchText = recherche.getText().trim().toLowerCase();

        ObservableList<Formation> formationsTrouves = FXCollections.observableArrayList();

        for (Formation formation : formationsList) {

            if (String.valueOf(formation.getId_tuteur()).contains(searchText) ||
                    formation.getCategorie().toLowerCase().contains(searchText) ||
                    formation.getDescription().toLowerCase().contains(searchText) ||
                    formation.getTitre().toLowerCase().contains(searchText))
            {
                formationsTrouves.add(formation);
            }
        }

        afficherFormationsTrouves(formationsTrouves);
    }

    private void afficherFormationsTrouves(ObservableList<Formation> formationsTrouves) {
        int row = 0;
        int column = 0;
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().clear();
        for (int i = 0; i < formationsTrouves.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();

                load.setLocation(getClass().getResource("/com/example/testuser1/formations.fxml"));
                Parent root = load.load();
                updateFormationFront items = load.getController();
                items.setData(formationsTrouves.get(i));

                grid.getProperties().put("controller", this);
                if (column == 1) {
                    column = 0;
                    row += 1;
                }
                grid.add(root, column++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void triFormations(ActionEvent event) {

        String choixTri = triChoiceBox.getValue();
        if (choixTri != null) {
            switch (choixTri) {
                case "Trier par categorie":
                    trierParCategorie();
                    break;
                case "Trier par niveau":
                    trierParNiveau();
                    break;
                case "default":
                    afficherFormationsNonTries();
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

    private void trierParCategorie() {
        try {
            List<Formation> formations = ft.trierParCategorie();
            System.out.println(formations.size());
            afficherFormations(formations);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par categorie.");
        }
    }

    private void trierParNiveau() {
        try {
            List<Formation> formations = ft.trierParNiveau();
            System.out.println(formations.size());
            afficherFormations(formations);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par niveau.");
        }
    }



    private void afficherFormationsNonTries() {
        try {
            List<Formation> formations= ft.getAll();
            afficherFormations(formations);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'affichage des formations.");
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

}

