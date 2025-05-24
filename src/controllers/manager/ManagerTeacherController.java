package controllers.manager;

import application.Main;
import data.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
