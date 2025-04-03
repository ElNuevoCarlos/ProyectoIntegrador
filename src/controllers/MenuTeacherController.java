package controllers;

import application.Main;
import data.SessionManager;
import data.UserDataManager;
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
import model.User;

public class MenuTeacherController {

    @FXML private BorderPane rootPane;
    @FXML private Label user, job;
    @FXML private VBox vbox;

    @FXML private TableColumn<User, String> name;
    @FXML private TableColumn<User, String> password;
    @FXML private TableColumn<User, String> email;
    @FXML private TableColumn<User, String> phone;
    @FXML private TableColumn<User, String> estate;
    @FXML private TableView<User> tableUsers;
    
	private UserDataManager userManager = UserDataManager.getInstance();
	private SessionManager sessionManager = SessionManager.getInstance();

    @FXML 
    public void initialize() {
    	ObservableList<User> users = FXCollections.observableArrayList();
    	
    	for (User user : userManager.getUsers()) {
        	users.add(user);
    	}

    	name.setCellValueFactory(new PropertyValueFactory<>("name"));
    	email.setCellValueFactory(new PropertyValueFactory<>("email"));
    	phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    	estate.setCellValueFactory(new PropertyValueFactory<>("estate"));
    	password.setCellValueFactory(new PropertyValueFactory<>("password"));
    	tableUsers.setItems(users);
    	
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        data();
    }
    
    @FXML
    void handleLogout(ActionEvent event) {
    	Main.loadView("/views/Login.fxml");
    }
  
    public void data() {
        this.user.setText(sessionManager.getName());
        this.job.setText(sessionManager.getRole());
    }
    
}

