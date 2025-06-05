package controllers.manager;

import java.sql.Connection;
import java.time.LocalDate;
import application.Main;
import data.DataBase;
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
import model.Equipment;
import model.EqupmentInfo;
import model.Hall;
import model.Loans;
import model.Location;
import model.Resources;
import model.Sanction;
import model.User;
import utils.ViewUtils;

public class EquipmentController {
	
    @FXML private TableView<Equipment> tableDisponibles;
    @FXML private TableColumn<Equipment, String> nombreTableOne, tipoTableOne, categoriatipoTableOne, marcaTableOne, serieTableOne, estadoTableOne, descripcionTableOne;
    
    
    @FXML private TableView<EqupmentInfo> tablePrestados;
    @FXML private TableColumn<EqupmentInfo, String> nombreTableTwo, descripcionTableTwo, docenteTableTwo, especificacionesTableTwo, serieTableTwo, entregadoTableTwo, estadoTableTwo;
    
    
    @FXML private TextField nombreTablaOneField, tipoTablaOneField, marcaTablaOneField, serieTablaOneField, estadoTablaOneField, 
    nombreTablaTwoField, correoTablaTwoField, serieTablaTwoField, entregadoTablaTwoField, estadoTablaTwoField;
    
    private FilteredList<Equipment> listaFiltradaDisponible;
    private FilteredList<EqupmentInfo> listaFiltradaPrestados;
    
    private Connection database = DataBase.getInstance().getConnection();
    private ResourcesDAO resourcesDao = new ResourcesDAO(database);
    private LoanDAO loanDao = new LoanDAO(database);
    private SanctionDAO sanctionDao = new SanctionDAO(database);
    
    @FXML void initialize() {
		// ------------------------------- Equipos Disponibles
    	
		ObservableList<Equipment> disponibles = FXCollections.observableArrayList();
		
		for (Equipment equipment : resourcesDao.EquipmentView()) { 
			disponibles.add(equipment);
		}
		
		nombreTableOne.setCellValueFactory(new PropertyValueFactory<>("name"));
		tipoTableOne.setCellValueFactory(new PropertyValueFactory<>("deviceType"));
		categoriatipoTableOne.setCellValueFactory(new PropertyValueFactory<>("category"));
		marcaTableOne.setCellValueFactory(new PropertyValueFactory<>("brand"));
		serieTableOne.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
		estadoTableOne.setCellValueFactory(new PropertyValueFactory<>("state"));
		descripcionTableOne.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		listaFiltradaDisponible = new FilteredList<>(disponibles, p -> true);
	    
		nombreTablaOneField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		tipoTablaOneField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		marcaTablaOneField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		serieTablaOneField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		estadoTablaOneField.textProperty().addListener((obs, oldVal, newVal) -> filtroDisponibles());
		
		tableDisponibles.setItems(listaFiltradaDisponible);
		
		// ------------------------------- Prestamos
		
		ObservableList<EqupmentInfo> prestamos = FXCollections.observableArrayList();
		
		for (EqupmentInfo loan : resourcesDao.EquipmentLoanView()) { 
			if (loan.getSpecs() == null) loan.setSpecs("Sin especificaciones");
			prestamos.add(loan);
		}

	    nombreTableTwo.setCellValueFactory(new PropertyValueFactory<>("name"));
	    descripcionTableTwo.setCellValueFactory(new PropertyValueFactory<>("description"));
	    docenteTableTwo.setCellValueFactory(new PropertyValueFactory<>("emailClient"));
	    especificacionesTableTwo.setCellValueFactory(new PropertyValueFactory<>("specs"));
	    serieTableTwo.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
	    entregadoTableTwo.setCellValueFactory(new PropertyValueFactory<>("dateLoan"));
	    estadoTableTwo.setCellValueFactory(new PropertyValueFactory<>("state"));

		listaFiltradaPrestados = new FilteredList<>(prestamos, p -> true);
		
	    nombreTablaTwoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
	    correoTablaTwoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
	    serieTablaTwoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
	    entregadoTablaTwoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
	    estadoTablaTwoField.textProperty().addListener((obs, oldVal, newVal) -> filtroPrestados());
	    
		tablePrestados.setItems(listaFiltradaPrestados);
    }
    
