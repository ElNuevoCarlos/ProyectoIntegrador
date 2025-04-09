package controllers;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewAccountController {
	@FXML private BorderPane rootPane;
	@FXML private ComboBox<String> ti;
	@FXML private ComboBox<String> rol;
    @FXML
    private TextField apellidos;

    @FXML
    private TextField correo;

    @FXML
    private Hyperlink login;

    @FXML
    private Button newAccount;

    @FXML
    private TextField nombre;

    @FXML
    private TextField numIdentificacion;

    @FXML
    private TextField password1;

    @FXML
    private TextField password2;

    @FXML
    private TextField pro_dep;

    @FXML
    private TextField telefono;

    @FXML
    private TextField username;
	
	@FXML
	public void initialize() {
		rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
	    ObservableList<String> itemsTI = FXCollections.observableArrayList(
	        "CC", "CE"
	    );
	    ti.setItems(itemsTI);

	    ObservableList<String> itemsRol = FXCollections.observableArrayList(
	        "Docente", "Administrativo"
	    );
	    rol.setItems(itemsRol);
	}
	
    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

	@FXML void handleNewAccount() {
		String Nombre = nombre.getText().trim();
		String Apellidos = apellidos.getText().trim();
		String TI = ti.getValue();
		String NumIdentificacion = numIdentificacion.getText().trim();
		String Pro_dep = pro_dep.getText().trim();
		String Telefono = telefono.getText().trim();
		String Correo = correo.getText().trim();
		String Username = username.getText().trim();
		String Rol = rol.getValue();
		String Password1 = password1.getText().trim();
		String Password2 = password2.getText().trim();
		
        if (Nombre.isEmpty() || 
        		Apellidos.isEmpty() || 
        		TI.isEmpty() || 
        		NumIdentificacion.isEmpty() || 
        		Pro_dep.isEmpty() || 
        		Telefono.isEmpty() || 
        		Correo.isEmpty() || 
        		Username.isEmpty() || 
        		Rol.isEmpty() || 
        		Password1.isEmpty() || 
        		Password2.isEmpty()){
        	
            mostrarAlerta("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }
	}
	
	@FXML void handleLogin() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
    	Main.loadView("/views/Login.fxml");
	}
}
