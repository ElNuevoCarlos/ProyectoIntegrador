package controllers;

import java.sql.Connection;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import application.Main;
import data.BlockDAO;
import data.DBConnectionFactory;
import data.Filter;
import data.LoanDAO;
import data.ResourcesDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import model.Block;
import model.LoanTable;
import model.Resources;
import model.UserSession;
import utils.ViewUtils;

public class ModifyLoanController {
	@FXML private DatePicker datePicker;

	@FXML private TextArea info, specs;

	@FXML private ListView<Block> blocks;
    @FXML private ListView<Block> myBlocks;
    
	@FXML private TextField nameText, buildingText, flatText, capacityText;

    @FXML private TableView<Resources> tableResources;
    @FXML private TableColumn<Resources, String> typeCapacityColumn;
    @FXML private TableColumn<Resources, String> locationTrademarkColumn;
    @FXML private TableColumn<Resources, String> nameColumn;
    
    @FXML private ContextMenu menuName;
    @FXML private ContextMenu menuCapacity; 
    @FXML private ContextMenu menuBuilding;
    @FXML private ContextMenu menuFlat;
    
	@SuppressWarnings("unused")
	private LocalDate date;
	
	LoanTable loan;
	
    public String userRol = UserSession.getInstance().getRole();
    private Connection connection = DBConnectionFactory.getConnectionByRole(userRol).getConnection();
	private  ResourcesDAO resourcesDAO = new ResourcesDAO(connection);
	private Filter filter = new Filter(connection);
	private BlockDAO blockDAO = new BlockDAO(connection);
	private LoanDAO loanDAO = new LoanDAO(connection);
	
	private Resources resource = null;

	ObservableList<Resources> resources = FXCollections.observableArrayList();
	
	private ObservableList<Block> availableBlocks = FXCollections.observableArrayList();
	private ObservableList<Block> selectedBlocks = FXCollections.observableArrayList();
	
	private ArrayList<Block> blocksArray;
	private ArrayList<Block> myBlocksInitial;
	
