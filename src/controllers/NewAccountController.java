package controllers;

import java.sql.Connection;

import data.DataBase;
import data.UserDAO;
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
import model.User;
import utils.ViewUtils;

public class NewAccountController {
	@FXML private BorderPane rootPane;
    @FXML private TextField name;
    @FXML private TextField lastName;
	@FXML private ComboBox<String> ti;
    @FXML private TextField numIdentification;
    @FXML private TextField pro_dep;
    @FXML private TextField phone;
    @FXML private TextField email;
	@FXML private ComboBox<String> role;
    @FXML private TextField password1;
    @FXML private TextField password2;
    @FXML private Hyperlink login;
    @FXML private Button newAccount;
    
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
	
	@FXML
	public void initialize() {
		rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
	    ObservableList<String> itemsTI = FXCollections.observableArrayList(
	        "CC", "CE"
	    );
	    ti.setItems(itemsTI);

	    ObservableList<String> itemsRol = FXCollections.observableArrayList(
	        "DOCENTE", "ADMINISTRATIVO"
	    );
	    role.setItems(itemsRol);
	}
	
    private void showAlert(String title, String head, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(message);
        alert.showAndWait();
    }

	@FXML void handleNewAccount() {
		String Name = name.getText().trim();
		String LastName = lastName.getText().trim();
		String TI = ti.getValue() != null ? ti.getValue().trim() : "";
		String NumIdentification = numIdentification.getText().trim();
		String Pro_dep = pro_dep.getText().trim();
		String Phone = phone.getText().trim();
		String Email = email.getText().trim();
		String Role = role.getValue() != null ? role.getValue().trim() : "";
		String Password1 = password1.getText().trim();
		String Password2 = password2.getText().trim();
		
        if (Name.isEmpty() || 
        		LastName.isEmpty() || 
        		TI.isEmpty() || 
        		NumIdentification.isEmpty() || 
        		Pro_dep.isEmpty() || 
        		Phone.isEmpty() || 
        		Email.isEmpty() || 
        		Role.isEmpty() || 
        		Password1.isEmpty() || 
        		Password2.isEmpty()){
        	
        	showAlert("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }
        String regexEmail = "^[a-zA-Z0-9._%+-]+@udi\\.edu\\.co$";
        if (!Email.matches(regexEmail)) {
        	showAlert("Error", "Correo inválido", "El correo debe tener el formato usuario@udi.edu.co");
        	return;
        }
        if (!NumIdentification.matches("\\d{6,10}")) {
        	showAlert("Error", "Número inválido", "Debe ingresar entre 6 y 10 dígitos numéricos.");
        	return;
        }
        if (!Phone.matches("\\d{10}")) {
        	showAlert("Error", "Número inválido", "Debe ingresar exactamente 10 dígitos numéricos.");
        	return;
        }
        if (Password1.equals(Password2)) {
        	if (!Password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$")) {
                showAlert("Formato inválido", "Contraseña insegura", 
                        "La contraseña debe tener:\n" +
                        "- Entre 8 y 20 caracteres\n" +
                        "- Al menos una letra mayúscula\n" +
                        "- Al menos una letra minúscula\n" +
                        "- Al menos un número\n" +
                        "- Al menos un carácter especial (@#$%^&+=!)");
                    return;
        	} else {
        		String fullName = Name +" "+ LastName;
        		User newUser = new User(fullName, NumIdentification, TI, Email, Pro_dep, Phone, "ACTIVA", Role, Password1, null);
        		userDao.save(newUser);
        	}
        } else {
        	showAlert("Error", "Contraseñas no coinciden", "Las contraseñas ingresadas no son iguales. Por favor, verifíquelas.");
        	return;
        }
	}
	
	@FXML void handleLogin() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
        ViewUtils.loadView("/views/Login.fxml");
	}
}
