package controllers.manager;

import java.sql.Connection;
import java.time.LocalDate;
import application.Main;
import data.DBConnectionFactory;
import data.LoanDAO;
import data.ResourcesDAO;
import data.SanctionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import model.Sanction;
import model.User;
import model.UserSession;
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
    
	public UserSession userSession = UserSession.getInstance();
    public String userRol = userSession.getRole();
    private Connection connection = DBConnectionFactory.getConnectionByRole(userRol).getConnection();
    private ResourcesDAO resourcesDao = new ResourcesDAO(connection);
    private LoanDAO loanDao = new LoanDAO(connection);
    private SanctionDAO sanctionDao = new SanctionDAO(connection);
    
    private Long id_location;
    
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
		
		for (Loans loan : loanDao.fetchLoan(null, new StringBuilder())) { 
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
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero una sala.", AlertType.ERROR);
    		return null;
    	}
    	return resource;
    }
    private Loans selectLoan() {
    	Loans loan = tablePrestados.getSelectionModel().getSelectedItem();
    	if (loan == null) {
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero un prestamo.", AlertType.ERROR);
    		return null;
    	}
    	return loan;
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
                
                id_location = resourcesDao.verifyLocation(Locate, floorString);

                
                
                resourcesDao.saveHall(new Hall(null, Name, id_location, String.valueOf(Capacity), Description, State));
                
                ViewUtils.AlertWindow(null, "Sala creada", "La sala "+Name+" ha sido registrada con éxito.", AlertType.INFORMATION);
            }
            return null;
        });
        dialog.showAndWait();
        initialize();
    }
    @FXML void eliminar() {
    	Resources resource = selectResource();
    	if (resource != null) {
    		if (resource.getState().equals("CANCELADO")) {
    			ViewUtils.AlertWindow(null, "No se puede", "La sala "+resource.getName()+" ya fue eliminada.", AlertType.INFORMATION);
    			return;
    		}
    		if (ViewUtils.showConfirmation("Confirmación", "¿Está seguro que desea eliminar la sala "+resource.getName()+"?\nAl eliminarla, todas las salas prestadas seran canceladas.")) {
    			if (loanDao.updatesStateHall("No Disponible", resource.getIdResource())) {
        			loanDao.updatesHallState("En Mantenimiento", resource.getIdResource());
        			initialize();
        			ViewUtils.AlertWindow(null, "Sala Eliminada", "La sala "+resource.getName()+" fue eliminada con éxito.", AlertType.INFORMATION);
    			}
    		}
    	}
    }
    @FXML void pedir() {
    	Resources resource = selectResource();
    	if (resource != null) {
        	if (resource.getState().equals("En Mantenimiento")) {
            	ViewUtils.AlertWindow(null, "En Mantenimiento", "No puedes pedir una sala en mantenimiento.", AlertType.ERROR);
            	return;
        	} else if (resource.getState().equals("CANCELADO")) {
            	ViewUtils.AlertWindow(null, "CANCELADA", "No puedes pedir una sala que fue eliminada.", AlertType.ERROR);
            	return;
        	}
        	Main.datoGlobal = resource;
        	ViewUtils.cargarGrid("/views/Request.fxml", Main.rootLayout);
    	}
    }
    @FXML void aprobar() {
    	Loans loan = selectLoan();
    	if (loan != null) {
    		if (loan.getState().equals("APROBADO")) {
    			ViewUtils.AlertWindow(null, "Ya esta aprobado", "El prestamo ya estaba aprobado.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("RECHAZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo está rechazado, ya no se puede aprobar", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("FINALIZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo está finalizado, ya no se puede aprobar", AlertType.ERROR);
    			return;
    		}
    		if (ViewUtils.showConfirmation("Confirmación", "Está apunto de aprobar el prestamo de "+loan.getEmailUser()+"\n"
    				+ "- Sala: "+loan.getNameHall()+"\n- Ubicación: "+loan.getLocation()+"\n- Especificaciones: "+loan.getSpecs())) {
    			
    			if (loanDao.updateState(loan.getId(), "APROBADO")) {
    				initialize();
    				ViewUtils.AlertWindow(null, "Prestamo Aprobado", "El prestamo de "+loan.getEmailUser()+" ha sido aprobado con éxito.", AlertType.INFORMATION);
    			}
    		}
    	}
    }
    @FXML void devolvio() {
    	Loans loan = selectLoan();
    	if (loan != null) {
    		if (loan.getState().equals("RECHAZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo está rechazado, no puede usar este boton.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("FINALIZADO")) {
    			ViewUtils.AlertWindow(null, "Ya esta finalizado", "El prestamo ya estaba finalizado.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("SOLICITADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo está SOLICITADO, no puede devolverlo si no ha sido aprobado.", AlertType.ERROR);
    			return;
    		}
    		if (ViewUtils.showConfirmation("Confirmación", "Está apunto de aprobar la entrega del prestamo de "+loan.getEmailUser()+"\n"
    				+ "- Sala: "+loan.getNameHall()+"\n- Ubicación: "+loan.getLocation()+"\n- Especificaciones: "+loan.getSpecs())) {
    			
    			if (loanDao.updateState(loan.getId(), "FINALIZADO")) {
    				initialize();
    				ViewUtils.AlertWindow(null, "Prestamo Finalizado", "El prestamo de "+loan.getEmailUser()+" ha finalizado con éxito.", AlertType.INFORMATION);
    			}
    		}
    	}
    }
    @FXML void rechazar() {
    	Loans loan = selectLoan();
    	if (loan != null) {
    		if (loan.getState().equals("RECHAZADO")) {
    			ViewUtils.AlertWindow(null, "Ya está rechazado", "El prestamo ya estaba rechazado.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("FINALIZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo está finalizado, ya no se puede rechazar", AlertType.ERROR);
    			return;
    		}
    		if (ViewUtils.showConfirmation("Confirmación", "Está apunto de rechazar el prestamo de "+loan.getEmailUser()+"\n"
    				+ "- Sala: "+loan.getNameHall()+"\n- Ubicación: "+loan.getLocation()+"\n- Especificaciones: "+loan.getSpecs())) {
    			
    			if (loanDao.updateState(loan.getId(), "RECHAZADO")) {
    				initialize();
    				ViewUtils.AlertWindow(null, "Prestamo Rechazado", "El prestamo de "+loan.getEmailUser()+" ha sido rechazado.", AlertType.INFORMATION);
    			}
    		}
    	}
    }
    @FXML void sancionar() {
    	Loans loan = selectLoan();
    	if (loan != null) {
    		if (loan.getState().equals("FINALIZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo ya está finalizado.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("RECHAZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo ha sido rechazado y no puedes sancionarlo.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("SOLICITADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo apenas ha sido SOLICITADO y no puedes sancionarlo.", AlertType.ERROR);
    			return;
    		}

	        Dialog<Sanction> dialog = new Dialog<>();
	        
	        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
	        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
	        
	        GridPane grid = new GridPane();
	        
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(20, 20, 10, 10));

	        DatePicker datePicker = new DatePicker();
	        datePicker.setPromptText("Fin de la sanción");
	        datePicker.setPrefWidth(150);
	        grid.add(datePicker, 0, 0, 2, 1);
	        
	        ComboBox<String> typeSanctionField = new ComboBox<>();
	        ObservableList<String> listCapacity = FXCollections.observableArrayList(
	        						"Entrega Tarde", "Mal estado");
	        typeSanctionField.setItems(listCapacity);
	        typeSanctionField.setPromptText("Tipo de Sanción");
	        typeSanctionField.setPrefWidth(150);
	        
		    TextField amountField = new TextField();
		    amountField.setPrefWidth(100);
		    amountField.setPromptText("Total a pagar");

	        HBox BoxOne = new HBox(5); 
	        BoxOne.getChildren().addAll(typeSanctionField, amountField);
	        
	        grid.add(BoxOne, 0, 1, 2, 1);
	        
		    TextField descField = new TextField();
		    descField.setPrefWidth(100);
		    descField.setPromptText("Descripción");
		    
	        grid.add(descField, 0, 3, 2, 2);
	        

	        dialog.getDialogPane().setContent(grid);
	        
	        ButtonType saveButtonType = new ButtonType("Guardar", ButtonData.OK_DONE);
	        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
	        
	        dialog.setResultConverter(dialogButton -> {
	            if (dialogButton == saveButtonType) {
	        		String typeSanction = typeSanctionField.getValue() != null ? typeSanctionField.getValue().trim() : "";
	        		String amount = amountField.getText().trim();
	        		String Description = descField.getText().trim();
	        		LocalDate data = datePicker.getValue();

	        		int parsedAmount = 0; 

	        		if (amount != null && !amount.trim().isEmpty()) {
	        		    try {
	        		        parsedAmount = Integer.parseInt(amount.trim());
	        		    } catch (NumberFormatException e) {
	        		        ViewUtils.AlertWindow(null, "Formato inválido", "El valor a pagar debe ser un número válido.", AlertType.ERROR);
	        		        return null;
	        		    }
	        		}
	        		
	                if (typeSanction.isEmpty() || 
	                		Description.isEmpty()) {
	                	ViewUtils.AlertWindow(null, "Campos vacíos", "Por favor, complete todos los campos.", AlertType.ERROR);
	                    return null;
	                }
	                
	                LocalDate today = LocalDate.now();
	                if (data.isBefore(today)) {
	                	ViewUtils.AlertWindow(null, "Fecha no aceptada", "Debe colocar una fecha valida.", AlertType.ERROR);
	                    return null;
	                }

	                sanctionDao.save(new Sanction(null, typeSanction, Description, data, parsedAmount, "ACTIVA", loan.getId()));
	                
	                ViewUtils.AlertWindow(null, "Sanción aplicada", "El docente ha sido sancionado con éxito.", AlertType.INFORMATION);
	            }
	            return null;
	        });
	        dialog.showAndWait();
    	}
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
