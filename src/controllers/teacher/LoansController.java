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
import model.Resources;
import utils.ViewUtils;

public class LoansController {
    @FXML private TableView<Resources> tableDisponibles;
    @FXML private TableColumn<Resources, String> capacidadDisponible, salaDisponible, ubicacionDisponible, descripcionDisponible, estadoDisponible;
    
    @FXML private TextField salaDisponibleField, ubicacionDisponibleField, estadoDisponibleField;
    
    private FilteredList<Resources> listaFiltradaDisponible;
    
    private Connection database = DataBase.getInstance().getConnection();
    private ResourcesDAO resourcesDao = new ResourcesDAO(database);
    
    @FXML void initialize() {
		// ------------------------------- Salas Disponibles
    	
		ObservableList<Resources> disponibles = FXCollections.observableArrayList();
		
		for (Resources resource : resourcesDao.ResourcesView(true, new StringBuilder())) { 
			disponibles.add(resource);
		}
		
		capacidadDisponible.setCellValueFactory(new PropertyValueFactory<>("typeCapacity"));
		salaDisponible.setCellValueFactory(new PropertyValueFactory<>("name"));
		ubicacionDisponible.setCellValueFactory(new PropertyValueFactory<>("locationTrademark"));
		descripcionDisponible.setCellValueFactory(new PropertyValueFactory<>("description"));
		estadoDisponible.setCellValueFactory(new PropertyValueFactory<>("state"));

		listaFiltradaDisponible = new FilteredList<>(disponibles, p -> true);
	    
		salaDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		ubicacionDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		estadoDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		
		tableDisponibles.setItems(listaFiltradaDisponible);
		
    }
    
    private void filtroDisponibles() {
        String sala = salaDisponibleField.getText().toLowerCase();
        String ubicacion = ubicacionDisponibleField.getText().toLowerCase();
        String estado = estadoDisponibleField.getText().toLowerCase();

        listaFiltradaDisponible.setPredicate(resource -> {
            boolean bSala = resource.getName().toLowerCase().contains(sala);
            boolean bUbicacion = resource.getLocationTrademark().toLowerCase().contains(ubicacion);
            boolean bEstado = resource.getState().toLowerCase().contains(estado);
            
            return bSala && bUbicacion && bEstado;
        });
    }

    private Resources selectResource() {
    	Resources resource = tableDisponibles.getSelectionModel().getSelectedItem();
    	if (resource == null) {
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero una sala.", AlertType.ERROR);
    		return null;
    	}
    	return resource;
    }

   
    @FXML void pedir() {
    	Resources resource = selectResource();
    	if (resource != null) {
    		if (resource.getState().equals("No Disponible")) {
            	ViewUtils.AlertWindow(null, "CANCELADA", "No puedes pedir una sala que fue eliminada.", AlertType.ERROR);
            	return;
    		} else if (resource.getState().equals("En Mantenimiento")) {
            	ViewUtils.AlertWindow(null, "En Mantenimiento", "No puedes pedir una sala en mantenimiento.", AlertType.ERROR);
            	return;
        	}
        	Main.datoGlobal = resource;
        	ViewUtils.cargarGrid("/views/Request.fxml", Main.rootLayout);
    	}
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
