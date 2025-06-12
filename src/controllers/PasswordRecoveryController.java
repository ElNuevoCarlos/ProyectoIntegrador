package controllers;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

import data.DataBase;
import data.EmailService;
import java.security.SecureRandom;
import data.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.UserSession;
import utils.ViewUtils;

public class PasswordRecoveryController {
	@FXML private BorderPane rootPane;
	@FXML private Text title, name, codeSend, text1;
	@FXML private TextField code, newPassword, confirmPassword;
	@FXML private Button button;
	@FXML private Hyperlink forward, returnLogin; 
	
    private Connection database = DataBase.getInstance().getConnection();
    
    @SuppressWarnings("exports")
    public UserSession userSession = UserSession.getInstance();
    private UserDAO userDao = new UserDAO(database);
    
    private String Name, Email, generatedCode, password1, password2;
    
    String generateRecoveryCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    
	@FXML void initialize() {
		this.Name = userSession.getName();
		this.Email = userSession.getEmail();
		name.setText(Name);
	}
	
	@FXML void handleReturnLogin() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
	}
	
	@FXML void handleSend() {
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
	    String htmlBody = String.format("""
	    		<!DOCTYPE html>
	    		<html>
	    		<head>
	    		  <meta charset="UTF-8">
	    		  <title>Confirmación de cambio de contraseña</title>
	    		</head>
	    		<body style="margin:0;padding:0;font-family:'Segoe UI',Roboto,sans-serif;background-color:#f5f6fa;">

	    		  <div style="max-width:600px;margin:0 auto;background-color:#ffffff;border-radius:12px;overflow:hidden;">
	    		    <div style="background-color:#0b66ff;padding:30px;text-align:center;">
	    		      <img src="https://img.icons8.com/ios-filled/100/ffffff/security-checked.png" alt="icono" style="width:60px;height:60px;" />
	    		      <h1 style="color:#ffffff;margin-top:10px;font-size:24px;">TECHLEND</h1>
	    		    </div>

	    		    <div style="padding:40px 30px;text-align:center;">
	    		      <h2 style="color:#222;font-size:22px;margin-bottom:20px;">Confirma tu cambio de contraseña</h2>
	    		      <p style="color:#555;font-size:16px;">Hola, <strong>%s 👋</strong></p>
	    		      <p style="color:#777;font-size:15px;">Introduce este código en la aplicación para continuar:</p>

	    		      <div style="margin:30px auto;width:max-content;background:linear-gradient(90deg,#7ee8fa,#80ff72,#8ec5fc);padding:4px;border-radius:14px;">
	    		        <div style="padding:12px 30px;background-color:white;border-radius:10px;">
	    		          <span style="font-size:28px;color:#0b66ff;font-weight:bold;letter-spacing:5px;">%s</span>
	    		        </div>
	    		      </div>

	    		      <p style="color:#999;font-size:14px;">Este código expira en 10 minutos.</p>

	    		      <hr style="margin:30px 0;border:none;border-top:1px solid #eee;">

	    		      <p style="font-size:14px;color:#444;">¿No fuiste tú? 
	    		        <a href="mailto:techlend27@gmail.com?subject=Reporte%%20de%%20cambio%%20de%%20contraseña%%20no%%20solicitado&body=Hola%%2C%%20quiero%%20reportar%%20un%%20problema%%20con%%20el%%20cambio%%20de%%20contraseña." 
	    		           style="color:#0b66ff;text-decoration:none;">
	    		          Contáctanos aquí.
	    		        </a>
	    		      </p>
	    		    </div>

	    		    <div style="background-color:#1e1e1e;padding:20px;text-align:center;color:#999;font-size:13px;border-bottom-left-radius:12px;border-bottom-right-radius:12px;">
	    		      © 2025 TECHLEND. Todos los derechos reservados.<br>
	    		      Este correo fue generado automáticamente. No respondas a este mensaje.
	    		    </div>
	    		  </div>

	    		</body>
	    		</html>
	    		""", Name, generatedCode);




	    CompletableFuture.runAsync(() -> {
	        EmailService.sendMessage(Email, affair, htmlBody);
	    });
	}

	void handleVerifyCode() {
	    String Code = code.getText().trim();
	    if (Code.equals(generatedCode)) {
	        handleRestore();
	    } else {
	    	ViewUtils.AlertWindow(null, "Código Incorrecto",
	                "El código de recuperación que introdujiste es incorrecto. Inténtalo nuevamente.",
	                AlertType.ERROR);
	    }
	}
	
	@FXML void handleRestore() {
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
			ViewUtils.AlertWindow(null, "Error", "Por favor, complete todos los campos.", AlertType.WARNING);
			return;
		}
	    if (password1.equals(password2)) {
	        if (!password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*]).{8,20}$")) {
	        	ViewUtils.AlertWindow(null,
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
		            
		            ViewUtils.AlertWindow(null, "Éxito", "Tu contraseña se actualizó correctamente", AlertType.INFORMATION);
		            ViewUtils.loadView("/views/Login.fxml");
		        } else {
		        	ViewUtils.AlertWindow(null, "Error", "No se pudo actualizar la contraseña", AlertType.ERROR);
		        }
	        }
	    } else {
	    	ViewUtils.AlertWindow(null, "Contraseñas no coinciden",
	                "Las contraseñas ingresadas no son iguales. Por favor, verifícalas.",
	                AlertType.WARNING);
	    }
	}

	@FXML void handleForward() {
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
}
