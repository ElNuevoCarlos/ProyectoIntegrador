package controllers.manager;

import java.sql.Connection;

import application.Main;
import data.DataBase;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import utils.ViewUtils;

public class Sanction {
    @FXML private TextField correoField;
    @FXML private TextField identificacionField;
    @FXML private TextField nombreField;

    @FXML private TableView<User> tableTeachers;
    @FXML private TableColumn<User, String> nombre;
    @FXML private TableColumn<User, String> correo_institucional;
    @FXML private TableColumn<User, String> numero_identificacion;
    @FXML private TableColumn<User, String> programa_departamento;
    @FXML private TableColumn<User, String> telefono;
    
    private FilteredList<User> listaFiltrada;
    
    private Connection database = DataBase.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(database);

    @FXML public void initialize() {
		ObservableList<User> teacher = FXCollections.observableArrayList();
		
		for (User docente : userDao.fetch()) { 
			teacher.add(docente);
		}
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre_completo"));
		correo_institucional.setCellValueFactory(new PropertyValueFactory<>("correo_institucional"));
		numero_identificacion.setCellValueFactory(new PropertyValueFactory<>("numero_identificacion"));
		programa_departamento.setCellValueFactory(new PropertyValueFactory<>("programa_departamento"));
		telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
	    listaFiltrada = new FilteredList<>(teacher, p -> true);
	    
		tableTeachers.setItems(listaFiltrada);
		
		correoField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		nombreField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		identificacionField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
    }
    private void filtro() {
        String Correo = correoField.getText().toLowerCase();
        String Nombre = nombreField.getText().toLowerCase();
        String Identificacion = identificacionField.getText().toLowerCase();

        listaFiltrada.setPredicate(teacher -> {
            boolean coincideCorreo = teacher.getCorreo_institucional().toLowerCase().contains(Correo);
            boolean coincideNombre = teacher.getNombre_completo().toLowerCase().contains(Nombre);
            boolean coincideId = teacher.getNumero_identificacion().toLowerCase().contains(Identificacion);

            return coincideCorreo && coincideNombre && coincideId;
        });
    }
    
    public User selectUser() {
    	User user = tableTeachers.getSelectionModel().getSelectedItem();
    	if (user == null) {
    		ViewUtils.AlertWindow(null, null, "Debe primero seleccionar a un docente.", AlertType.ERROR);
    		return null;
    	}
    	return user;
    }
    
    @FXML void actualizar(ActionEvent event) {

    }

    @FXML public void sanciones() {
    	User user = selectUser();
    	if (user != null) {
        	user = new User(user.getNombre_completo(), user.getNumero_identificacion(), user.getTipo_identificacion(),
        			user.getCorreo_institucional(), user.getPrograma_departamento(), user.getTelefono(), user.getEstado(),
        			user.getRol(), user.getPassword(), user.getId());
    		Main.datoGlobal = user;
    		ViewUtils.cargarGrid("/views/Manager/SanctionUser.fxml", Main.rootLayout);
    	}
    }
    
    @FXML void sancionar(ActionEvent event) {
    	User user = selectUser();
    }
    
    
    
    
    
    
    
}
