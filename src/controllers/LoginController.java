package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.*;
import data.Oracle;
import data.SessionManager;
import data.UserDataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import model.User;

public class LoginController {
    @FXML private BorderPane rootPane;
    @FXML private TextField email;
    @FXML private PasswordField password;
    
    private UserDataManager userManager = UserDataManager.getInstance();
    private SessionManager sessionManager = SessionManager.getInstance();
    private String Email, Password;

    @FXML 
    public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    }

    @FXML 
    public void handleJoinLogin() throws IOException, SQLException {
        Email = email.getText();
        Password = password.getText();

        if (Email.isBlank() || Password.isBlank()) {
            this.AlertWindow("Todos los campos deben llenarse.", null, AlertType.WARNING);
            return;
        }
		
        for (User userx : userManager.getUsers()) {
            if (Email.equals(userx.email) && Password.equals(userx.password)) {
            	sessionManager.setUser(userx.name, userx.role);
                Main.loadView("/views/Menu.fxml");
                return;
            }
        }

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
