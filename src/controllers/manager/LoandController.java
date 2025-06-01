package controllers.manager;

import java.sql.Connection;
import java.sql.Timestamp;

import data.DataBase;
import data.LoanDAO;
import data.ResourcesDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.LoanTable;
import model.Resources;

public class LoandController {
/*
        this.idResource = idResource; // ID SALA, ID DISPOSITIVO
        this.name = name; // NOMBRE DISPOSITIVO, NOMBRE SALA
        this.locationTrademark = locationTrademark; //UBICACIÓN SALA, MARCA DEL DISPOSITIVO
        this.typeCapacity = typeCapacity; // TIPO DISPOSITIVO, CAPACIDAD SALA
        this.description = description; // DESCRIPCIÓN SALA, DISPOSITIVO
        this.typeResource = typeResource; // SALA, DISPOSITIVO
	   */
    @FXML private TableView<LoanTable> tablePrestados;
    @FXML private TableColumn<LoanTable, String> devuelvePrestado;
    @FXML private TableColumn<LoanTable, String> docentePrestado;
    @FXML private TableColumn<LoanTable, String> especificacionesPrestado;
    @FXML private TableColumn<LoanTable, String> fechaPrestado;
    @FXML private TableColumn<LoanTable, String> salaPrestado;
    @FXML private TableColumn<LoanTable, String> ubicacionPrestado;
    @FXML private TableColumn<LoanTable, String> estadoPrestado;
    
    @FXML private TableView<Resources> tableDisponibles;
    @FXML private TableColumn<Resources, String> capacidadDisponible;
    @FXML private TableColumn<Resources, String> salaDisponible;
    @FXML private TableColumn<Resources, String> ubicacionDisponible;
    @FXML private TableColumn<Resources, String> descripcionDisponible;
    
    @FXML private TextField salaDisponibleField, ubicacionDisponibleField, salaPrestadoField, ubicacionPrestadoField, docentePrestadoField;
    
    private FilteredList<Resources> listaFiltradaDisponible;
    private FilteredList<LoanTable> listaFiltradaPrestados;
    
    private Connection database = DataBase.getInstance().getConnection();
    private ResourcesDAO resourcesDao = new ResourcesDAO(database);
    private LoanDAO loanDao = new LoanDAO(database);
    
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

		listaFiltradaDisponible = new FilteredList<>(disponibles, p -> true);
	    
		salaDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		ubicacionDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		
		tableDisponibles.setItems(listaFiltradaDisponible);
		
		// ------------------------------- Prestamos
		ObservableList<LoanTable> prestamos = FXCollections.observableArrayList();
		
		for (LoanTable loan : loanDao.fetchLoand(true)) { 
			prestamos.add(loan);
		}
        
	    devuelvePrestado.setCellValueFactory(new PropertyValueFactory<>("date"));
	    docentePrestado.setCellValueFactory(new PropertyValueFactory<>("name"));
	    especificacionesPrestado.setCellValueFactory(new PropertyValueFactory<>("specs"));
	    fechaPrestado.setCellValueFactory(new PropertyValueFactory<>("date"));
	    salaPrestado.setCellValueFactory(new PropertyValueFactory<>("name"));
	    ubicacionPrestado.setCellValueFactory(new PropertyValueFactory<>("locationType"));
	    estadoPrestado.setCellValueFactory(new PropertyValueFactory<>("state"));
		
		listaFiltradaPrestados = new FilteredList<>(prestamos, p -> true);
		
		/*
		salaPrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
		ubicacionPrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
		docentePrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
		*/
		tablePrestados.setItems(listaFiltradaPrestados);
    }
    
    private void filtroDisponibles() {
        String sala = salaDisponibleField.getText().toLowerCase();
        String ubicacion = ubicacionDisponibleField.getText().toLowerCase();

        listaFiltradaDisponible.setPredicate(resource -> {
            boolean bSala = resource.getName().toLowerCase().contains(sala);
            boolean bUbicacion = resource.getLocationTrademark().toLowerCase().contains(ubicacion);

            return bSala && bUbicacion;
        });
    }
    
    @FXML void añadir() {

    }
    @FXML void devolvio() {

    }
    @FXML void eliminar() {

    }
    @FXML void pedir() {

    }
    @FXML void aprobar() {

    }
    @FXML void rechazar() {

    }
    @FXML void sancionar() {

    }
}
