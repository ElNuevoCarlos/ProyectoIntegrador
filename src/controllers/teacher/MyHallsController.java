package controllers.teacher;

import java.sql.Connection;

import application.Main;
import data.DBConnectionFactory;
import data.LoanDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Loans;
import model.UserSession;
import utils.ViewUtils;

public class MyHallsController {
    @FXML private TableView<Loans> tableDisponibles;
    @FXML private TableColumn<Loans, String> capacidadDisponible, salaDisponible, ubicacionDisponible, descripcionDisponible, estadoDisponible, fechaDisponible;
    
    @FXML private TextField salaDisponibleField, ubicacionDisponibleField, estadoDisponibleField;
    
    private FilteredList<Loans> listaFiltradaDisponible;
    

	public UserSession userSession = UserSession.getInstance();
    public String userRol = userSession.getRole();
    private Connection connection = DBConnectionFactory.getConnectionByRole(userRol).getConnection();
    private LoanDAO loanDao = new LoanDAO(connection);
    
    @FXML void initialize() {
		// ------------------------------- Salas Disponibles
    	
		ObservableList<Loans> myList = FXCollections.observableArrayList();
		
		for (Loans loan : loanDao.fetchLoan(userSession.getId(), new StringBuilder())) { 
			myList.add(loan);
		}

		salaDisponible.setCellValueFactory(new PropertyValueFactory<>("nameHall"));
		ubicacionDisponible.setCellValueFactory(new PropertyValueFactory<>("location"));
		estadoDisponible.setCellValueFactory(new PropertyValueFactory<>("state"));
		fechaDisponible.setCellValueFactory(new PropertyValueFactory<>("date"));

		listaFiltradaDisponible = new FilteredList<>(myList, p -> true);
	    
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
            boolean bSala = resource.getNameHall().toLowerCase().contains(sala);
            boolean bUbicacion = resource.getLocation().toLowerCase().contains(ubicacion);
            boolean bEstado = resource.getState().toLowerCase().contains(estado);
            
            return bSala && bUbicacion && bEstado;
        });
    }
    
    @FXML void actualizar() {
    	Loans loan = selectResource();
    	if (loan != null) {
        	Main.datoGlobal = loan;
        	ViewUtils.cargarGrid("/views/Request.fxml", Main.rootLayout);
    	}
    }

    private Loans selectResource() {
    	Loans loan = tableDisponibles.getSelectionModel().getSelectedItem();
    	if (loan == null) {
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero una sala.", AlertType.ERROR);
    		return null;
    	}
    	return loan;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
