package controllers;

import java.sql.Connection;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.Main;
import data.DataBase;
import data.Filter;
import data.ResourcesDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Resources;
import utils.ViewUtils;

public class RequestConsultationController {

    @FXML
    private TextField buildingText;

    @FXML
    private TableColumn<Resources, String> typeCapacityColumn;

    @FXML
    private TextField capacityText;

    @FXML
    private Button filterButton;

    @FXML
    private TextField flatText;

    @FXML
    private GridPane gripe;

    @FXML
    private TableColumn<Resources, String> locationTrademarkColumn;

    @FXML
    private ContextMenu menuBuilding;

    @FXML
    private ContextMenu menuFlat;

    @FXML
    private ContextMenu menuName;

    @FXML
    private ContextMenu menuCapacity;

    @FXML
    private TableColumn<Resources, String> nameColumn;

    @FXML
    private TextField nameText;

    @FXML
    private TableView<Resources> tableResources;

    @FXML
    private Label title;
    
    ObservableList<Resources> resources = FXCollections.observableArrayList();
    
	private Connection database = DataBase.getInstance().getConnection();
	private  ResourcesDAO resourcesDAO = new ResourcesDAO(database);
	private Filter filter = new Filter(database);
	
    @FXML
    public void initialize() {  	
    	tableResources.setRowFactory(tv -> {
    	    TableRow<Resources> row = new TableRow<>();

    	    row.setOnMouseClicked(event -> {
    	        if (!row.isEmpty()) {
    	        	Resources resource = row.getItem();

    	            if (event.getClickCount() == 2) {
    	                // Doble clic
    	            	Main.datoGlobal = resource;
    	            	ViewUtils.cargarGrid("/views/Request.fxml", Main.rootLayout);
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
    }

    @FXML
    void handleDevices() {
    	visble();
    	buildingText.setPromptText("Tipo");
        GridPane.setColumnSpan(buildingText, 3);
    	flatText.setVisible(false);
    	capacityText.setPromptText("Marca");
    	title.setText("Solicitar recursos\n(Dispositivos)");
    	fillTable(resourcesDAO.ResourcesView(false, new StringBuilder()), "DISPOSITIVO", "TIPO DISPOSITIVO", "MARCA");
    	contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "EQUIPO", ""));
    	contextualAutocomplete(buildingText, menuBuilding, filter.Options("TIPO_DISPOSITIVO", "EQUIPO", ""));
    	contextualAutocomplete(capacityText, menuCapacity, filter.Options("MARCA", "EQUIPO", ""));
    }

    @FXML
    void handleFilter(ActionEvent event) {
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
    void handleHall(MouseEvent event) {
    	visble();
    	buildingText.setPromptText("Edificio");
        GridPane.setColumnSpan(buildingText, 1);
    	flatText.setVisible(true);
    	capacityText.setPromptText("Capacidad");
    	title.setText("Solicitar recursos\n(Salas)");
    	fillTable(resourcesDAO.ResourcesView(true, new StringBuilder()), "SALA", "CAPACIDAD","UBICACION");
    	contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "SALA", ""));
    	contextualAutocomplete(buildingText, menuBuilding, filter.Options("EDIFICIO", "UBICACION", ""));
    	contextualAutocomplete(capacityText, menuCapacity, filter.Options("CAPACIDAD", "SALA", ""));
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
    
    void visble() {
    	buildingText.setVisible(true);
    	flatText.clear();
    	capacityText.clear();
    	buildingText.clear();
    	capacityText.setVisible(true);
    	filterButton.setVisible(true);
    	tableResources.setVisible(true);
    	nameText.setVisible(true);
    	nameText.clear();
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
    
    @FXML
    void handleReturn() {
    	Main.rootLayout.setCenter(null);
    }
    
    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

}