    private void filtroPrestados() {
        String nombre = nombreTablaTwoField.getText().toLowerCase();
        String correo = correoTablaTwoField.getText().toLowerCase();
        String serie = serieTablaTwoField.getText().toLowerCase();
        String fecha = entregadoTablaTwoField.getText().toLowerCase();
        String estado = estadoTablaTwoField.getText().toLowerCase();
        
        listaFiltradaPrestados.setPredicate(resource -> {
            boolean bNombre = resource.getName().toLowerCase().contains(nombre);
            boolean bCorreo = resource.getEmailClient().toLowerCase().contains(correo);
            boolean bSerie = resource.getSerialNumber().toLowerCase().contains(serie);
            boolean bFecha = resource.getDateLoan().toString().toLowerCase().contains(fecha);
            boolean bEstado = resource.getState().toLowerCase().contains(estado);
            
            return bNombre && bCorreo && bSerie && bFecha && bEstado;
        });
    }
    
    private void filtroDisponibles() {
        String nombre = nombreTablaOneField.getText().toLowerCase();
        String tipo = tipoTablaOneField.getText().toLowerCase();
        String marca = marcaTablaOneField.getText().toLowerCase();
        String serie = serieTablaOneField.getText().toLowerCase();
        String estado = estadoTablaOneField.getText().toLowerCase();
        
        listaFiltradaDisponible.setPredicate(resource -> {
            boolean bNombre = resource.getName().toLowerCase().contains(nombre);
            boolean bTipo = resource.getDeviceType().toLowerCase().contains(tipo);
            boolean bMarca = resource.getBrand().toLowerCase().contains(marca);
            boolean bSerie = resource.getSerialNumber().toLowerCase().contains(serie);
            boolean bEstado = resource.getState().toLowerCase().contains(estado);
            
            return bNombre && bTipo && bMarca && bSerie && bEstado;
        });
    }

    private Equipment selectResource() {
    	Equipment equipment = tableDisponibles.getSelectionModel().getSelectedItem();
    	if (equipment == null) {
    		ViewUtils.AlertWindow(null, null, "Debe seleccionar primero un equipo", AlertType.ERROR);
    		return null;
    	}
    	return equipment;
    }
    
