package com.example.testuser1;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import services.EventService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AnchorPaneNode extends AnchorPane{

    private EventService es = new EventService();

    // Date associated with this pane
    private LocalDate date;

    public AnchorPaneNode(Node... children) {
        super(children);

        // Add action handler for mouse clicked
        this.setOnMouseClicked(e ->
        {
            System.out.println("This pane's date is: " + date);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Événements pour le " + date);
            alert.setHeaderText(null);
            try {
                List<String> eventNames = es.getByDate(Date.valueOf(date));

                if (eventNames != null && !eventNames.isEmpty()) {
                    // Hauteur fixe par élément
                    double cellHeight = 24.0; // Vous pouvez ajuster cette valeur selon vos besoins

                    // Calcul de la hauteur totale du ListView
                    double totalHeight = Math.min(eventNames.size() * cellHeight, 200.0); // Limitez à une hauteur maximale, par exemple, 200.0

                    ListView<String> listView = new ListView<>();
                    listView.setPrefHeight(totalHeight); // Définissez la hauteur du ListView

                    listView.getItems().addAll(eventNames);

                    // Ajoutez ListView à la boîte de dialogue
                    alert.getDialogPane().setContent(listView);
                } else {
                    alert.setContentText("Aucun événement pour cette date.");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            alert.showAndWait();
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


