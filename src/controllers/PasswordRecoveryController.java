package controllers;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

import application.Main;
import data.DataBase;
import data.EmailService;
import java.security.SecureRandom;
import data.SessionManager;
import data.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PasswordRecoveryController {
	@FXML private BorderPane rootPane;
	@FXML private Text title, name, codeSend, text1;
	@FXML private TextField code, newPassword, confirmPassword;
	@FXML private Button button;
	@FXML private Hyperlink forward, returnLogin; 
	
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
    
    private String Name, Email, generatedCode, password1, password2;
    
    String generateRecoveryCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    
	@FXML
	public void initialize() {
		this.Name = SessionManager.getInstance().getName();
		this.Email = SessionManager.getInstance().getEmail();
		name.setText(Name);
	}
	
	@FXML
	void handleReturnLogin() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
		Main.loadView("/views/Login.fxml");
	}
	
	@FXML
	void handleSend() {
	    generatedCode = generateRecoveryCode();

	    // Primero actualizamos la interfaz
	    code.setVisible(true);
	    forward.setVisible(true);
	    name.setVisible(false);
	    codeSend.setVisible(true);
	    text1.setText("Hemos enviado el código a\n"+Email);
	    returnLogin.setVisible(false);
	    title.setText("Introduce el código de seguridad");
	    button.setText("Continuar");
	    button.setOnAction(event -> handleVerifyCode());

	    // Después enviamos el correo en segundo plano
	    String affair = "Tu código de cambio de contraseña es " + generatedCode;
	    String body = "Un paso más para cambiar tu contraseña\n\n" +
	            "Hola, " + Name + ":\n" +
	            "Hemos recibido tu solicitud de cambio de contraseña. Introduce este código en TECHLEND:\n" +
	            generatedCode + "\n" +
	            "No compartas este código con nadie.\n\n" +
	            "Si alguien solicita este código\n" +
	            "No compartas este código con nadie, especialmente si te dice que trabaja para TECHLEND. Puede que estén intentando piratear tu cuenta.";

	    CompletableFuture.runAsync(() -> {
	        EmailService.sendMessage(Email, affair, body);
	    });
	}


	
	void handleVerifyCode() {
	    String Code = code.getText().trim();
	    if (Code.equals(generatedCode)) {
	        handleRestore();
	    } else {
	    	AlertWindow("Código Incorrecto",
	                "El código de recuperación que introdujiste es incorrecto. Inténtalo nuevamente.",
	                AlertType.ERROR);
	    }
	}
	
	@FXML
	void handleRestore() {
		text1.setVisible(false);
		forward.setVisible(false);
		code.setVisible(false);
		title.setText("Elige una contraseña nueva");
		name.setText("Crea una contraseña entre 8 y 20 caracteres.\r\n"
				+ "Una contraseña segura debe incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial (@ # $ % ^ & + = !).");
		newPassword.setVisible(true);
		confirmPassword.setVisible(true);
		System.out.println("RSETORE");
		button.setOnAction(event -> handleVerifyPassword());
	}
	
	void handleVerifyPassword() {
		password1 = newPassword.getText().trim();
		password2 = confirmPassword.getText().trim();
		System.out.println("VERIFYPASSWORD");
		System.out.println(password1+" "+password2);
		if(password1.isEmpty() || password2.isEmpty()) {
			AlertWindow("Error", "Por favor, complete todos los campos.", AlertType.WARNING);
			return;
		}
	    if (password1.equals(password2)) {
	        if (!password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*]).{8,20}$")) {
	        	AlertWindow(
	        		    "Formato inválido", 
	        		    "La contraseña debe tener:\n" +
	        		    "- Entre 8 y 20 caracteres\n" +
	        		    "- Al menos una letra mayúscula\n" +
	        		    "- Al menos una letra minúscula\n" +
	        		    "- Al menos un número\n" +
	        		    "- Al menos un carácter especial (@#$%^&+=!)", 
	        		    AlertType.WARNING
	        		);
	            return;
	        } else {
		        boolean updated = userDao.updatePassword(Email, password1);
		        if (updated) {
		        	// CIERRA LA VENTANA ACTUAL
		            Stage currentStage = (Stage) rootPane.getScene().getWindow();
		            currentStage.close();
		            
		            AlertWindow("Éxito", "Tu contraseña se actualizó correctamente", AlertType.INFORMATION);
		            Main.loadView("/views/Login.fxml");
		        } else {
		        	AlertWindow("Error", "No se pudo actualizar la contraseña", AlertType.ERROR);
		        }
	        }
	    } else {
	        AlertWindow("Contraseñas no coinciden",
	                "Las contraseñas ingresadas no son iguales. Por favor, verifícalas.",
	                AlertType.WARNING);
	    }
	}

	
	@FXML
	void handleForward() {
	    generatedCode = generateRecoveryCode();
	    String affair = "Tu código de cambio de contraseña es " + generatedCode;
	    String body = "Un paso más para cambiar tu contraseña\n\n" +
	            "Hola, " + Name + ":\n" +
	            "Hemos recibido tu solicitud de cambio de contraseña. Introduce este código en TECHLEND:\n" +
	            generatedCode + "\n" +
	            "No compartas este código con nadie.\n\n" +
	            "Si alguien solicita este código\n" +
	            "No compartas este código con nadie, especialmente si te dice que trabaja para TECHLEND. Puede que estén intentando piratear tu cuenta.";

	    // Ejecuta el envío del correo en un hilo aparte
	    CompletableFuture.runAsync(() -> {
	        EmailService.sendMessage(Email, affair, body);
	        System.out.println("Reenviado");
	    });
	}

	
    private void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
