package controllers.manager;

import application.Main;
import data.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import utils.ViewUtils;


public class MenuController {
    @FXML private BorderPane rootPane;
    @FXML private Text username, role;
    @FXML private ImageView menuImage;
    @FXML private TableView<User> tableTeachers;
    @FXML private TableColumn<User, String> nombre;
    @FXML private TableColumn<User, String> correo;
    @FXML private TableColumn<User, String> rol;
    @FXML private TableColumn<User, String> programa;
    @FXML private TableColumn<User, String> identificacion;
    @FXML private TableColumn<User, String> contacto;

	private SessionManager sessionManager = SessionManager.getInstance();

    @FXML void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());

	    String[] partes = sessionManager.getName().split(" ");
	    String primerNombre = partes[0];
	    String primerApellido = partes.length > 2 ? partes[partes.length - 2] : partes[1];
        
    	username.setText(primerNombre + " " +primerApellido);
    	role.setText(sessionManager.getRole());
    	Main.rootLayout = rootPane;
    }
    @FXML void userInfo() {
    	ViewUtils.cargarGrid("/views/MyAccount.fxml", rootPane);
    }
    @FXML void docentes() {
    	ViewUtils.cargarGrid("/views/Manager/Manager.fxml", rootPane);
    }
    @FXML void sanciones() {
    	ViewUtils.cargarGrid("/views/Manager/Sanction.fxml", rootPane);
    }
    @FXML void prestamos() {
    	ViewUtils.cargarGrid("/views/Manager/MenuLoands.fxml", rootPane);
    }
    @FXML void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
    }

}
