package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Tuteur;
import services.ServiceTuteur;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class addTuteur1 {

    private final ServiceTuteur st = new ServiceTuteur();

    @FXML
    private Button AddTuteur;

    @FXML
    private DatePicker date_naisc;

    @FXML
    private TextField email;

    @FXML
    private TextField id_tuteur;

    @FXML
    private ImageView importIV;

    @FXML
    private Button importerBT;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField profession;

    @FXML
    private TextField tlf;
    private Image image;

    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Validation des champs requis
            if (nom.getText().isEmpty() || prenom.getText().isEmpty() || date_naisc.getValue() == null ||
                    tlf.getText().isEmpty() || profession.getText().isEmpty() || email.getText().isEmpty()) {
                showAlert("Erreur", "Champs requis", "Veuillez remplir tous les champs.");
                return;
            }

            if (nom.getText().length() < 3 || prenom.getText().length() < 3) {
                showAlert("Erreur", "Longueur minimale non respectée", "Le nom et le prénom doivent avoir au moins 2 caractères.");
                return;
            }

            // Validation du format de l'email
            if (!isValidEmail(email.getText())) {
                showAlert("Erreur", "Format d'email invalide", "Veuillez saisir une adresse email valide.");
                return;
            }


            // Validation du numéro de téléphone
            if (!isValidTlf(tlf.getText())) {
                showAlert("Erreur", "Format de numéro de téléphone invalide", "Veuillez saisir un numéro de téléphone valide.");
                return;
            }


            Date selectedDate = Date.valueOf(date_naisc.getValue());

            st.addTuteur(new Tuteur(
                    nom.getText(),
                    prenom.getText(),
                    Date.valueOf(selectedDate.toLocalDate()),
                    Integer.parseInt(tlf.getText()),
                    profession.getText(),
                    email.getText(),
                    data.path
            ));
            afficherAlerteInformation("Succès", "Opération réussie", "Le tuteur est ajouté avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Erreur de format", "Assurez-vous que le numéro de téléphone est un nombre.");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur SQL", e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur inattendue", e.getMessage());
        }

    }

    @FXML
    void clear(ActionEvent event) {

        id_tuteur.setText("");
        nom.setText("");
        prenom.setText("");
        date_naisc.setValue(null);
        profession.setText("");
        email.setText("");
        data.path = "";
        importIV.setImage(null);

    }

    @FXML
    void importer(ActionEvent event) {
        Tuteur t = new Tuteur();
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
        File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
        if(file != null)
        {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 125, 130, false, true);
            importIV.setImage(image);
        }


    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showTuteur1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void afficherAlerteInformation(String succès, String opérationRéussie, String s) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(succès);
        alert.setHeaderText(opérationRéussie);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void afficherAlerteErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidTlf(String tlf) {
        // Vérifier si le numéro de téléphone contient exactement 8 chiffres
        return tlf.matches("\\d{8}");
    }


    private boolean isValidEmail(String email) {
        // Expression régulière pour valider l'adresse e-mail
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

        // Vérification de la correspondance de l'adresse e-mail avec l'expression régulière
        return email.matches(emailRegex);
    }


    public void showTuteur1(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showTuteur1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showFormation1(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showFormation1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    @FXML
    void tuteurs(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/showTuteur1.fxml"));
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


