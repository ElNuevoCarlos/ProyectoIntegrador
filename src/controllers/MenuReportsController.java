package controllers;

import application.Main;
import data.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuReportsController {
	private SessionManager sessionManager = SessionManager.getInstance();
	@FXML private BorderPane rootPane;
    @FXML private Label user, role;
    
    @FXML public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        data();
    }
    
    @FXML void handleHall(ActionEvent event) {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
    	Main.loadView("/views/MenuTeacher.fxml");
    }
    @FXML void handleLogout(ActionEvent event) {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
    	Main.loadView("/views/Login.fxml");
    }
    
    public void data() {
        this.user.setText(sessionManager.getName());
        this.role.setText(sessionManager.getRole());
    }
}
