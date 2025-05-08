package controllers;

import java.sql.Connection;

import application.Main;
import data.DataBase;
import data.SessionManager;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;


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
  
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
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
    	menuTechLend.setVisible(false);
    	menuImage.setVisible(false);
    	
    	tableTeachers.setVisible(true);
    	
		ObservableList<User> teacher = FXCollections.observableArrayList();
		
		for (User docente : userDao.fetch()) {
			teacher.add(docente);
		}
		
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre_completo"));
		correo.setCellValueFactory(new PropertyValueFactory<>("correo_institucional"));
		rol.setCellValueFactory(new PropertyValueFactory<>("rol"));
		programa.setCellValueFactory(new PropertyValueFactory<>("programa_departamento"));
		identificacion.setCellValueFactory(new PropertyValueFactory<>("numero_identificacion"));
		contacto.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		
		tableTeachers.setItems(teacher);
    }
    
    @FXML public void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
		Main.loadView("/views/Login.fxml");
    }

}
