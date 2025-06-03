package controllers.manager;

import java.sql.Connection;
import data.DataBase;
import data.SanctionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Sanction;
import utils.ViewUtils;

public class SanctionController {
    @FXML private TextField prestamoField;
    @FXML private TextField fechaSancionField;
    @FXML private TextField tipoSancionField;

    @FXML private TableView<Sanction> tableSanctions;
    @FXML private TableColumn<Sanction, String> tipo_sancion;
    @FXML private TableColumn<Sanction, String> descripcion;
    @FXML private TableColumn<Sanction, String> fecha_sancion;
    @FXML private TableColumn<Sanction, String> fecha_fin;
    @FXML private TableColumn<Sanction, String> monto;
    @FXML private TableColumn<Sanction, String> id_prestamo;
    
    private FilteredList<Sanction> listaFiltrada;
    
    private Connection database = DataBase.getInstance().getConnection();
    private SanctionDAO sanctionDao = new SanctionDAO(database);

    @FXML public void initialize() {
		ObservableList<Sanction> sanctions = FXCollections.observableArrayList();
		
		for (Sanction sanction : sanctionDao.fetch()) { 
			sanctions.add(sanction);
		}
		
		tipo_sancion.setCellValueFactory(new PropertyValueFactory<>("typeSanction"));
		descripcion.setCellValueFactory(new PropertyValueFactory<>("description"));
		fecha_fin.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		monto.setCellValueFactory(new PropertyValueFactory<>("amount"));
		id_prestamo.setCellValueFactory(new PropertyValueFactory<>("idLoanDevice"));
        
	    listaFiltrada = new FilteredList<>(sanctions, p -> true);
	    
		tableSanctions.setItems(listaFiltrada);
		
		prestamoField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		fechaSancionField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		tipoSancionField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
    }

    
    private void filtro() {
        String prestamo = prestamoField.getText().toLowerCase();
        String tipoSancion = tipoSancionField.getText().toLowerCase();

        listaFiltrada.setPredicate(sanction -> {
            String idPrestamoStr = String.valueOf(sanction.getIdLoanDevice()).toLowerCase();
            String tipoSancionStr = sanction.getTypeSanction().toLowerCase();

            boolean bPrestamo = idPrestamoStr.contains(prestamo);
            boolean bTipoSancion = tipoSancionStr.contains(tipoSancion);

            return bPrestamo && bTipoSancion;
        });
    }
    
    private Sanction selectSanction() {
    	Sanction sanction = tableSanctions.getSelectionModel().getSelectedItem();
    	if (sanction == null) {
    		ViewUtils.AlertWindow(null, null, "Debe primero seleccionar una sanción", AlertType.ERROR);
    		return null;
    	}
    	return sanction;
    }
    @FXML private void actualizar() {
    	Sanction sanction = selectSanction();
    	if (sanction != null) {
    		
    	}
    	
    }
    @FXML private void eliminar() {
    	Sanction sanction = selectSanction();
    	if (sanction != null) {
    		
    	}
    }
    
    
    
    
    
    
}
