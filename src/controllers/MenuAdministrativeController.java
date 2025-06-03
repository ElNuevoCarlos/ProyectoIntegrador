package controllers;

import data.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MenuAdministrativeController {

    @FXML private BorderPane rootPane;
    @FXML private Label user, job;
    
	private SessionManager sessionManager = SessionManager.getInstance();
    
    @FXML 
    public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        data();
    }
    
    public void data() {
        this.user.setText(sessionManager.getName());
        this.job.setText(sessionManager.getRole());
    }
    
}
