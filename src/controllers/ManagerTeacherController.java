package controllers;

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
  
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);
	private SessionManager sessionManager = SessionManager.getInstance();

    @FXML public void initialize() { 
        rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
        /*
        
    	username.setText(sessionManager.getName());
    	role.setText(sessionManager.getRole());
        
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
		*/
    }
    @FXML public void docentes() {
    	Main.cargarGrid("/views/Manager/Manager.fxml", rootPane);
    }
    
    @FXML public void añadir() {
    	
    }
    
    @FXML public void actualizar() {
    	User user = tableTeachers.getSelectionModel().getSelectedItem();
    	if (user == null) {
    		Main.AlertWindow(null, "Debe primero seleccionar a un docente.", AlertType.ERROR);
    		return;
    	}
    	
        Dialog<User> dialog = new Dialog<>();
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/teacher.png")));
        
        GridPane grid = new GridPane();
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField nameField = new TextField(user.getNombre_completo());
        TextField emailField = new TextField(user.getCorreo_institucional());
        TextField programaField = new TextField(user.getPrograma_departamento());
        TextField telefonoField = new TextField(user.getTelefono());
        TextField rolField = new TextField(user.getRol());
        TextField passwordField = new TextField(user.getPassword());

        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Correo:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Programa:"), 0, 3);
        grid.add(programaField, 1, 3);
        grid.add(new Label("Telefono:"), 0, 4);
        grid.add(telefonoField, 1, 4);
        grid.add(new Label("Rol:"), 0, 5);
        grid.add(rolField, 1, 5);
        grid.add(new Label("Password:"), 0, 6);
        grid.add(passwordField, 1, 6);
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String programa = programaField.getText().trim();
                String telefono = telefonoField.getText().trim();
                String rol = rolField.getText().trim();
                String contraseña = passwordField.getText().trim();
                
                return new User(name, 
                		user.getNumero_identificacion(), 
                		user.getTipo_identificacion(), 
                		email, 
                		programa, 
                		telefono, 
                		user.getEstado(), 
                		rol, 
                		contraseña, 
                		user.getId());
            }
            return null;
        });
        
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(updatedCourse -> {
            userDao.update(updatedCourse);
            initialize();
            Main.AlertWindow(null, "Docente actualizado con éxito.", AlertType.INFORMATION);
        });
    	
    }
    @FXML public void eliminar() {
    	User user = tableTeachers.getSelectionModel().getSelectedItem();
    	if (user == null) {
    		Main.AlertWindow(null, "Debe primero seleccionar a un docente.", AlertType.ERROR);
    		return;
    	}
    	
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setContentText("Estás seguro de eliminar al docente?\n\n"+
        "- Docente: "+user.getNombre_completo()+"\n"+
        "- Identificación: "+user.getNumero_identificacion()+"\n"+
        "- Rol: "+user.getRol());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
    		userDao.delete(user.getNumero_identificacion());
    		initialize();
    		Main.AlertWindow(null, "El docente fue removido con éxito.", AlertType.INFORMATION);
        }
        
    }
    @FXML public void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
		Main.loadView("/views/Login.fxml");
    }

}
