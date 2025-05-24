package controllers.manager;

import java.sql.Connection;
import java.util.Optional;

import application.Main;
import data.DataBase;
import data.SessionManager;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;


public class ManagerTeacherController {
    @FXML private BorderPane rootPane;
    @FXML private Text username, role, menuTechLend;
    @FXML private ImageView menuImage;

    @FXML private TableView<User> tableTeachers;
    @FXML private TableColumn<User, String> nombre;
    @FXML private TableColumn<User, String> correo;
    @FXML private TableColumn<User, String> rol;
    @FXML private TableColumn<User, String> programa;
    @FXML private TableColumn<User, String> identificacion;
    @FXML private TableColumn<User, String> contacto;
    @FXML private GridPane gridPane;

	private SessionManager sessionManager = SessionManager.getInstance();

    @FXML public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        
    	username.setText(sessionManager.getName());
    	role.setText(sessionManager.getRole());
    }
    @FXML public void docentes() {
    	Main.cargarGrid("/views/Manager/Manager.fxml", rootPane);
    }
    
    @FXML public void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
		Main.loadView("/views/Login.fxml");
    }

}
