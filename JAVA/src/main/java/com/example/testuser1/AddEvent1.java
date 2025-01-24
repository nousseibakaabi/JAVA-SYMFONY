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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Event;
import services.EtablissementServices;
import services.EventService;
import services.PartnerService;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class AddEvent1 {
    private final EventService es = new EventService();
    private final PartnerService ps = new PartnerService();

    private final EtablissementServices ests = new EtablissementServices();

    @FXML
    private TextField nameEventTF;
    @FXML
    private DatePicker dateEventDP;
    @FXML
    private Spinner<Integer> nbrMaxS;
    @FXML
    private TextField prixTF;
    @FXML
    private TextArea descTF;
    @FXML
    private AnchorPane mainForm;
    @FXML
    private ComboBox<String> partnerCB;

    @FXML
    private ComboBox<String> estabCB;

    @FXML
    private ImageView importIV;
    private Image image;

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        nbrMaxS.setValueFactory(valueFactory);

        // Charger les noms des partenaires dans le ComboBox partnerCB
        try {
            List<String> nomsPartners = ps.getName();
            ObservableList<String> observableNoms = FXCollections.observableArrayList(nomsPartners);
            partnerCB.setItems(observableNoms);

            List<String> nomsEstab = ests.getNoms();
            ObservableList<String> observableNomsEst = FXCollections.observableArrayList(nomsEstab);
            estabCB.setItems(observableNomsEst);
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Une erreur est survenue lors du chargement des établissements.");
            e.printStackTrace();

    }}
    @FXML
    void addEvent(ActionEvent event) throws SQLException {
        try {
            // Validation des champs
            String eventName = nameEventTF.getText().trim();
            if (eventName.isEmpty()) {
                showAlert("Erreur", "Le champ Nom de l'événement ne peut pas être vide.");
                return;
            }

            Date currentDate = new Date(System.currentTimeMillis());
            Date selectedDate = Date.valueOf(dateEventDP.getValue());
            if (selectedDate.before(currentDate)) {
                showAlert("Erreur", "La date de l'événement doit être supérieure à la date actuelle.");
                return;
            }

            int maxParticipants = nbrMaxS.getValue();
            if (maxParticipants <= 0) {
                showAlert("Erreur", "Le nombre maximal de participants ne peut pas être négatif.");
                return;
            }

            String eventDescription = descTF.getText().trim();
            if (eventDescription.isEmpty()) {
                showAlert("Erreur", "Le champ Description ne peut pas être vide.");
                return;
            }



            String selectedPartner = partnerCB.getValue();
            String selectedEstab = estabCB.getValue();
             // Utiliser la méthode de EtablissementServices pour obtenir l'ID par le nom

            if (data.path == null || data.path.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner une image.");
                return;
            }
            double prix = Double.parseDouble(prixTF.getText());
            System.out.println(ps.getIDByNom(selectedPartner));
            System.out.println(ests.getIDByNom(selectedEstab));

            // Ajouter l'événement si toutes les validations sont réussies
            Date dateEventC = Date.valueOf(dateEventDP.getValue());

            Event e =new Event(ps.getIDByNom(selectedPartner),ests.getIDByNom(selectedEstab),
                    eventName, dateEventC, maxParticipants,prix, eventDescription,data.path);
            System.out.println(e);
            es.add(e);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de format du numéro d'établissement.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout de l'événement à la base de données.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue s'est produite.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //BOUTTON BACK (NAVIGATION)
    private Scene scene;
    private Stage primaryStage;
    private Parent root;

    @FXML
    void retourEvent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEvent1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    //BOUTTON IMPORT
    public void importer(ActionEvent event) {
        Event e = new Event();
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
        
    //BOUTTON CLEAR
    public void clear(ActionEvent event) {
        estabCB.setValue("Establishment Name");
        partnerCB.setValue("Partner Name");
        nameEventTF.setText("");
        dateEventDP.setValue(null);
        nbrMaxS.getValueFactory().setValue(0);
        descTF.setText("");
        data.path = "";
        importIV.setImage(null);
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
    void affi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetEtabliss1.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur de chargement", "Une erreur est survenue lors du chargement de la vue.");
        }
    }

    @FXML
    void gestioncertif(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/GetCertif1.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur de chargement", "Une erreur est survenue lors du chargement de la vue.");
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