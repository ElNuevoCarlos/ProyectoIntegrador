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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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

    @FXML void initialize() {
	    Object dato = Main.datoGlobal;
	    
	    if (dato instanceof User) {
	    	user = (User) dato;
	    }
	    
	    String[] partes = user.getNombre_completo().split(" ");
	    String primerNombre = partes[0];
	    String primerApellido = partes.length > 2 ? partes[partes.length - 2] : partes[1];
        
    	name.setText(primerNombre + " " +primerApellido);
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
	    
	    tipo_sancion.setCellFactory(TextFieldTableCell.forTableColumn());
	    descripcion.setCellFactory(TextFieldTableCell.forTableColumn());
	    
	    tipo_sancion.setOnEditCommit(event -> { 
		    Sanction sanction = event.getRowValue();
		    String type = sanction.getTypeSanction();
	    	
		    sanction.setTypeSanction(event.getNewValue());
		    Boolean verifyUpdate = sanctionDao.update(sanction); /// FALTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
		    if (!verifyUpdate) sanction.setTypeSanction(type);
	    }); 
	    
	    descripcion.setOnEditCommit(event -> { 
		    Sanction sanction = event.getRowValue();
		    String description = sanction.getDescription();
	    	
		    sanction.setDescription(event.getNewValue());
		    Boolean verifyUpdate = sanctionDao.update(sanction);
		    if (!verifyUpdate) sanction.setDescription(description);
	    }); 
        

		tableSanctions.setItems(sanctions);
    }

    @FXML void añadir() {
    	
    }
    
    @FXML void actualizar() {

    }
    
    @FXML void eliminar() {

    }
}
