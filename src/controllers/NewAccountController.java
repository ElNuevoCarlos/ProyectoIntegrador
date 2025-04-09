package controllers;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewAccountController {
	@FXML private BorderPane rootPane;
	@FXML private Button again;
	@FXML
	private ComboBox<String> TI;
	
	@FXML
	public void initialize() {
	    ObservableList<String> items = FXCollections.observableArrayList(
	        "item-1", "item-2", "item-3", "item-4", "item-5"
	    );
	    TI.setItems(items);
	}

	
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
