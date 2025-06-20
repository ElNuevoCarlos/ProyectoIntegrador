package controllers;

import java.sql.Connection;

import application.Main;
import data.DataBase;
import data.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.UserSession;
import utils.ViewUtils;

public class LoginController {
    @FXML private Text title;
    @FXML private Hyperlink hyperlink;
    @FXML private BorderPane rootPane;
    @FXML private PasswordField password;
    @FXML private ImageView passwordview;
    @FXML private Button button, newAccount;
    @FXML private TextField email, passwordVisible;

    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);

    private String Email, Password, Name;

    @FXML
    void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());

        email.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                button.fire();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                button.fire();
            }
        });
    }

    @FXML
    void handlePasswordView() {
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

    @FXML
    void handleNewAccount() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/NewAccount.fxml");
    }

    @FXML
    void handleMailRecovery() {
        ViewUtils.AlertWindow(null,
            "¿Olvidaste tu correo?", 
            "Para recuperar tu correo institucional:\n\n" +
            "1. Visita la página:\nhttps://web.udi.edu.co/\n\n" +
            "2. Haz clic en el botón \"Correo Institucional\".",  
            AlertType.INFORMATION
        );
    }

    @FXML
    void handleEmail() {
        Email = email.getText();
        if (Email.isBlank()) {
            ViewUtils.AlertWindow(null, "El campo debe llenarse", null, AlertType.WARNING);
            return;
        }
        Name = userDao.verifyEmail(Email);
        if (Name != null) {
            email.setVisible(false);
            password.setVisible(true);
            passwordview.setVisible(true);
            newAccount.setVisible(false);
            hyperlink.setText("¿Acaso has olvidado tú contraseña?");

            hyperlink.setOnAction(event -> {
                Stage currentStage = (Stage) rootPane.getScene().getWindow();
                currentStage.close();
                ViewUtils.loadView("/views/PasswordRecovery.fxml");
            });

            button.setLayoutX(button.getLayoutX() - 60);
            button.setText("Entrar");
            button.setOnAction(event -> handlePassword());
        } else {
            ViewUtils.AlertWindow(null,
                "El nombre no está registrado.",
                "Verifique que el nombre es correcto.",
                AlertType.ERROR
            );
        }
    }

    @FXML
    void handlePassword() {
        Password = password.getText();
        if (Password.isBlank()) {
            ViewUtils.AlertWindow(null, "El campo debe llenarse", null, AlertType.WARNING);
            return;
        }

        String[] verificationResult = userDao.verifyPassword(Email, Password);
        String verification = verificationResult[0];
        String role = verificationResult[1];
        Long id = Long.parseLong(verificationResult[2]);

        if (verification.equals("true")) {
            // Cierra la ventana actual
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();
            // Actualiza la sesión con datos completos
            UserSession.getInstance(id, Name, role, Email);

            // Redirige según el rol
            switch (role) {
                case "ENCARGADO":
                    ViewUtils.loadView("/views/Manager/Menu.fxml");
                    break;
                case "SUPERENCARGADO":
                    Main.isSuperManager = true;
                    ViewUtils.loadView("/views/Manager/Menu.fxml");
                    break;
                default:
                    ViewUtils.loadView("/views/Teacher/Menu.fxml");
            }
        } else {
            ViewUtils.AlertWindow(null,
                null,
                "La contraseña es incorrecta.",
                AlertType.ERROR
            );
        }
    }
}

