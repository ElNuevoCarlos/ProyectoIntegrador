package controllers.manager;

import java.sql.Connection;
import java.sql.Date;
import application.Main;
import data.DataBase;
import data.SanctionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Sanction;
import model.User;
import utils.ViewUtils;


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
	    
	    if (dato instanceof User) {
	    	user = (User) dato;
	    }
	    System.out.println(user);
	    
    	name.setText(user.getNombre_completo());
    	id.setText(user.getNumero_identificacion());
  	
		ObservableList<Sanction> sanctions = FXCollections.observableArrayList();

		for (Sanction sanction : sanctionDao.fetchUser(user.getNumero_identificacion())) { 
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
    	ViewUtils.cargarGrid("/views/Manager/Manager.fxml", Main.rootLayout);
    }
    
    @FXML public void añadir() {
    	
    }
    
    @FXML public void actualizar() {

    }
    
    @FXML public void eliminar() {

    }
    @FXML public void goToBack() {
    	ViewUtils.cargarGrid("/views/Manager/Menu.fxml", Main.rootLayout);
    }

}
