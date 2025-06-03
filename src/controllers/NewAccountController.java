package controllers;

import java.sql.Connection;

import data.DataBase;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
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
	@FXML private ComboBox<String> ti, role;
    @FXML private TextField name, lastName, numIdentification, pro_dep, phone, email, password1, password2;
    @FXML private Hyperlink login;
    @FXML private Button newAccount;
    
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
	
	@FXML void initialize() {
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
        	
        	ViewUtils.AlertWindow("Error", "Campos vacíos", "Por favor, complete todos los campos.", AlertType.ERROR);
            return;
        }
        String regexEmail = "^[a-zA-Z0-9._%+-]+@udi\\.edu\\.co$";
        if (!Email.matches(regexEmail)) {
        	ViewUtils.AlertWindow("Error", "Correo inválido", "El correo debe tener el formato usuario@udi.edu.co", AlertType.ERROR);
        	return;
        }
        if (!NumIdentification.matches("\\d{6,10}")) {
        	ViewUtils.AlertWindow("Error", "Número inválido", "Debe ingresar entre 6 y 10 dígitos numéricos.", AlertType.ERROR);
        	return;
        }
        if (!Phone.matches("\\d{10}")) {
        	ViewUtils.AlertWindow("Error", "Número inválido", "Debe ingresar exactamente 10 dígitos numéricos.", AlertType.ERROR);
        	return;
        }
        if (Password1.equals(Password2)) {
        	if (!Password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$")) {
        		ViewUtils.AlertWindow("Formato inválido", "Contraseña insegura", 
                        "La contraseña debe tener:\n" +
                        "- Entre 8 y 20 caracteres\n" +
                        "- Al menos una letra mayúscula\n" +
                        "- Al menos una letra minúscula\n" +
                        "- Al menos un número\n" +
                        "- Al menos un carácter especial (@#$%^&+=!)", AlertType.ERROR);
                    return;
        	} else {
        		String fullName = Name +" "+ LastName;
        		User newUser = new User(fullName, NumIdentification, TI, Email, Pro_dep, Phone, "ACTIVA", Role, Password1, null);
        		userDao.save(newUser);
        	}
        } else {
        	ViewUtils.AlertWindow("Error", "Contraseñas no coinciden", "Las contraseñas ingresadas no son iguales. Por favor, verifíquelas.", AlertType.ERROR);
        	return;
        }
	}
	
	@FXML void handleLogin() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
	}
}