	@FXML
	void initialize() {
	    Object dato = Main.datoGlobal;
	    if (dato instanceof LoanTable) {
	        loan = (LoanTable) dato;
	    }

	    specs.setText(loan.getSpecs());
	    datePicker.setValue(loan.getDate());
	    blocksArray = blockDAO.findBlocksByLoanId(loan.getId());
	    myBlocksInitial = new ArrayList<>(blocksArray);
	    selectedBlocks.setAll(blocksArray);
	    myBlocks.setItems(selectedBlocks);

	    if (!"0".equals(loan.getCapacity())) {
	        blocksArray = blockDAO.findAvailableBlocks(loan.getDate(), "p.ID_SALA = (SELECT ID_SALA FROM PRESTAMO WHERE ID = " + loan.getId() + ")");
	        String[] partsLocation = loan.getLocationType().split("-");
	        info.setText("Sala: " + loan.getName() + "\n" + "Fecha:" + loan.getDate() + "\n" + "Capacidad: " + loan.getCapacity() + "\n" + "Edificio: " + partsLocation[0].trim() + "\n" + "Piso: " + partsLocation[1].trim() + "\n" + "Estado: " + loan.getState() + "\n" + "Especificaciones: " + loan.getSpecs());
	        buildingText.setPromptText("Edificio");
	        flatText.setVisible(true);
	        capacityText.setPromptText("Capacidad");
	        fillTable(resourcesDAO.ResourcesView(true, new StringBuilder()), "SALA", "CAPACIDAD", "UBICACION");
	        contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "SALA", ""));
	        contextualAutocomplete(buildingText, menuBuilding, filter.Options("EDIFICIO", "UBICACION", ""));
	        contextualAutocomplete(capacityText, menuCapacity, filter.Options("CAPACIDAD", "SALA", ""));
	    } else {
	        blocksArray = blockDAO.findAvailableBlocks(loan.getDate(), "p.ID_EQUIPO = (SELECT ID_EQUIPO FROM PRESTAMO WHERE ID = " + loan.getId() + ")");
	        capacityText.setEditable(true);
	        buildingText.setPromptText("Tipo");
	        flatText.setVisible(false);
	        capacityText.setPromptText("Marca");
	        info.setText("Dispositivo: " + loan.getName() + "\n" + "Fecha:" + loan.getDate() + "\n" + "Tipo: " + loan.getLocationType() + "\n" + "Estado: " + loan.getState() + "\n" + "Especificaciones: " + loan.getSpecs());
	        fillTable(resourcesDAO.ResourcesView(false, new StringBuilder()), "DISPOSITIVO", "TIPO DISPOSITIVO", "MARCA");
	        contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "EQUIPO", ""));
	        contextualAutocomplete(buildingText, menuBuilding, filter.Options("TIPO_DISPOSITIVO", "EQUIPO", ""));
	        contextualAutocomplete(capacityText, menuCapacity, filter.Options("MARCA", "EQUIPO", ""));
	    }

	    availableBlocks.setAll(blocksArray);
	    blocks.setItems(availableBlocks);

	    tableResources.setRowFactory(tv -> {
	        TableRow<Resources> row = new TableRow<>();

	        row.setOnMouseClicked(event -> {
	            if (!row.isEmpty()) {
	                resource = row.getItem();

	                if (event.getClickCount() == 2) {
	                    // Doble clic
	                    datePicker.setValue(null);
	                    myBlocks.getItems().clear();
	                    blocks.getItems().clear();
	                    selectedBlocks.clear();       // <-- limpiar internamente los bloques seleccionados
	                    availableBlocks.clear();      // <-- limpiar internamente los bloques disponibles

	                    if (resource.getTypeResource()) {
	                        String[] partsLocation = resource.getLocationTrademark().split("-");
	                        info.setText("Sala: " + resource.getName() + "\n" + "Capacidad: " + resource.getTypeCapacity() + "\n" + "Edificio: " + partsLocation[0].trim() + "\n" + "Piso: " + partsLocation[1].trim() + "\n" + "Descripcion: " + resource.getDescription());
	                    } else {
	                        info.setText("Dispositivo: " + resource.getName() + "\n" + "Tipo: " + resource.getTypeCapacity() + "\n" + "Marca: " + resource.getLocationTrademark() + "\n" + "Descripcion: " + resource.getDescription());
	                    }
	                } else {
	                    // Clic simple
	                    if (row.isSelected()) {
	                        tableResources.getSelectionModel().clearSelection();
	                    } else {
	                        tableResources.getSelectionModel().select(resource);
	                    }
	                }
	            }
	        });

	        return row;
	    });

	    datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue == null) {
	            return;
	        }

	        if (newValue.isBefore(LocalDate.now()) || newValue.isAfter(LocalDate.now().plusDays(15))) {
	            ViewUtils.AlertWindow(
	                "Fecha inválida",
	                "La fecha seleccionada no es válida",
	                "Por favor, selecciona una fecha desde hoy hasta un máximo de 15 días en el futuro.",
	                AlertType.WARNING
	            );
	            datePicker.setValue(null);
	            return;
	        }

	        myBlocks.getItems().clear();
	        selectedBlocks.clear();
	        availableBlocks.clear();

	        if (resource == null) {
	            if (!"0".equals(loan.getCapacity())) {
	                blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_SALA = (SELECT ID_SALA FROM PRESTAMO WHERE ID = " + loan.getId() + ")");
	            } else {
	                blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_EQUIPO = (SELECT ID_EQUIPO FROM PRESTAMO WHERE ID = " + loan.getId() + ")");
	            }
	        } else {
	            if (resource.getTypeResource()) {
	                blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_SALA = " + resource.getIdResource());
	            } else {
	                blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_EQUIPO = " + resource.getIdResource());
	            }
	        }

	        availableBlocks.setAll(blocksArray);
	        blocks.setItems(availableBlocks);
	        date = newValue;
	    });

	    blocks.setOnMouseClicked((MouseEvent event) -> {
	        if (event.getClickCount() == 2) {
	            Block selected = blocks.getSelectionModel().getSelectedItem();
	            if (selected != null) {
	                availableBlocks.remove(selected);
	                selectedBlocks.add(selected);
	            }
	        }
	    });

	    myBlocks.setOnMouseClicked((MouseEvent event) -> {
	        if (event.getClickCount() == 2) {
	            Block selected = myBlocks.getSelectionModel().getSelectedItem();
	            if (selected != null) {
	                selectedBlocks.remove(selected);
	                availableBlocks.add(selected);
	            }
	        }
	    });
	}

	
	@FXML void handleFilter() {
    	String name = nameText.getText().trim();
        StringBuilder query = new StringBuilder();
        String building = buildingText.getText().trim();
    	if(!buildingText.getPromptText().equals("Tipo")) {            
            String flat = flatText.getText().trim();
            String capacity = capacityText.getText().trim();
            
            if (!name.isEmpty()) {
                query.append(" AND s.NOMBRE = '").append(name).append("'");
            }
            if (!building.isEmpty()) {
                query.append(" AND u.EDIFICIO = '").append(building).append("'");
            }
            if (!flat.isEmpty()) {
                query.append(" AND u.PISO = '").append(flat).append("'");
            }
            if (!capacity.isEmpty()) {
            	query.append(" AND s.CAPACIDAD = '").append(capacity).append("'");
            }
            if (query.length() > 0) {
            	fillTable(resourcesDAO.ResourcesView(true, query), "SALA", "CAPACIDAD", "UBICACION");
            } 
    	} else {

            if (!name.isEmpty()) {
                query.append(" AND NOMBRE = '").append(name).append("'");
            }
            if (!building.isEmpty()) {
                query.append(" AND TIPO_DISPOSITIVO = '").append(building).append("'");
            }
            if (query.length() > 0) {
            	fillTable(resourcesDAO.ResourcesView(false, query), "DISPOSITIVO", "TIPO DISPOSITIVO", "MARCA");
            } 
    	}
	}
	
	@FXML
	void handleUpdate() {
	    // Validación de fecha
	    LocalDate selectedDate = datePicker.getValue();
	    if (selectedDate == null) {
	        ViewUtils.AlertWindow("Error", null, "Por favor selecciona una fecha válida.", AlertType.ERROR);
	        return;
	    }

	    // Validación de bloques seleccionados
	    if (selectedBlocks.isEmpty()) {
	        ViewUtils.AlertWindow("Error", null, "El préstamo debe tener asignado al menos un bloque.", AlertType.ERROR);
	        return;
	    }

	    // CASO 1: Sin cambiar de recurso
	    if (resource == null) {
	        // Detectar nuevos bloques
	        List<Block> newBlocks = new ArrayList<>(selectedBlocks);
	        newBlocks.removeAll(myBlocksInitial);

	        // Detectar bloques eliminados
	        List<Block> deletedBlocks = new ArrayList<>(myBlocksInitial);
	        deletedBlocks.removeAll(selectedBlocks);

	        // Guardar nuevos bloques
	        for (Block block : newBlocks) {
	            blockDAO.saveBlock(loan.getId(), block.getId());
	        }

	        // Eliminar bloques removidos
	        for (Block block : deletedBlocks) {
	            blockDAO.deleteBlock(loan.getId(), block.getId());
	        }

	        // Actualizar el préstamo (solo fecha y especificaciones)
	        loanDAO.update(java.sql.Date.valueOf(selectedDate), specs.getText(), null, null, loan.getId());
	        
		    ViewUtils.AlertWindow("Actulizado", null, "El préstamo ha sido actualizado", AlertType.INFORMATION);
		    ViewUtils.cargarGrid("/views/MyLoans.fxml", Main.rootLayout);

	        // Actualizar estado inicial
	        myBlocksInitial = new ArrayList<>(selectedBlocks);

	        System.out.println("Actualización sin cambio de recurso.");
	        System.out.println("Nuevos: " + newBlocks);
	        System.out.println("Eliminados: " + deletedBlocks);
	    }

	    // CASO 2: Cambio de recurso (sala/dispositivo)
	    else {
	        // Primero eliminar todos los bloques anteriores
	        for (Block block : myBlocksInitial) {
	            blockDAO.deleteBlock(loan.getId(), block.getId());
	        }

	        // Guardar nuevos bloques
	        for (Block block : selectedBlocks) {
	            blockDAO.saveBlock(loan.getId(), block.getId());
	        }

	        // Actualizar el préstamo con nuevo recurso
	        if ("0".equals(loan.getCapacity())) {
	            // Recurso es dispositivo
	            loanDAO.update(java.sql.Date.valueOf(selectedDate), specs.getText(), null, resource.getIdResource(), loan.getId());
	        } else {
	            // Recurso es sala
	            loanDAO.update(java.sql.Date.valueOf(selectedDate), specs.getText(), resource.getIdResource(), null, loan.getId());
	        }

		    ViewUtils.AlertWindow("Actulizado", null, "El préstamo ha sido actualizado", AlertType.INFORMATION);
		    ViewUtils.cargarGrid("/views/MyLoans.fxml", Main.rootLayout);
	        // Actualizar estado inicial
	        myBlocksInitial = new ArrayList<>(selectedBlocks);

	        System.out.println("Recurso cambiado. Nuevos bloques: " + selectedBlocks);
	    }
	    
	    

	    
	}
	
	@FXML
	void handleVolver() {
		ViewUtils.cargarGrid("/views/MyLoans.fxml", Main.rootLayout);
	}


	
    void fillTable(ArrayList<Resources> listResource, String name, String typeCapacity, String locationTrademark) {
    	tableResources.getItems().clear();
    	nameColumn.setText(name);
    	typeCapacityColumn.setText(typeCapacity);
    	locationTrademarkColumn.setText(locationTrademark);
    	
		for (Resources hallDevice : listResource) {
			resources.add(hallDevice);
		}
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		typeCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("typeCapacity"));
		locationTrademarkColumn.setCellValueFactory(new PropertyValueFactory<>("locationTrademark"));
		tableResources.setItems(resources);
    }
    
    void contextualAutocomplete(TextField textField, ContextMenu suggestionsMenu, List<String> options) {	
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                suggestionsMenu.hide();
            } else {
            	showMatches(textField, suggestionsMenu, options, newText);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String currentText = textField.getText();
                if (currentText.isEmpty()) {

                	showMatches(textField, suggestionsMenu, options, "");
                }
            }
        });
    }
    
    private void showMatches(TextField textField, ContextMenu suggestionsMenu, List<String> options, String filtro) {
        List<String> matches = options.stream()
                .filter(item -> removeAccents(item).contains(removeAccents(filtro)))
                .collect(Collectors.toList());

        if (!matches.isEmpty()) {
            List<CustomMenuItem> items = new ArrayList<>();
            for (String match : matches) {
                Label entryLabel = new Label(match);
                CustomMenuItem item = new CustomMenuItem(entryLabel, true);
                item.setOnAction(e -> {
                    textField.setText(match);
                    if (textField == buildingText) {
                    	flatText.setEditable(true);
                    	contextualAutocomplete(flatText, menuFlat, filter.Options("PISO", "UBICACION", match));
                    }
                    suggestionsMenu.hide();
                });
                items.add(item);
            }
            suggestionsMenu.getItems().setAll(items);
            if (!suggestionsMenu.isShowing()) {
                suggestionsMenu.show(textField, Side.BOTTOM, 0, 0);
            }
        } else {
            suggestionsMenu.hide();
        }
    }
    
    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }
}
