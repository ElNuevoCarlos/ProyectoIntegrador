package controllers.manager;

import java.sql.Connection;
import application.Main;
import data.DataBase;
import data.LoanDAO;
import data.ResourcesDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Hall;
import model.Loans;
import model.Location;
import model.Resources;
import model.User;
import utils.ViewUtils;

public class LoandController {
    @FXML private TableView<Loans> tablePrestados;
    @FXML private TableColumn<Loans, String> docentePrestado, especificacionesPrestado, 
    											fechaPrestado, salaPrestado, ubicacionPrestado, estadoPrestado;
    
    @FXML private TableView<Resources> tableDisponibles;
    @FXML private TableColumn<Resources, String> capacidadDisponible, salaDisponible, ubicacionDisponible, descripcionDisponible, estadoDisponible;
    
    @FXML private TextField salaDisponibleField, ubicacionDisponibleField, estadoDisponibleField, 
    						salaPrestadoField, ubicacionPrestadoField, 
    						docentePrestadoField, estadoPrestadoField;
    
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
		estadoDisponible.setCellValueFactory(new PropertyValueFactory<>("state"));

		listaFiltradaDisponible = new FilteredList<>(disponibles, p -> true);
	    
		salaDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		ubicacionDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		estadoDisponibleField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		
		tableDisponibles.setItems(listaFiltradaDisponible);
		
		// ------------------------------- Prestamos
		
		ObservableList<Loans> prestamos = FXCollections.observableArrayList();
		
		for (Loans loan : loanDao.fetchLoandTwo()) { 
			if (loan.getSpecs() == null) loan.setSpecs("Sin especificaciones");
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
		estadoPrestadoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());

		tablePrestados.setItems(listaFiltradaPrestados);
    }
    
    private void filtroPrestados() {
        String sala = salaPrestadoField.getText().toLowerCase();
        String ubicacion = ubicacionPrestadoField.getText().toLowerCase();
        String email = docentePrestadoField.getText().toLowerCase();
        String estado = estadoPrestadoField.getText().toLowerCase();
        
        listaFiltradaPrestados.setPredicate(resource -> {
            boolean bSala = resource.getNameHall().toLowerCase().contains(sala);
            boolean bUbicacion = resource.getLocation().toLowerCase().contains(ubicacion);
            boolean bEmail = resource.getEmailUser().toLowerCase().contains(email);
            boolean bEstado = resource.getState().toLowerCase().contains(estado);
            
            return bSala && bUbicacion && bEmail && bEstado;
        });
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
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero una sala", AlertType.ERROR);
    		return null;
    	}
    	return resource;
    }
    
    @FXML void añadir() {
        Dialog<User> dialog = new Dialog<>();
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        
        GridPane grid = new GridPane();
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
    	
        TextField nameField = new TextField();
        nameField.setPromptText("Nombre - Ejemplo: Sala 105");
        
        ComboBox<Integer> capacityField = new ComboBox<>();
        ObservableList<Integer> listCapacity = FXCollections.observableArrayList(10, 20, 30, 40, 50);
        capacityField.setItems(listCapacity);
        capacityField.setPromptText("Capacidad");
        capacityField.setPrefWidth(100);

        HBox BoxOne = new HBox(5); 
        BoxOne.getChildren().addAll(nameField, capacityField);
        
        grid.add(BoxOne, 0, 1, 2, 1);
        
        // ------------------------------- Estado
    	ComboBox<String> stateField = new ComboBox<>();
    	
	    ObservableList<String> itemsState = FXCollections.observableArrayList(
		        "Disponible", "En Mantenimiento"
		    );
	    stateField.setPromptText("Estado");
	    stateField.setPrefWidth(140);
	    stateField.setItems(itemsState);
	    
	 // ------------------------------- Ubicación
    	ComboBox<String> locateField = new ComboBox<>();
    	
	    ObservableList<String> itemslocate = FXCollections.observableArrayList(
		        "Edificio A", "Edificio B"
		    );
	    
	    locateField.setPromptText("Edificio");
	    locateField.setPrefWidth(100);
	    locateField.setItems(itemslocate);
	    locateField.setEditable(true);
	    
	    TextField floorField = new TextField();
	    floorField.setPrefWidth(50);
	    floorField.setPromptText("Piso");
	    
     // ------------------------------- Ubicación
        HBox Box = new HBox(5); 
        Box.getChildren().addAll(locateField, floorField, stateField);
        
        grid.add(Box, 0, 2, 2, 1);
        
        
	    TextField descField = new TextField();
	    descField.setPrefWidth(100);
	    descField.setPromptText("Descripción");
	    
        grid.add(descField, 0, 3, 2, 2);
        

        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
        		String Name = nameField.getText().trim();
        		String Locate = locateField.getValue() != null ? locateField.getValue().trim() : "";
        		String State = stateField.getValue() != null ? stateField.getValue().trim() : "";
        		Integer Capacity = capacityField.getValue() != null ? capacityField.getValue() : 10;
        		String Description = descField.getText().trim();

        		try {
        		    Integer.parseInt(floorField.getText().trim() != null ? floorField.getText().trim(): "1");
        		} catch (NumberFormatException e) {
        		    ViewUtils.AlertWindow(null, "Formato inválido", "El piso debe ser un número entero.", AlertType.ERROR);
        		    return null;
        		}
        		
                if (Name.isEmpty() || 
                		Locate.isEmpty() || 
                		Description.isEmpty() ||
                		State.isEmpty()) {
                	ViewUtils.AlertWindow(null, "Campos vacíos", "Por favor, complete todos los campos.", AlertType.ERROR);
                    return null;
                }
                if (resourcesDao.AuthenticateHall(Name)) {
                	ViewUtils.AlertWindow(null, "Sala existente", "La sala "+Name+" ya estaba registrada.", AlertType.ERROR);
                	return null;
                }
                String floorString = floorField.getText().trim();
                
                if (!resourcesDao.AuthenticateBuildingFloor(Locate, floorString)) {
                	resourcesDao.saveLocation(new Location(null, Locate, floorString));
                }

                Long id_location = resourcesDao.verifyLocation(Locate, floorString);
                
                resourcesDao.saveHall(new Hall(null, Name, id_location, String.valueOf(Capacity), Description, State));
                
                ViewUtils.AlertWindow(null, "Sala creada", "La sala "+Name+" ha sido registrada con éxito.", AlertType.INFORMATION);
            }
            return null;
        });
        dialog.showAndWait();
        initialize();
    }
    @FXML void devolvio() {

    }
    @FXML void eliminar() {
    	Resources resource = selectResource();
    	if (resource != null) {
    		if (ViewUtils.showConfirmation("Confirmación", "¿Está seguro que desea eliminar la sala "+resource.getName()+"?")) {
    			System.out.println("Elimino");
    		}
    		
    		// Si tiene prestamos aprobados no se puede eliminar
    	}
    }
    @FXML void pedir() {
    	Resources resource = selectResource();
    	if (resource.getState().equals("En Mantenimiento")) {
        	ViewUtils.AlertWindow(null, "En Mantenimiento", "No puedes pedir una sala en mantenimiento.", AlertType.ERROR);
        	return;
    	}
    	if (resource != null) {
        	Main.datoGlobal = resource;
        	ViewUtils.cargarGrid("/views/Request.fxml", Main.rootLayout);
    	}
    }
    @FXML void aprobar() {

    }
    @FXML void rechazar() {

    }
    @FXML void sancionar() {

    }
}
