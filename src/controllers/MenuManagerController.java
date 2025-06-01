package controllers;

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


public class MenuManagerController {
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
  
	private SessionManager sessionManager = SessionManager.getInstance();

    @FXML public void initialize() {
    	username.setText(sessionManager.getName());
    	role.setText(sessionManager.getRole());
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    }
    
    @FXML public void rooms() {
    	menuTechLend.setVisible(false);
    	menuImage.setVisible(false);
    	System.out.println("SALAS");
    }
    @FXML public void returns() {
    	menuTechLend.setVisible(false);
    	menuImage.setVisible(false);
    	System.out.println("Devoluciones");
    }
    @FXML
    public void sanctions() {
    	menuTechLend.setVisible(false);
    	menuImage.setVisible(false);
    	System.out.println("SANCIONES");
    }
    @FXML
    public void teachers() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/test.fxml");
    }
    
    @FXML public void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
    }

}
