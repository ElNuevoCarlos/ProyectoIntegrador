package controllers;

import application.*;
import data.SessionManager;
import data.UserDataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import model.User;

public class LoginController {
    @FXML private BorderPane rootPane;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Button button;
    
    private UserDataManager userManager = UserDataManager.getInstance();
    private SessionManager sessionManager = SessionManager.getInstance();
    private String Email, Password;
    
    @FXML public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        
        //Para poder dar enter
        email.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                button.fire();
            }
        });
        
      //Para poder dar enter
        password.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                button.fire(); 
            }
        });
    }
    
    @FXML public void handlePassword() {
    	Password = password.getText();
    	System.out.println("handlePassword "+Password);
    	handleVerify(Email, Password);
    }
    
    @FXML public void handleEmail() {
        Email = email.getText();
        email.setVisible(false);
        password.setVisible(true);
        button.setText("Entrar");
        button.setOnAction(event -> handlePassword());
    }
    
    public void handleVerify(String Email, String Password) {
    	
    	if (Email.isBlank() || Password.isBlank()) {
            this.AlertWindow("Todos los campos deben llenarse.", null, AlertType.WARNING);
            return;
        }
		
        for (User userx : userManager.getUsers()) {
            if (Email.equals(userx.email) && Password.equals(userx.password)) {
            	sessionManager.setUser(userx.name, userx.role);
            	if (userx.role.equals("Profesor")) Main.loadView("/views/MenuTeacher.fxml");
            	else Main.loadView("/views/MenuAdministrative.fxml");
                return;
            }
        }
        email.setVisible(true);
        password.setVisible(false);
        email.setText("");
        button.setText("Siguiente");
        
    	this.Email = "";
    	this.Password = "";
    	
        this.AlertWindow("El correo o contraseña no es correcto.", "Verifique que todos los datos esten bien y vuelva a intentarlo.", AlertType.ERROR);
    }
    
    private void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
