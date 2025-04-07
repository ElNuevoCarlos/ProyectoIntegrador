package controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewAccountController {
	@FXML private BorderPane rootPane;
	@FXML private Button again;
	
	@FXML void handleName() {
		
	}
	@FXML void handleNewAccount() {
		
	}
	
	@FXML void handleAgainLogin() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
    	Main.loadView("/views/Login.fxml");
	}
}
