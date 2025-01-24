    package com.example.testuser1;

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
    import models.Partner;
    import services.PartnerService;
    import utils.SessionManager;

    import java.io.File;
    import java.io.IOException;
    import java.sql.SQLException;
    
    public class UpdatePartner1 {
        private PartnerService ps = new PartnerService();
    
    
        @FXML
        private Label descP_label;
    
        @FXML
        private TextArea descTF;
    
        @FXML
        private TextField emailTF;
    
        @FXML
        private TextField idPartnerTF;
    
        @FXML
        private Label nameP_label;
    
        @FXML
        private TextField namePartnerTF;
    
        @FXML
        private TextField telTF;
    
        @FXML
        private Label typeP_label;
        @FXML
        private Label email_label;
        @FXML
        private Label tel_label;
    
        @FXML
        private ComboBox<String> typePartnerCB;
        private Partner partner;
        @FXML
        private ImageView partnerIV;
        @FXML
        private ImageView partnerIV1;

        private Image image;
        @FXML
        private AnchorPane mainForm;




        public void setData(Partner partner)
        {
            this.partner = partner;
    
            nameP_label.setText(String.valueOf(partner.getNamePartner()));
            typeP_label.setText(String.valueOf(partner.getTypePartner()));
            descP_label.setText(partner.getDescription());
            email_label.setText(partner.getEmail());
            tel_label.setText(String.valueOf(partner.getTel()));
    
            String path = "File:"+partner.getImage();
            image = new Image(path,125,130,false,true);
            partnerIV.setImage(image);
        }
    
        @FXML
        void clear(ActionEvent event) {
            idPartnerTF.setText("");
            namePartnerTF.setText("");
            typePartnerCB.setValue("default");
            descTF.setText("");
            emailTF.setText("");
            telTF.setText("");

        }
    
        public void setPartnerData(Partner partner) {
            idPartnerTF.setText(String.valueOf(partner.getIdPartner()));
            namePartnerTF.setText(partner.getNamePartner());
            typePartnerCB.setValue(partner.getTypePartner());
            descTF.setText(partner.getDescription());
            emailTF.setText(partner.getEmail());
            telTF.setText(String.valueOf(partner.getTel()));

            String path = "File:"+partner.getImage();
            image = new Image(path,125,130,false,true);
            partnerIV1.setImage(image);
        }
    
    
        @FXML
        void editEvent(ActionEvent event) {
            try{
                // Validation des champs
                String eventName = namePartnerTF.getText().trim();
                if (eventName.isEmpty()) {
                    showAlert("Erreur", "Le champ Nom du partnaire ne peut pas être vide.");
                    return;
                }
    
                String eventType = typePartnerCB.getValue();
                if (eventType == null || eventType.isEmpty() || eventType.equals("default")) {
                    showAlert("Erreur", "Veuillez sélectionner un type de partenaire valide.");
                    return;
                }
    
                String partnerDescription = descTF.getText().trim();
                if (partnerDescription.isEmpty()) {
                    showAlert("Erreur", "Le champ Description ne peut pas être vide.");
                    return;
                }
    
                String email = emailTF.getText().trim();
                if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    showAlert("Erreur", "Le champ Email n'est pas dans un format valide.");
                    return;
                }
                if (email.isEmpty()) {
                    showAlert("Erreur", "Le champ Description ne peut pas être vide.");
                    return;
                }
    
                int tel = Integer.parseInt(telTF.getText());
    
                String telText = telTF.getText().trim();
                if (!isValidPhoneNumber(telText)) {
                    showAlert("Erreur", "Le champ Téléphone n'est pas dans un format valide.");
                    return;
                }
    
                int idSelectedPartner = Integer.parseInt(idPartnerTF.getText());
                Partner updatedPartner = new Partner(
                        idSelectedPartner,
                        eventName,
                        eventType,
                        partnerDescription,
                        email,
                        tel,
                        data.path1
                );
                System.out.println("Updated Event: " + updatedPartner);
    
                // Appeler la méthode d'update de votre service
                ps.update(updatedPartner, idSelectedPartner);
    
                System.out.println("Event updated successfully");
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
    
    
        private boolean isValidPhoneNumber(String phoneNumber) {
            // Expression régulière pour un numéro de téléphone avec exactement 8 chiffres
            String phoneRegex = "^[0-9]{8}$";
            return phoneNumber.matches(phoneRegex);
        }
        private void showAlert(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }
    
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

        public void showPartners(ActionEvent actionEvent) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getPartner1.fxml"));
            root = loader.load();
            scene = new Scene(root);
            primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();
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

        public void affi(ActionEvent actionEvent) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEtabliss1.fxml"));
            root = loader.load();
            scene = new Scene(root);
            primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("TANIT ONLINE");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        public void gestioncertif(ActionEvent actionEvent) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getCertif1.fxml"));
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
        void importer(ActionEvent event) {
            Event e = new Event();
            FileChooser openFile = new FileChooser();
            openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
            File file = openFile.showOpenDialog(mainForm.getScene().getWindow());
            if(file != null)
            {
                data.path1 = file.getAbsolutePath();
                image = new Image(file.toURI().toString(), 125,130,false , true);
                partnerIV.setImage(image);
            }

        }
    }
