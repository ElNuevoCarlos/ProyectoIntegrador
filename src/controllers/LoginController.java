package controllers;

import java.io.IOException;
import java.util.ArrayList;

import application.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

public class LoginController {
    ArrayList<User> users = new ArrayList<>();
    @FXML private BorderPane rootPane;
    @FXML private TextField email;
    @FXML private PasswordField password;

    private String Email, Password;

    @FXML 
    public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    }

    @FXML 
    public void handleJoinLogin() throws IOException {
        users.add(new User("1095788069", "yesid", "cpinto5@udi.edu.co", "3112685855", "Alto", "yesid123", "Profesor"));
        users.add(new User("1095788069", "carlos", "cpinto5@udi.edu.co", "3112685855", "Alto", "carlos123", "Personal Administrativo"));

        Email = email.getText();
        Password = password.getText();

        if (Email.isBlank() || Password.isBlank()) {
            this.AlertWindow("Todos los campos deben llenarse.", null, AlertType.WARNING);
            return;
        }

        for (User userx : users) {
            if (Email.equals(userx.email) && Password.equals(userx.password)) {
                cambiarEscenaConDatos(userx);
                return;
            }
        }

        this.AlertWindow("El correo o contraseña no es correcto.", "Verifique que todos los datos esten bien y vuelva a intentarlo.", AlertType.ERROR);
    }

    private void cambiarEscenaConDatos(User userx) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
        Parent root = loader.load();

        MenuController controller = loader.getController();
        controller.recibirDatos(userx);

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
