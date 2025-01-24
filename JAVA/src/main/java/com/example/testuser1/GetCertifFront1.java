package com.example.testuser1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Etablissement;
import services.EtablissementServices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Certificat;
import services.CertficatServices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class     GetCertifFront1 implements Initializable {

    @FXML
    private com.gluonhq.charm.glisten.control.TextField searchByName;


    @FXML
    private GridPane event_gridPane;
    @FXML
    private ImageView notFound;

    @FXML
    private AnchorPane panee;


    private final CertficatServices cs = new CertficatServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchByName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                rechercherEtablissements(newValue);
            }
        });
        try {
            populateGrid();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateGrid() throws SQLException {
        List<Certificat> certificatList = fetchDataFromDatabase();
        afficherCertificats(certificatList);
    }

    private void afficherCertificats(List<Certificat> certificats) {
        event_gridPane.getChildren().clear();
        int row = 0;
        for (Certificat certificat : certificats) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/itemsfront1.fxml"));
            try {
                AnchorPane pane = loader.load();
                itemsfont1 itemsController = loader.getController();
                itemsController.setData(certificat, e -> {
                    event_gridPane.getChildren().remove(pane);
                    try {
                        cs.deleteCertificate(certificat.getID_Certificat());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Failed to delete the certificate from the database.");
                    }
                });
                event_gridPane.add(pane, 0, row++);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load certificate item view.");
            }
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private List<Certificat> fetchDataFromDatabase() throws SQLException {
        return cs.getAll();
    }

    private void rechercherEtablissements(String query) {
        try {
            List<Certificat> resultatsRecherche = cs.getByPartialName(query);
            if (resultatsRecherche.isEmpty()) {
                afficherNotFound();
            } else {
                afficherCertificats(resultatsRecherche);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void afficherNotFound() {
        event_gridPane.getChildren().clear();
        event_gridPane.add(notFound, 0, 0);
    }


    public void Schools(ActionEvent actionEvent) {
    }

    public void SchoolsHyper(ActionEvent actionEvent) {
    }

    public void Home(ActionEvent actionEvent) {
    }

    @FXML
    void back(ActionEvent event) {
        loadFXML("/com/example/testuser1/GetCertif1.fxml", event);
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
        }
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

    private Scene scene;
    private Stage primaryStage;
    private Parent root;


    public void showSchools(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtablissFront1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
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
