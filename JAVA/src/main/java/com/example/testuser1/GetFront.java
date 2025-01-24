package com.example.testuser1;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GetFront implements Initializable {

    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane pane2;

    @FXML
    private AnchorPane pane3;

    @FXML
    private Label num;

    public void transplateAnimation(double duration, Node node ,double width){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration) , node);
        translateTransition.setByX(width);
        translateTransition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transplateAnimation(0.5,pane2,1000);
        transplateAnimation(0.5,pane3,1000);
    }

    int show = 0;
    @FXML
    void next(ActionEvent event) {
        if(show==0)
        {
            transplateAnimation(0.5,pane2,-1000);
            show++;
            num.setText("2/3");
        }else if(show==1){
            transplateAnimation(0.5,pane3,-1000);
            show++;
            num.setText("3/3");
        }

    }

    @FXML
    void back(ActionEvent event) {
        if(show==1)
        {
            transplateAnimation(0.5,pane2,1000);
            show--;
            num.setText("1/3");
        }else if(show==2){
            transplateAnimation(0.5,pane3,1000);
            show--;
            num.setText("2/3");
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

    public void showEvents(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/testuser1/getEventFront1.fxml"));
        root = loader.load();
        scene = new Scene(root);
        primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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


