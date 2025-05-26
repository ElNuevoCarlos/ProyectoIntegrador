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
import model.LoanTable;
import model.Sanction;
import model.User;


public class SanctionUser {

    @FXML private Text name, id;
    @FXML private TableView<Sanction> tableSanctions;
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
    
    User user;

    @FXML public void initialize() {
    	
	    Object dato = Main.datoGlobal;
	    if (dato instanceof LoanTable) {
	    	user = (User) dato;
	    }
	    
    	
    	name.setText(user.getNombre_completo());
    	id.setText(user.getNumero_identificacion());
  	
		ObservableList<Sanction> sanctions = FXCollections.observableArrayList();
		

		long x = (long) 1095788069;
		
		for (Sanction sanction : sanctionDao.fetchUser(x)) { 
			sanctions.add(sanction);
		}
		
	    tipo_sancion.setCellValueFactory(new PropertyValueFactory<>("typeSanction"));
	    descripcion.setCellValueFactory(new PropertyValueFactory<>("description"));
	    fecha_sancion.setCellValueFactory(new PropertyValueFactory<>("sanctionDate"));
	    fecha_fin.setCellValueFactory(new PropertyValueFactory<>("endDate"));
	    monto.setCellValueFactory(new PropertyValueFactory<>("amount"));
	    estado.setCellValueFactory(new PropertyValueFactory<>("state"));
	    id_prestamo.setCellValueFactory(new PropertyValueFactory<>("idLoanHall"));
	    
		
		tableSanctions.setItems(sanctions);

    }
    @FXML public void docentes() {
    	Main.cargarGrid("/views/Manager/Manager.fxml", Main.rootLayout);
    }
    
    @FXML public void añadir() {
    	
    }
    
    @FXML public void actualizar() {

    }
    
    @FXML public void eliminar() {

    }
    @FXML public void goToBack() {
    	Main.cargarGrid("/views/Manager/Menu.fxml", Main.rootLayout);
    }

}