    private EqupmentInfo selectLoan() {
    	EqupmentInfo loan = tablePrestados.getSelectionModel().getSelectedItem();
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
        nameField.setPromptText("Nombre - Ejemplo: ASUS");
        
        ComboBox<String> tipoField = new ComboBox<>();
        ObservableList<String> listTipo = FXCollections.observableArrayList(
        		"PORTÁTIL", "CÁMARA", "MICRÓFONO", "VÍDEO PROYECTOR", "HDMI", "ETHERNET");
        tipoField.setItems(listTipo);
        tipoField.setPromptText("Tipo de Dispositivo");
        tipoField.setPrefWidth(150);

        HBox BoxOne = new HBox(5); 
        BoxOne.getChildren().addAll(nameField, tipoField);
        
        grid.add(BoxOne, 0, 1, 2, 1);
        
    	ComboBox<String> stateField = new ComboBox<>();
    	
	    ObservableList<String> itemsState = FXCollections.observableArrayList(
		        "Disponible", "En Mantenimiento"
		    );
	    stateField.setPromptText("Estado");
	    stateField.setPrefWidth(140);
	    stateField.setItems(itemsState);
	    
    	ComboBox<String> categoryField = new ComboBox<>();
    	
	    ObservableList<String> itemsCategory = FXCollections.observableArrayList(
		        "Dispositivo", "Computadora de Mesa"
		    );
	    categoryField.setPromptText("Categoria");
	    categoryField.setPrefWidth(140);
	    categoryField.setItems(itemsCategory);
	    
        HBox Box = new HBox(5); 
        Box.getChildren().addAll(categoryField, stateField);
        
        grid.add(Box, 0, 2, 2, 1);
        
	    TextField brandField = new TextField();
	    brandField.setPrefWidth(100);
	    brandField.setPromptText("Marca");
	    
        grid.add(brandField, 0, 3);
        
	    TextField seriesField = new TextField();
	    seriesField.setPrefWidth(100);
	    seriesField.setPromptText("Serie");
	    
        grid.add(seriesField, 1, 3);
        
	    TextField descField = new TextField();
	    descField.setPrefWidth(100);
	    descField.setPromptText("Descripción");
	    
        grid.add(descField, 0, 5, 2, 2);
        

        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
        		String Name = nameField.getText().trim();
        		String Category = categoryField.getValue() != null ? categoryField.getValue().trim() : null;
        		String State = stateField.getValue() != null ? stateField.getValue().trim() : "";
        		String Tipo = tipoField.getValue() != null ? tipoField.getValue() : null;
        		String Marca = brandField.getText().trim();
        		String Serie = seriesField.getText().trim();
        		String Description = descField.getText().trim();
        		
                if (Name.isEmpty() || 
                		Marca.isEmpty() || 
                		Serie.isEmpty() ||
                		Description.isEmpty() ||
                		State.isEmpty()) {
                	ViewUtils.AlertWindow(null, "Campos vacíos", "Por favor, complete todos los campos.", AlertType.ERROR);
                    return null;
                }
                if (resourcesDao.AuthenticateHall(Name)) {
                	ViewUtils.AlertWindow(null, "Sala existente", "La sala "+Name+" ya estaba registrada.", AlertType.ERROR);
                	return null;
                }

                /*
                
                resourcesDao.saveHall(new Hall(null, Name, id_location, String.valueOf(Capacity), Description, State));
                */
                ViewUtils.AlertWindow(null, "Sala creada", "La sala "+Name+" ha sido registrada con éxito.", AlertType.INFORMATION);
            }
            return null;
        });
        dialog.showAndWait();
        initialize();
    }
    
    @FXML void eliminar() {
    	Equipment resource = selectResource();
    	if (resource != null) {
    		if (ViewUtils.showConfirmation("Confirmación", "¿Está seguro que desea eliminar el quipo "+resource.getName()+"?")) {
    			System.out.println("Elimino");
    		}
    		// Si tiene prestamos aprobados no se puede eliminar
    	}
    }
    
    @FXML void pedir() {
    	Equipment resource = selectResource();
    	if (resource != null) {
        	if (resource.getState().equals("En Mantenimiento")) {
            	ViewUtils.AlertWindow(null, "En Mantenimiento", "No puedes pedir un equipo en mantenimiento.", AlertType.ERROR);
            	return;
        	}
        	Main.datoGlobal = resource;
        	ViewUtils.cargarGrid("/views/RequestEquipment.fxml", Main.rootLayout);
    	}
    }
    @FXML void aprobar() {
    	EqupmentInfo loan = selectLoan();
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
    		if (ViewUtils.showConfirmation("Confirmación", "Está apunto de aprobar el prestamo de "+loan.getEmailClient()+"\n"
    				+ "- Equipo: "+loan.getName()+"\n- Marca: "+loan.getBrand()+"\n- Especificaciones: "+loan.getSpecs())) {
    			
    			if (loanDao.updateState(loan.getId(), "APROBADO")) {
    				initialize();
    				ViewUtils.AlertWindow(null, "Prestamo Aprobado", "El prestamo de "+loan.getEmailClient()+" ha sido aprobado con éxito.", AlertType.INFORMATION);
    			}
    		}
    	}
    	
    }
    
    @FXML void devolvio() {
    	EqupmentInfo loan = selectLoan();
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
    		if (ViewUtils.showConfirmation("Confirmación", "Está apunto de aprobar la entrega del prestamo de "+loan.getEmailClient()+"\n"
    				+ "- Equipo: "+loan.getName()+"\n- Marca: "+loan.getBrand()+"\n- Especificaciones: "+loan.getSpecs())) {
    			
    			if (loanDao.updateState(loan.getId(), "FINALIZADO")) {
    				initialize();
    				ViewUtils.AlertWindow(null, "Prestamo Finalizado", "El prestamo de "+loan.getEmailClient()+" ha finalizado con éxito.", AlertType.INFORMATION);
    			}
    		}
    	}
    }
    
    @FXML void rechazar() {
    	EqupmentInfo loan = selectLoan();
    	if (loan != null) {
    		if (loan.getState().equals("RECHAZADO")) {
    			ViewUtils.AlertWindow(null, "Ya está rechazado", "El prestamo ya estaba rechazado.", AlertType.ERROR);
    			return;
    		} else if (loan.getState().equals("FINALIZADO")) {
    			ViewUtils.AlertWindow(null, "No hay posibilidad", "El prestamo está finalizado, ya no se puede rechazar", AlertType.ERROR);
    			return;
    		}
    		if (ViewUtils.showConfirmation("Confirmación", "Está apunto de rechazar el prestamo de "+loan.getEmailClient()+"\n"
    				+ "- Sala: "+loan.getName()+"\n- Marca: "+loan.getBrand()+"\n- Especificaciones: "+loan.getSpecs())) {
    			
    			if (loanDao.updateState(loan.getId(), "RECHAZADO")) {
    				initialize();
    				ViewUtils.AlertWindow(null, "Prestamo Rechazado", "El prestamo de "+loan.getEmailClient()+" ha sido rechazado.", AlertType.INFORMATION);
    			}
    		}
    	}
    }
    
    @FXML void sancionar() {
    	EqupmentInfo loan = selectLoan();
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
