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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Event;
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;



public class GetEventFront1 implements Initializable {
    private final EventService es = new EventService();


    @FXML
    private AnchorPane event_AP;

    @FXML
    private GridPane event_gridPane;

    @FXML
    private ScrollPane event_scrollPane;
    private Event event;

    @FXML
    private ChoiceBox<String> triCB;
    @FXML
    private ImageView notFound;

    @FXML
    private com.gluonhq.charm.glisten.control.TextField searchByName;

    private ObservableList<Event> eventList;



    public void eventDisplay() throws SQLException {
        eventList = FXCollections.observableList(es.getAll());
        int row = 0;
        int column = 0;
        event_gridPane.getRowConstraints().clear();
        event_gridPane.getColumnConstraints().clear();
        event_gridPane.getChildren().clear();
        for(int i=0;i<eventList.size();i++)
        {
            try{
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/testuser1/events.fxml"));
                AnchorPane pane = load.load();
                Events eventsCard = load.getController();
                eventsCard.setData(eventList.get(i));

                //mise a jour de l'affichage
                pane.getProperties().put("controller", this);

                if(column == 1)
                {
                    column=0;
                    row+=1;
                }

                event_gridPane.add(pane,column++,row);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void eventDisplayDesc() throws SQLException {
        eventList = FXCollections.observableList(es.getAllDESC());
        int row = 0;
        int column = 0;
        event_gridPane.getRowConstraints().clear();
        event_gridPane.getColumnConstraints().clear();
        event_gridPane.getChildren().clear();
        for(int i=0;i<eventList.size();i++)
        {
            try{
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/testuser1/events.fxml"));
                AnchorPane pane = load.load();
                Events eventsCard = load.getController();
                eventsCard.setData(eventList.get(i));

                //mise a jour de l'affichage
                pane.getProperties().put("controller", this);

                if(column == 1)
                {
                    column=0;
                    row+=1;
                }

                event_gridPane.add(pane,column++,row);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public void eventDisplayAsc() throws SQLException {
        eventList = FXCollections.observableList(es.getAllASC());
        int row = 0;
        int column = 0;
        event_gridPane.getRowConstraints().clear();
        event_gridPane.getColumnConstraints().clear();
        event_gridPane.getChildren().clear();
        for(int i=0;i<eventList.size();i++)
        {
            try{
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/testuser1/events.fxml"));
                AnchorPane pane = load.load();
                Events eventsCard = load.getController();
                eventsCard.setData(eventList.get(i));

                //mise a jour de l'affichage
                pane.getProperties().put("controller", this);

                if(column == 1)
                {
                    column=0;
                    row+=1;
                }

                event_gridPane.add(pane,column++,row);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        triCB.getItems().addAll("default","Croissant","Decroissant");

        triCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if ("Decroissant".equals(newValue)) {
                    eventDisplayDesc();
                } else if ("Croissant".equals(newValue)) {
                    // Ajoutez le code correspondant pour le tri croissant
                    eventDisplayAsc();
                } else if ("default".equals(newValue)) {
                    // Ajoutez le code correspondant pour le tri croissant
                    eventDisplay();}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Initialisez le tri par défaut (par exemple, tri croissant)
        triCB.getSelectionModel().select("default");
        notFound.setVisible(false);
        notFound.setManaged(false);
        searchByName.setOnKeyReleased(event -> {
            try {
                // Obtenez le texte saisi dans le champ de recherche
                String partialName = searchByName.getText();

                // Appelez la méthode de recherche avancée avec le texte saisi
                List<Event> resultatsRecherche = es.getByPartialName(partialName);
                System.out.println("Number of results: " + resultatsRecherche.size());


                // Affichez les résultats dans le GridPane
                afficherEvents(resultatsRecherche);
                if(partialName == ""){
                    eventDisplay();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void afficherEvents(List<Event> eventList) throws SQLException {
        event_gridPane.getChildren().clear(); // Nettoyez le contenu existant

        if (eventList.isEmpty()) {
            // Affichez un message indiquant que aucun résultat n'a été trouvé
            notFound.setVisible(true);
            notFound.setManaged(true);
            event_gridPane.getChildren().add(notFound);
        } else {
            // Cachez le message "notFound"
            notFound.setVisible(false);
            notFound.setManaged(false);
            int row = 0;
            int column = 0;
            event_gridPane.getRowConstraints().clear();
            event_gridPane.getColumnConstraints().clear();
            event_gridPane.getChildren().clear();
            for (int i = 0; i < eventList.size(); i++) {
                try {
                    FXMLLoader load = new FXMLLoader();
                    load.setLocation(getClass().getResource("/com/example/testuser1/events.fxml"));
                    AnchorPane pane = load.load();
                    Events eventsCard = load.getController();
                    eventsCard.setData(eventList.get(i));

                    //mise a jour de l'affichage
                    pane.getProperties().put("controller", this);

                    if (column == 1) {
                        column = 0;
                        row += eventList.size();
                    }

                    event_gridPane.add(pane, column++, row);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Scene scene;
    private Stage primaryStage;
    private Parent root;
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
    public void showCourses(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/ShowFormation1Front.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("TANIT ONLINE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void showCalendar(ActionEvent event) throws IOException {
        // Charger le fichier FXML pour la nouvelle fenêtre
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/calendar.fxml"));
        Parent calendarRoot = loader.load();

        // Créer une nouvelle scène pour la nouvelle fenêtre
        Scene calendarScene = new Scene(calendarRoot);
        calendarScene.getStylesheets().add(getClass().getResource("/com/example/testuser1/calendar.css").toExternalForm());

        // Créer une nouvelle fenêtre
        Stage calendarStage = new Stage();
        calendarStage.setTitle("Calendrier");  // Vous pouvez définir le titre de la nouvelle fenêtre ici
        calendarStage.setScene(calendarScene);

        // Charger le contrôleur de calendrier et ajouter la vue du calendrier à son conteneur
        Calendar calendarController = loader.getController();
        calendarController.calendarPane.getChildren().add(new FullCalendarView(YearMonth.now(), es).getView());

        // Afficher la nouvelle fenêtre
        calendarStage.show();
    }


}
