package com.example.testuser1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import models.Certificat;
import models.Niveau;
import org.controlsfx.control.Notifications;
import services.CertficatServices;
import services.NiveauServices;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddNiveau1 implements Initializable {
    NiveauServices ns = new NiveauServices();

    @FXML
    private TextArea descTF;

    @FXML
    private TextField dureeTF;

    @FXML
    private ImageView importIV;

    @FXML
    private Button importerBT;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TextField nameNiveauTF;

    @FXML
    private TextField nbformationTF;

    @FXML
    private TextField prerequisTF;
    private Image image;
    @FXML
    private ComboBox<Certificat> ComboCertificat;
    @FXML
    void addNiveau(ActionEvent event) {
        try{
            // Validation des champs
            String niveauName = nameNiveauTF.getText().trim();
            if (niveauName.isEmpty()) {
                showAlert("Erreur", "Le champ Nom du niveau ne peut pas être vide.");
                return;
            }
            String niveauPrerequis = prerequisTF.getText().trim();
            if (niveauPrerequis.isEmpty()) {
                showAlert("Erreur", "Le champ Prérequis ne peut pas être vide.");
                return;
            }
            String duration = dureeTF.getText().trim();
            if (duration.isEmpty()) {
                showAlert("Erreur", "Le champ duree ne peut pas être vide.");
                return;
            }
            int nbformation = Integer.parseInt(nbformationTF.getText());

            String nbformationText = nbformationTF.getText().trim();
            if (nbformationText.isEmpty()) {
                showAlert("Erreur", "Le champ nbformations ne peut pas être vide .");
                return;
            }
            String description = descTF.getText().trim();
            if (description.isEmpty()) {
                showAlert("Erreur", "Le champ description ne peut pas être vide.");
                return;
            }

            if (data.path1 == null || data.path1.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner une image.");
                return;
            }

            Niveau niveau = new Niveau(
                    niveauName,
                    niveauPrerequis,
                    duration,
                    nbformation,
                    selectedCertificatId,
                    description,
                    data.path1
            );


            // Appeler la méthode d'update de votre service
            ns.add(niveau);
            btnNotifcationOnAction(event);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de format du numéro d'établissement.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour de l'événement dans la base de données.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue s'est produite.");
        }


    }
    private void btnNotifcationOnAction(ActionEvent event) {
        Notifications notificationsBuilder = Notifications.create()
                .title("Notifications")
                .text("Level added successfully")
                .graphic(null)
                .hideAfter(Duration.seconds(6))
                .position(Pos.TOP_RIGHT)
                .onAction(events -> onNotificationAction(event));

        notificationsBuilder.show();
    }

    private void onNotificationAction(ActionEvent event) {
        System.out.println("Notification clicked!");
        // Ajoutez le code que vous souhaitez exécuter lorsqu'une notification est cliquée
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void clear(ActionEvent event) {
        nameNiveauTF.setText("");
        prerequisTF.setText("");
        dureeTF.setText("");
        nbformationTF.setText("");
        descTF.setText("");
        data.path = "";
        importIV.setImage(null);

    }

    @FXML
    void importer(ActionEvent event) {
        Niveau niveau = new Niveau();
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if(file != null)
        {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 125,130,false , true);
            importIV.setImage(image);
        }


    }
    private Scene scene;
    private Stage primaryStage;
    private Parent root;
    @FXML
    void retourEvent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("getNiveau1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    @FXML
    void showLearners(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("getApprenant1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    void showLevels(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("getNiveau1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private int selectedCertificatId; // Declare the variable at the class level

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CertficatServices cs = new CertficatServices();
        List<Certificat> certificats = null;
        try {
            certificats = cs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ComboCertificat.getItems().addAll(certificats);

        // Set the StringConverter for the ComboBox
        ComboCertificat.setConverter(new StringConverter<Certificat>() {
            @Override
            public String toString(Certificat certificat) {
                // Convert the Certificat object to its name for display
                return certificat != null ? certificat.getNom_Certificat() : null;
            }

            @Override
            public Certificat fromString(String string) {
                // Not needed for this example
                return null;
            }
        });

        // Add a listener to the ComboBox to handle selection changes
        ComboCertificat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Handle the selected Certificat object
                 selectedCertificatId = newValue.getID_Certificat();
                // Use the selectedCertificatId as needed
            }
        });



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
    }}
