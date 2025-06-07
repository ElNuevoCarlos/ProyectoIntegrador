package controllers.teacher;

import java.sql.Connection;

import application.Main;
import data.DataBase;
import data.ResourcesDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Equipment;
import utils.ViewUtils;

public class EquipmentController {
    @FXML private TableView<Equipment> tableDisponibles;
    @FXML private TableColumn<Equipment, String> nombre, tipo, marca, estado, descripcion;
    
    @FXML private TextField nombreField, tipoField, marcaField, estadoField;
    
    private FilteredList<Equipment> listaFiltradaDisponible;
    
    private Connection database = DataBase.getInstance().getConnection();
    private ResourcesDAO resourcesDao = new ResourcesDAO(database);
    
    @FXML void initialize() {
		// ------------------------------- Equipos Disponibles
    	
		ObservableList<Equipment> disponibles = FXCollections.observableArrayList();
		
		for (Equipment equipment : resourcesDao.EquipmentView()) { 
			disponibles.add(equipment);
		}
		
		nombre.setCellValueFactory(new PropertyValueFactory<>("name"));
		tipo.setCellValueFactory(new PropertyValueFactory<>("deviceType"));
		marca.setCellValueFactory(new PropertyValueFactory<>("brand"));
		estado.setCellValueFactory(new PropertyValueFactory<>("state"));
		descripcion.setCellValueFactory(new PropertyValueFactory<>("description"));

		listaFiltradaDisponible = new FilteredList<>(disponibles, p -> true);
	    
		nombreField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		tipoField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		marcaField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		estadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		
		tableDisponibles.setItems(listaFiltradaDisponible);
		
    }
    
    private void filtroDisponibles() {
        String Nombre = nombreField.getText().toLowerCase();
        String Tipo = tipoField.getText().toLowerCase();
        String Marca = marcaField.getText().toLowerCase();
        String Estado = estadoField.getText().toLowerCase();

        listaFiltradaDisponible.setPredicate(resource -> {
            boolean bNombre = resource.getName().toLowerCase().contains(Nombre);
            boolean bTipo = resource.getDeviceType().toLowerCase().contains(Tipo);
            boolean bMarca = resource.getBrand().toLowerCase().contains(Marca);
            boolean bEstado = resource.getState().toLowerCase().contains(Estado);
            
            return bNombre && bTipo && bMarca &&bEstado;
        });
    }

    private Equipment selectResource() {
    	Equipment equipment = tableDisponibles.getSelectionModel().getSelectedItem();
    	if (equipment == null) {
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero un equipo.", AlertType.ERROR);
    		return null;
    	}
    	return equipment;
    }


    @FXML void pedir() {
    	Equipment equipment = selectResource();
    	if (equipment != null) {
    		if (equipment.getState().equals("No Disponible")) {
            	ViewUtils.AlertWindow(null, "CANCELADA", "No puedes pedir un equipo que fue eliminado.", AlertType.ERROR);
            	return;
    		} else if (equipment.getState().equals("En Mantenimiento")) {
            	ViewUtils.AlertWindow(null, "En Mantenimiento", "No puedes pedir un equipo en mantenimiento.", AlertType.ERROR);
            	return;
        	}
    		
        	Main.datoGlobal = equipment;
        	ViewUtils.cargarGrid("/views/RequestEquipment.fxml", Main.rootLayout);
        	
    	}
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
