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
import model.Loan;
import model.LoanTable;
import model.Loans;
import model.Resources;
import model.User;

public class LoandController {
    @FXML private TableView<Loans> tablePrestados;
    @FXML private TableColumn<Loans, String> docentePrestado;
    @FXML private TableColumn<Loans, String> especificacionesPrestado;
    @FXML private TableColumn<Loans, String> fechaPrestado;
    @FXML private TableColumn<Loans, String> salaPrestado;
    @FXML private TableColumn<Loans, String> ubicacionPrestado;
    @FXML private TableColumn<Loans, String> estadoPrestado;
    
    @FXML private TableView<Resources> tableDisponibles;
    @FXML private TableColumn<Resources, String> capacidadDisponible;
    @FXML private TableColumn<Resources, String> salaDisponible;
    @FXML private TableColumn<Resources, String> ubicacionDisponible;
    @FXML private TableColumn<Resources, String> descripcionDisponible;
    
    @FXML private TextField salaDisponibleField, ubicacionDisponibleField, salaPrestadoField, ubicacionPrestadoField, docentePrestadoField;
    
    private FilteredList<Resources> listaFiltradaDisponible;
    private FilteredList<Loans> listaFiltradaPrestados;
    
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
		
		ObservableList<Loans> prestamos = FXCollections.observableArrayList();
		
		for (Loans loan : loanDao.fetchLoandTwo()) { 
			prestamos.add(loan);
		}
        
	    docentePrestado.setCellValueFactory(new PropertyValueFactory<>("emailUser"));
	    especificacionesPrestado.setCellValueFactory(new PropertyValueFactory<>("specs"));
	    fechaPrestado.setCellValueFactory(new PropertyValueFactory<>("date"));
	    salaPrestado.setCellValueFactory(new PropertyValueFactory<>("nameHall"));
	    ubicacionPrestado.setCellValueFactory(new PropertyValueFactory<>("location"));
	    estadoPrestado.setCellValueFactory(new PropertyValueFactory<>("state"));
		
		listaFiltradaPrestados = new FilteredList<>(prestamos, p -> true);
		
		salaPrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
		ubicacionPrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
		docentePrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
		
		
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
    
    private void filtroPrestados() {
        String sala = salaPrestadoField.getText().toLowerCase();
        String ubicacion = ubicacionPrestadoField.getText().toLowerCase();
        String email = docentePrestadoField.getText().toLowerCase();

        listaFiltradaDisponible.setPredicate(resource -> {
            boolean bSala = resource.getName().toLowerCase().contains(sala);
            boolean bUbicacion = resource.getLocationTrademark().toLowerCase().contains(ubicacion);
            boolean bEmail = resource.getLocationTrademark().toLowerCase().contains(email);

            return bSala && bUbicacion && bEmail;
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
