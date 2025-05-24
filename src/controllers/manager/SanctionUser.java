package controllers.manager;

import java.sql.Connection;
import java.sql.Date;
import java.util.Optional;

import application.Main;
import data.DataBase;
import data.SanctionDAO;
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
import model.Sanction;
import model.User;


public class SanctionUser {
    @FXML private BorderPane rootPane;
    @FXML private Text name, id;
    @FXML private TableView<Sanction> tabletableSanctions;
    @FXML private TableColumn<Sanction, String> tipo_sancion;
    @FXML private TableColumn<Sanction, String> descripcion;
    @FXML private TableColumn<Sanction, Date> fecha_sancion;
    @FXML private TableColumn<Sanction, Date> fecha_fin;
    @FXML private TableColumn<Sanction, Integer> monto;
    @FXML private TableColumn<Sanction, String> estado;
    @FXML private TableColumn<Sanction, Long> id_prestamo;
    @FXML private GridPane gridPane;
  
    private Connection database = DataBase.getInstance().getConnection();
    private SanctionDAO sanctionDao = new SanctionDAO(database);

    @FXML public void initialize() {

    	
    	name.setText("Carlos");
    	id.setText("1095788069");
    	
		ObservableList<Sanction> sanctions = FXCollections.observableArrayList();
		
		for (User sanction : sanctionDao.fetchUser(1095788069)) { 
			sanctions.add(sanction);
		}
		
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre_completo"));
		correo.setCellValueFactory(new PropertyValueFactory<>("correo_institucional"));
		rol.setCellValueFactory(new PropertyValueFactory<>("rol"));
		programa.setCellValueFactory(new PropertyValueFactory<>("programa_departamento"));
		identificacion.setCellValueFactory(new PropertyValueFactory<>("numero_identificacion"));
		contacto.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		
		tableSanctions.setItems(sanctions);
    }
    @FXML public void docentes() {
    	Main.cargarGrid("/views/Manager/Manager.fxml", rootPane);
    }
    
    @FXML public void añadir() {
    	
    }
    
    @FXML public void actualizar() {

    }
    
    @FXML public void eliminar() {

    }
    @FXML public void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
		Main.loadView("/views/Login.fxml");
    }

}
