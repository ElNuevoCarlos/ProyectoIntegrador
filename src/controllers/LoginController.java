package controllers;

import java.sql.Connection;

import application.*;
import data.DataBase;
import data.SessionManager;
import data.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
    @FXML private BorderPane rootPane;
    @FXML private TextField username, passwordVisible;
    @FXML private Text title;
    @FXML private PasswordField password;
    @FXML private Button button, passwordview, newAccount;
    @FXML private Hyperlink hyperlink;
     
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
    
	private SessionManager sessionManager = SessionManager.getInstance();
    private String Username, Password;
    
    @FXML void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        
        //Para poder dar enter
        username.setOnKeyPressed(event -> {
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
    @FXML void handlePasswordView() {
        if (password.isVisible()) {
            password.setVisible(false);
            passwordVisible.setDisable(true);
            passwordVisible.setText(password.getText());
            passwordVisible.setVisible(true);
        } else {
            password.setVisible(true);
            passwordVisible.setVisible(false);
        }
    }
    
    @FXML void handleNewAccount() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
        Main.loadView("/views/NewAccount.fxml");
    }
    
    @FXML void handleName() {
    	Username = username.getText();
    	if (Username.isBlank()) {
            this.AlertWindow("El campo debe llenarse", null, AlertType.WARNING);
            return;
        }
    	String name = userDao.verifyUser(Username);
    	if (name != null) {
        	username.setVisible(false);
            password.setVisible(true);
            passwordview.setVisible(true);
            newAccount.setVisible(false);
            hyperlink.setText("¿Has olvidado tú contraseña?");
            title.setText(name);
            button.setText("Entrar");
            button.setOnAction(event -> handlePassword());
    	} else this.AlertWindow(
    			"El nombre no está registrado.", 
    			"Verifique que el nombre es correcto.", 
    			AlertType.ERROR);
    }
    @FXML void handlePassword() {
    	Password = password.getText();
    	if (Password.isBlank()) {
            this.AlertWindow("El campo debe llenarse", null, AlertType.WARNING);
            return;
        }
    	String[] verificationResult = userDao.verifyPassword(Username, Password);
    	String verification = verificationResult[0];
    	System.out.println(verification);
    	String role = verificationResult[1];
    	System.out.println(role);
		if (verification.equals("Y")) {
			
        	// CIERRA LA VENTANA ACTUAL
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();
            // CIERRA LA VENTANA ACTUAL
            sessionManager.setUser(Username, role);
            if (role.equals("DOCENTE") || role.equals("ADMINISTRATIVO")) {
            	Main.loadView("/views/TeacherAdmin.fxml");
            } else if (role.equals("ENCARGADO")) {
            	Main.loadView("/views/Manager.fxml");
            } else {
            	// Si no es ninguna, entonces es el superencargado
            	Main.loadView("/views/SuperManager.fxml");
            }
		} else this.AlertWindow(
    			null, 
    			"La contraseña es incorrecta.", 
    			AlertType.ERROR);
    }
    
    private void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
