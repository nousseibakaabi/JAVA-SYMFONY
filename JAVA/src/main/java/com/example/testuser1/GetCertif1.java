package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import models.Certificat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.controlsfx.control.Notifications;
import services.CertficatServices;
import utils.SessionManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetCertif1 {

    @FXML
    private AnchorPane event_AP;

    @FXML
    private GridPane event_gridPane;

    @FXML
    private ScrollPane event_scrollPane;

    @FXML
    private TextField CertifRecherche;

    @FXML
    private ChoiceBox<String> triChoiceBox;

    private CertficatServices cs = new CertficatServices();

    @FXML
    void rechercherCertificate(ActionEvent event) {
        String nomCertificat = CertifRecherche.getText();
        if (!nomCertificat.isEmpty()) {
            try {
                List<Certificat> certificats = cs.getByPartialName(nomCertificat);
                if (!certificats.isEmpty()) {
                    afficherCertificats(certificats);
                } else {
                    showAlert("Information", "Aucun certificat trouvé avec ce nom.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Une erreur est survenue lors de la recherche des certificats.");
            }
        } else {
            showAlert("Error", "Veuillez saisir un nom de certificat pour effectuer la recherche.");
        }
    }

    @FXML
    void triCertificats(ActionEvent event) {
        String choixTri = triChoiceBox.getValue(); // Récupérer le choix de tri depuis le ChoiceBox
        if (choixTri != null) {
            switch (choixTri) {
                case "Trier par nom":
                    trierParNom();
                    break;
                case "Trier par domaine":
                    trierParDomaine();
                    break;
                case "Trier par niveau":
                    trierParNiveau();
                    break;
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un critère de tri.");
        }
    }








    @FXML
    public void initialize() {
        // Ajoutez les options de tri au ChoiceBox
        triChoiceBox.setItems(FXCollections.observableArrayList("Trier par nom", "Trier par domaine", "Trier par niveau"));

        // Définissez une option par défaut si nécessaire
        triChoiceBox.setValue(" "); // Notez que je laisse une chaîne vide ici, mais vous pouvez choisir une valeur par défaut différente si nécessaire

        // Vérifiez si une valeur est sélectionnée dans le ChoiceBox avant d'afficher les certificats non triés
        if (triChoiceBox.getValue() != null) {
            afficherCertificatsNonTries();
        }
    }

    private void afficherCertificatsNonTries() {
        try {
            List<Certificat> certificats = cs.getAll(); // Obtenir tous les certificats
            afficherCertificats(certificats); // Afficher les certificats
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'affichage des certificats.");
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void afficherCertificats(List<Certificat> certificats) {
        event_gridPane.getChildren().clear();
        int row = 0;
        for (Certificat certificat : certificats) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/itemsE1.fxml"));
            try {
                AnchorPane pane = loader.load();
                itemsE1 itemsEController = loader.getController();
                itemsEController.setData(certificat, e -> {
                    event_gridPane.getChildren().remove(pane);
                    try {
                        cs.deleteCertificate(certificat.getID_Certificat());
                        Notifications.create()
                                .title("supression réussi")
                                .text("Le certificat a été suprimer avec succès.")
                                .showError();

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

    //  tri par nom, domaine et niveau
    private void trierParNom() {
        try {
            List<Certificat> certificats = cs.trierParNom();
            afficherCertificats(certificats);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par nom.");
        }
    }

    private void trierParDomaine() {
        try {
            List<Certificat> certificats = cs.trierParDomaine();
            afficherCertificats(certificats);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par domaine.");
        }
    }

    private void trierParNiveau() {
        try {
            List<Certificat> certificats = cs.trierParNiveau();
            afficherCertificats(certificats);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors du tri par niveau.");
        }
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
            showAlert("Error", "Failed to load the view.");
        }
    }

    public void ajouterCertificat(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/AddCertif1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @FXML
    void ExportExcel(ActionEvent event) {
        List<Certificat> certificats;
        try {
            certificats = cs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        // Create header row
        Row headerRow = spreadsheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("Domaine");
        headerRow.createCell(3).setCellValue("Niveau");
        headerRow.createCell(4).setCellValue("Date d'obtention");
        headerRow.createCell(5).setCellValue("ID de l'établissement");

        // Populate data rows
        int rowNum = 1;
        for (Certificat certificat : certificats) {
            Row dataRow = spreadsheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(certificat.getID_Certificat());
            dataRow.createCell(1).setCellValue(certificat.getNom_Certificat());
            dataRow.createCell(2).setCellValue(certificat.getDomaine_Certificat());
            dataRow.createCell(3).setCellValue(certificat.getNiveau());
            dataRow.createCell(4).setCellValue(certificat.getDate_Obtention_Certificat().toString()); // Adjust as needed
            dataRow.createCell(5).setCellValue(certificat.getID_Etablissement());
        }

        try (FileOutputStream fileOut = new FileOutputStream("workbook.xls")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new RuntimeException("Error writing Excel file", e);
        }
    }
    private Scene scene;
    private Stage primaryStage;
    private Parent root;

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
