package controllers;

import java.sql.Connection;

import data.DataBase;
import data.SessionManager;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.User;

public class MenuTeacherController {
    @FXML private BorderPane rootPane;
    @FXML private  Hyperlink editUser;
    @FXML private Text nameText, numIdentificationText, tiText, emailText, phoneText, roleText, pro_depText, password1Text, password2Text, title;
    @FXML private TextField name, numIdentification, email, phone, pro_dep, password1, password2;
    @FXML private ComboBox<String> ti, role;
    @FXML private Button save;
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
	private SessionManager sessionManager = SessionManager.getInstance();
    @FXML 
    public void initialize() {
    	String[] partes = SessionManager.getInstance().getName().trim().split("\\s+");
    	Rectangle rect = new Rectangle();
    	rect.setX(0);                  
    	rect.setY(0);                  
    	rect.setWidth(150);           
    	rect.setHeight(500);           
    	rect.setFill(Color.BLUE); 
    	rootPane.getChildren().add(0, rect);
    	editUser.setText(partes[0] + " " + partes[1] + "\n" + SessionManager.getInstance().getRole());
    }
    
    @FXML
    void handleEditUser() {
	    ObservableList<String> itemsTi = FXCollections.observableArrayList(
		        "CC", "CE"
		    );
		    ti.setItems(itemsTi);
	    ObservableList<String> itemsRole = FXCollections.observableArrayList(
		        "DOCENTE", "ADMINISTRATIVO"
		    );
		    role.setItems(itemsRole);
		

    	nameText.setVisible(true);
    	numIdentificationText.setVisible(true);
    	tiText.setVisible(true);
    	emailText.setVisible(true);
    	phoneText.setVisible(true);
    	roleText.setVisible(true);
    	pro_depText.setVisible(true);
    	password1Text.setVisible(true);
    	password2Text.setVisible(true);
    	title.setVisible(true);


    	name.setVisible(true);
    	numIdentification.setVisible(true);
    	ti.setVisible(true);
    	email.setVisible(true);
    	phone.setVisible(true);
    	role.setVisible(true);
    	pro_dep.setVisible(true);
    	password1.setVisible(true);
    	password2.setVisible(true);


    	save.setVisible(true);
    	String[] otherData = userDao.otherData(sessionManager.getEmail());
    	name.setText(sessionManager.getName());
    	numIdentification.setText(otherData[0]);
    	ti.setValue(otherData[1]);
    	email.setText(sessionManager.getEmail());
    	phone.setText(otherData[4]);
    	role.setValue(sessionManager.getRole());
    	password1.setText(otherData[2]);
    	password2.setText(otherData[2]);
    	pro_dep.setText(otherData[3]);
    }
    
    @FXML
    void handleSave() {
		String Name = name.getText().trim();
		String TI = ti.getValue() != null ? ti.getValue().trim() : "";
		String NumIdentification = numIdentification.getText().trim();
		String Pro_dep = pro_dep.getText().trim();
		String Phone = phone.getText().trim();
		String Email = email.getText().trim();
		String Role = role.getValue() != null ? role.getValue().trim() : "";
		String Password1 = password1.getText().trim();
		String Password2 = password2.getText().trim();
		
        if (Name.isEmpty() || 
        		TI.isEmpty() || 
        		NumIdentification.isEmpty() || 
        		Pro_dep.isEmpty() || 
        		Phone.isEmpty() || 
        		Email.isEmpty() || 
        		Role.isEmpty() || 
        		Password1.isEmpty() || 
        		Password2.isEmpty()){
        	
        	showAlert("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }
        String regexEmail = "^[a-zA-Z0-9._%+-]+@udi\\.edu\\.co$";
        if (!Email.matches(regexEmail)) {
        	showAlert("Error", "Correo inválido", "El correo debe tener el formato usuario@udi.edu.co");
        	return;
        }
        if (!NumIdentification.matches("\\d{6,10}")) {
        	showAlert("Error", "Número inválido", "Debe ingresar entre 6 y 10 dígitos numéricos.");
        	return;
        }
        if (!Phone.matches("\\d{10}")) {
        	showAlert("Error", "Número inválido", "Debe ingresar exactamente 10 dígitos numéricos.");
        	return;
        }
        if (Password1.equals(Password2)) {
        	if (!Password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$")) {
                showAlert("Formato inválido", "Contraseña insegura", 
                        "La contraseña debe tener:\n" +
                        "- Entre 8 y 20 caracteres\n" +
                        "- Al menos una letra mayúscula\n" +
                        "- Al menos una letra minúscula\n" +
                        "- Al menos un número\n" +
                        "- Al menos un carácter especial (@#$%^&+=!)");
                    return;
        	} else {
        		User newUser = new User(Name, NumIdentification, TI, Email, Pro_dep, Phone, "ACTIVA", Role, Password1, sessionManager.getId());
        		sessionManager.setUser(sessionManager.getId(), Name, Role, Email);
	            if (showConfirmation("Confirmation", "Do you want to update your personal information?")) {
	            	userDao.update(newUser);
	            	showAlert("Updated", "Updated information", "Your data has been updated successfully.");
	            	String[] partesUptate = Name.split("\\s+");
	            	editUser.setText(partesUptate[0] + " " + partesUptate[1] + "\n" + Role);
	            } else {
	            	showAlert("Canceled", "Update cancelled", "No changes made.");
	            }
        		
        	}
        } else {
        	showAlert("Error", "Contraseñas no coinciden", "Las contraseñas ingresadas no son iguales. Por favor, verifíquelas.");
        	return;
        }
    }
    
    private void showAlert(String title, String head, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }
}

