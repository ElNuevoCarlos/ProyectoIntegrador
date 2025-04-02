package controllers;

import data.SessionManager;
import data.UserDataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import model.User;

public class MenuTeacherController {

    @FXML private BorderPane rootPane;
    @FXML private Label user, job;
    
	private UserDataManager userManager = UserDataManager.getInstance();
	private SessionManager sessionManager = SessionManager.getInstance();
    
    @FXML 
    public void initialize() {
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        data();
    }
    
    public void data() {
        this.user.setText(sessionManager.getName());
        this.job.setText(sessionManager.getRole());
        
    	for (User user : userManager.getUsers()) {
    		System.out.println(user.role);
    	}
    }
    
}

