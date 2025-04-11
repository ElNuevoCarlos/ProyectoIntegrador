package controllers;

import java.sql.Connection;

import application.Main;
import data.DataBase;
import data.SessionManager;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class MenuTeacherController {
    @FXML private BorderPane rootPane;
    @FXML private Label user, role;
    @FXML private VBox vbox;

    @FXML private TableColumn<User, String> name;
    @FXML private TableColumn<User, String> email;
    @FXML private TableColumn<User, String> phone;
    @FXML private TableView<User> tableUsers;
    
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
	private SessionManager sessionManager = SessionManager.getInstance();

    @FXML 
    public void initialize() {
    	ObservableList<User> users = FXCollections.observableArrayList();
    	
    	for (User user : userDao.fetch()) {
        	users.add(user);
    	}

    	name.setCellValueFactory(new PropertyValueFactory<>("name"));
    	email.setCellValueFactory(new PropertyValueFactory<>("email"));
    	phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    	tableUsers.setItems(users);
    	
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        data();
    }
    
    @FXML
    void handleReport(ActionEvent event) {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
    	Main.loadView("/views/MenuReports.fxml");
    }
    @FXML
    void handleLogout(ActionEvent event) {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
    	Main.loadView("/views/Login.fxml");
    }
  
    public void data() {
        this.user.setText(sessionManager.getName());
        this.role.setText(sessionManager.getRole());
    }
    
}

