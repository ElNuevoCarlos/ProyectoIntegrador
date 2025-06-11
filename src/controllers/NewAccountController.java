package controllers;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

import data.DataBase;
import data.EmailService;
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
import utils.SecurityUtils;
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
		String FirstName = name.getText().trim();
		String LastName = lastName.getText().trim();
		String TI = ti.getValue() != null ? ti.getValue().trim() : "";
		String NumIdentification = numIdentification.getText().trim();
		String Pro_dep = pro_dep.getText().trim();
		String Phone = phone.getText().trim();
		String Email = email.getText().trim();
		String Role = role.getValue() != null ? role.getValue().trim() : "";
		String Password1 = password1.getText().trim();
		String Password2 = password2.getText().trim();
		
        if (FirstName.isEmpty() || 
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
        String affair = "¡Cuenta creada con éxito en TECHLEND!";
        String htmlBody = String.format("""
        		<!DOCTYPE html>
        		<html>
        		<head>
        		  <meta charset="UTF-8">
        		  <title>Confirmación de creación de cuenta</title>
        		</head>
        		<body style="margin:0;padding:0;font-family:'Segoe UI',Roboto,sans-serif;background-color:#f5f6fa;">

        		  <div style="max-width:600px;margin:0 auto;background-color:#ffffff;border-radius:12px;overflow:hidden;">
        		    <div style="background-color:#0b66ff;padding:30px;text-align:center;">
        		      <img src="https://img.icons8.com/ios-filled/100/ffffff/checked--v1.png" alt="icono" style="width:60px;height:60px;" />
        		      <h1 style="color:#ffffff;margin-top:10px;font-size:24px;">TECHLEND</h1>
        		    </div>

        		    <div style="padding:40px 30px;text-align:center;">
        		      <h2 style="color:#222;font-size:22px;margin-bottom:20px;">Cuenta creada con éxito</h2>
        		      <p style="color:#555;font-size:16px;">Hola, <strong>%s 👋</strong></p>
        		      <p style="color:#777;font-size:15px;">
        		        Tu cuenta ha sido creada correctamente. Ahora puedes iniciar sesión y comenzar a usar TECHLEND.
        		      </p>

        		      <div style="margin:30px auto;width:max-content;background:linear-gradient(90deg,#7ee8fa,#80ff72,#8ec5fc);padding:4px;border-radius:14px;">
        		        <div style="padding:12px 30px;background-color:white;border-radius:10px;">
        		          <span style="font-size:20px;color:#0b66ff;font-weight:bold;">¡Bienvenido a la comunidad TECHLEND!</span>
        		        </div>
        		      </div>

        		      <hr style="margin:30px 0;border:none;border-top:1px solid #eee;">

        		      <p style="font-size:14px;color:#444;">
        		        Si no fuiste tú, por favor 
        		        <a href="mailto:techlend27@gmail.com?subject=Cuenta%%20creada%%20no%%20solicitada&body=Hola%%2C%%20quiero%%20reportar%%20un%%20problema%%20con%%20la%%20creaci%%C3%%B3n%%20de%%20cuenta." 
        		           style="color:#0b66ff;text-decoration:none;">
        		          contáctanos aquí.
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
        		""", FirstName);


	    CompletableFuture.runAsync(() -> {
	        EmailService.sendMessage(Email, affair, htmlBody);
	    });
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
        		try {
        		    String encryptedPassword = SecurityUtils.encrypt(Password1);
        		    String Name = FirstName + " " + LastName;
        		    User newUser = new User(
        		        Name, NumIdentification, TI, Email, 
        		        Pro_dep, Phone, "ACTIVA", Role, encryptedPassword, null
        		    );
        		    userDao.save(newUser);

        		    Stage currentStage = (Stage) rootPane.getScene().getWindow();
        		    currentStage.close();
        		    ViewUtils.loadView("/views/Login.fxml");

        		} catch (Exception e) {
        		    e.printStackTrace();
        		    ViewUtils.AlertWindow("Error", "Cifrado fallido", "Ocurrió un error al procesar la contraseña. Inténtalo de nuevo.", AlertType.ERROR);
        		}
        		
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
