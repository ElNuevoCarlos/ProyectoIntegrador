package controllers.manager;

import java.sql.Connection;
import java.util.Optional;

import application.Main;
import data.DataBase;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.User;
import utils.ViewUtils;


public class UsersController {
    @FXML private BorderPane rootPane;
    @FXML private TableView<User> tableTeachers;
    @FXML private TableColumn<User, String> nombre;
    @FXML private TableColumn<User, String> correo;
    @FXML private TableColumn<User, String> rol;
    @FXML private TableColumn<User, String> programa;
    @FXML private TableColumn<User, String> identificacion;
    @FXML private TableColumn<User, String> contacto;
  
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);

    @FXML public void initialize() {
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
		
        nombre.setCellFactory(TextFieldTableCell.forTableColumn());
        correo.setCellFactory(TextFieldTableCell.forTableColumn());
        rol.setCellFactory(TextFieldTableCell.forTableColumn());
        programa.setCellFactory(TextFieldTableCell.forTableColumn());
        identificacion.setCellFactory(TextFieldTableCell.forTableColumn());
        contacto.setCellFactory(TextFieldTableCell.forTableColumn());
        
        nombre.setOnEditCommit(event -> {
        	User user = event.getRowValue();
        	String name = user.getNombre_completo();
        	
        	user.setNombre_completo(event.getNewValue());
        	Boolean verifyUpdate = userDao.update(user);
        	if (!verifyUpdate) user.setNombre_completo(name);
        	else ViewUtils.AlertWindow(null, null, "El nombre del docente fue cambiado con exito.", AlertType.INFORMATION);
        	
        });
        
        correo.setOnEditCommit(event -> {
        	User user = event.getRowValue();
        	String email = user.getCorreo_institucional();
        	
        	user.setCorreo_institucional(event.getNewValue());
        	Boolean verifyUpdate = userDao.update(user);
        	if (!verifyUpdate) user.setCorreo_institucional(email);
        	else ViewUtils.AlertWindow(null, null, "El correo del docente fue cambiado con exito.", AlertType.INFORMATION);
        });
        
        rol.setOnEditCommit(event -> {
        	if (event.getNewValue().equals("ENCARGADO")) {
        		ViewUtils.AlertWindow(null, "Sin Autorización", "Tú no puedes definir nuevos Encargados", AlertType.ERROR);
        		return;
        	}
        	else if (event.getNewValue().equals("SUPERENCARGADO")) {
        		ViewUtils.AlertWindow(null, "Sin Autorización", "Tú no puedes definir nuevos Super Encargados", AlertType.ERROR);
        		return;
        	}
        	
        	
        	User user = event.getRowValue();
        	String role = user.getRol();
        	
        	user.setRol(event.getNewValue());
        	Boolean verifyUpdate = userDao.update(user);
        	if (!verifyUpdate) user.setRol(role);
        	else ViewUtils.AlertWindow(null, null, "El rol del docente fue cambiado con exito.", AlertType.INFORMATION);
        });
        
        programa.setOnEditCommit(event -> {
        	User user = event.getRowValue();
        	String program = user.getPrograma_departamento();
        	
        	user.setPrograma_departamento(event.getNewValue());
        	Boolean verifyUpdate = userDao.update(user);
        	if (!verifyUpdate) user.setPrograma_departamento(program);
        	else ViewUtils.AlertWindow(null, null, "El programa del docente fue cambiado con exito.", AlertType.INFORMATION);
        });
        
        identificacion.setOnEditCommit(event -> {
        	User user = event.getRowValue();
        	String id = user.getNumero_identificacion();
        	
        	user.setNumero_identificacion(event.getNewValue());
        	Boolean verifyUpdate = userDao.update(user);
        	if (!verifyUpdate) user.setNumero_identificacion(id);
        	else ViewUtils.AlertWindow(null, null, "La identificación del docente fue cambiado con exito.", AlertType.INFORMATION);
        });
        
        contacto.setOnEditCommit(event -> {
        	User user = event.getRowValue();
        	String phone = user.getTelefono();
        	
        	user.setTelefono(event.getNewValue());
        	Boolean verifyUpdate = userDao.update(user);
        	if (!verifyUpdate) user.setTelefono(phone);
        	else ViewUtils.AlertWindow(null, null, "El contacto del docente fue cambiado con exito.", AlertType.INFORMATION);
        });
        
		tableTeachers.setItems(teacher);
    }
    @FXML public void docentes() {
    	ViewUtils.cargarGrid("/views/Manager/Manager.fxml", rootPane);
    }
    
    public User selectUser() {
    	User user = tableTeachers.getSelectionModel().getSelectedItem();
    	if (user == null) {
    		ViewUtils.AlertWindow(null, null, "Debe primero seleccionar a un docente.", AlertType.ERROR);
    		return null;
    	}
    	return user;
    }
    
    @FXML public void prestamos() {
    	User user = selectUser();
    	if (user != null) {
    		
    	}
    }

    @FXML public void sanciones() {
    	User user = selectUser();
    	if (user != null) {
        	user = new User(user.getNombre_completo(), user.getNumero_identificacion(), user.getTipo_identificacion(),
        			user.getCorreo_institucional(), user.getPrograma_departamento(), user.getTelefono(), user.getEstado(),
        			user.getRol(), user.getPassword(), user.getId());
    		Main.datoGlobal = user;
    		ViewUtils.cargarGrid("/views/Manager/Sanction.fxml", Main.rootLayout);
    	}
    }
    
    @FXML public void añadir() {
        Dialog<User> dialog = new Dialog<>();
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/teacher.png")));
        
        GridPane grid = new GridPane();
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
    	
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        TextField apellidoField = new TextField();
        apellidoField.setPromptText("Apellidos");
        
    	ComboBox<String> tiField = new ComboBox<>();
    	
	    ObservableList<String> itemsTI = FXCollections.observableArrayList(
		        "CC", "CE"
		    );
	    tiField.setPromptText("T.I");
	    tiField.setPrefWidth(60);
	    tiField.setItems(itemsTI);
	    
	    TextField numeroField = new TextField();
	    numeroField.setPrefWidth(250);
	    numeroField.setPromptText("Número de Identificación");
	    
	    TextField proDeField = new TextField();
	    proDeField.setPrefWidth(100);
	    proDeField.setPromptText("Programa o Departamento");
	    
	    TextField telefonoField = new TextField();
	    telefonoField.setPromptText("Telefono");
	    
	    ComboBox<String> roleField = new ComboBox<>();
	    
	    ObservableList<String> itemsRol = FXCollections.observableArrayList(
		        "DOCENTE", "ADMINISTRATIVO"
		    );
	    roleField.setPromptText("Seleccione su rol");
	    roleField.setPrefWidth(155);
		roleField.setItems(itemsRol);
		
		TextField correoField = new TextField();
	    correoField.setPromptText("Correo Institucional");
	    
	    TextField contraseñaField = new TextField();
	    contraseñaField.setPromptText("Contraseña");
	    
	    TextField conVerField = new TextField();
	    conVerField.setPromptText("Confirmar Contraseña");

        grid.add(nombreField, 0, 1);
        grid.add(apellidoField, 1, 1);
        
        
        HBox identificacionBox = new HBox(5); 
        identificacionBox.getChildren().addAll(tiField, numeroField);
        
        grid.add(identificacionBox, 0, 2, 2, 1);
        
        grid.add(proDeField, 0, 3, 2, 1);
        grid.add(telefonoField, 0, 4);
        grid.add(roleField, 1, 4);
        grid.add(correoField, 0, 5, 2, 1);
        grid.add(contraseñaField, 0, 6);
        grid.add(conVerField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
        		String Name = nombreField.getText().trim();
        		String LastName = apellidoField.getText().trim();
        		String TI = tiField.getValue() != null ? tiField.getValue().trim() : "";
        		String NumIdentification = numeroField.getText().trim();
        		String Pro_dep = proDeField.getText().trim();
        		String Phone = telefonoField.getText().trim();
        		String Email = correoField.getText().trim();
        		String Role = roleField.getValue() != null ? roleField.getValue().trim() : "";
        		String Password1 = contraseñaField.getText().trim();
        		String Password2 = conVerField.getText().trim();
        		
                if (Name.isEmpty() || 
                		LastName.isEmpty() || 
                		TI.isEmpty() || 
                		NumIdentification.isEmpty() || 
                		Pro_dep.isEmpty() || 
                		Phone.isEmpty() || 
                		Email.isEmpty() || 
                		Role.isEmpty() || 
                		Password1.isEmpty() || 
                		Password2.isEmpty()){
                	
                	ViewUtils.AlertWindow(null, "Campos vacíos", "Por favor, complete todos los campos.", AlertType.ERROR);
                    return null;
                }
                String regexEmail = "^[a-zA-Z0-9._%+-]+@udi\\.edu\\.co$";
                if (!Email.matches(regexEmail)) {
                	ViewUtils.AlertWindow(null, "Correo inválido", "El correo debe tener el formato usuario@udi.edu.co", AlertType.ERROR);
                	return null;
                }
                if (!NumIdentification.matches("\\d{6,10}")) {
                	ViewUtils.AlertWindow(null, "Número inválido", "Debe ingresar entre 6 y 10 dígitos numéricos.", AlertType.ERROR);
                	return null;
                }
                if (!Phone.matches("\\d{10}")) {
                	ViewUtils.AlertWindow(null, "Número inválido", "Debe ingresar exactamente 10 dígitos numéricos.", AlertType.ERROR);
                	return null;
                }
                if (Password1.equals(Password2)) {
                	if (!Password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$")) {
                		ViewUtils.AlertWindow(null, "Contraseña insegura", 
                                "La contraseña debe tener:\n" +
                                "- Entre 8 y 20 caracteres\n" +
                                "- Al menos una letra mayúscula\n" +
                                "- Al menos una letra minúscula\n" +
                                "- Al menos un número\n" +
                                "- Al menos un carácter especial (@#$%^&+=!)", AlertType.ERROR);
                            return null;
                	} else {
                		String fullName = Name +" "+ LastName;
                		User newUser = new User(fullName, NumIdentification, TI, Email, Pro_dep, Phone, "ACTIVA", Role, Password1, null);
                		userDao.save(newUser);
                		return newUser;
                	}
                } else {
                	ViewUtils.AlertWindow(null, "Contraseñas no coinciden", "Las contraseñas ingresadas no son iguales. Por favor, verifíquelas.", AlertType.ERROR);
                	return null;
                }
            }
            return null;
        });
        dialog.showAndWait();
        initialize();
    }
    
    @FXML public void actualizar() {
    	User user = selectUser();
    	if (user != null) {
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
	            	String rol = rolField.getText().trim();
	            	
	            	if (rol.equals("ENCARGADO")) {
	            		ViewUtils.AlertWindow(null, "Sin Autorización", "Tú no puedes definir nuevos Encargados", AlertType.ERROR);
	            		return null;
	            	}
	            	else if (rol.equals("SUPERENCARGADO")) {
	            		ViewUtils.AlertWindow(null, "Sin Autorización", "Tú no puedes definir nuevos Super Encargados", AlertType.ERROR);
	            		return null;
	            	}
	            	
	            	
	                String name = nameField.getText().trim();
	                String email = emailField.getText().trim();
	                String programa = programaField.getText().trim();
	                String telefono = telefonoField.getText().trim();
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
	            ViewUtils.AlertWindow(null, null, "Docente actualizado con éxito.", AlertType.INFORMATION);
	        });
    	}
    }
    @FXML public void eliminar() {
    	User user = selectUser();
    	if (user != null) {
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
	    		ViewUtils.AlertWindow(null, null, "El docente fue removido con éxito.", AlertType.INFORMATION);
	        }
    	}
    }
    @FXML public void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
    }

}
