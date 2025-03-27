package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MenuController {

    @FXML private BorderPane rootPane;
    @FXML private Label user, job;

    // Me ayuda a poder quitarle el focus a los TextField -
    // dandole click a cualquier parte del panel
    
    @FXML 
    public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    }

    public void recibirDatos(model.User user) {
        this.user.setText(user.name.toUpperCase());
        this.job.setText(user.role);
    }
}
