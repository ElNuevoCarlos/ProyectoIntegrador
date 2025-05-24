package controllers;

import java.sql.Connection;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import data.DataBase;
import data.Filter;
import data.LoanDAO;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Resources;
import model.LoanTable;

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
    private ContextMenu menuState;

    @FXML
    private ContextMenu menuType;

    @FXML
    private TableColumn<Resources, String> nameColumn;

    @FXML
    private TextField nameText;

    @FXML
    private TableView<Resources> tableResources;

    @FXML
    private Label title;

    @FXML
    private TextField typeText;
    
    ObservableList<Resources> resources = FXCollections.observableArrayList();
    
	private Connection database = DataBase.getInstance().getConnection();
	private  ResourcesDAO resourcesDAO = new ResourcesDAO(database);
	private Filter filter = new Filter(database);

    @FXML
    void handleDevices() {
    	
    }

    @FXML
    void handleFilter(ActionEvent event) {

    }

    @FXML
    void handleHall(MouseEvent event) {
    	buildingText.setVisible(true);
    	flatText.setVisible(true);
    	typeText.setVisible(false);
    	title.setText("MIS PRESTAMOS\n(SALAS)");
    	visble();
    	fillTable(resourcesDAO.ResourcesView(true, new StringBuilder()), "SALA", "CAPACIDAD","UBICACION");
    	contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "SALA", ""));
    	contextualAutocomplete(buildingText, menuBuilding, filter.Options("EDIFICIO", "UBICACION", ""));
    	contextualAutocomplete(capacityText, menuState, filter.Options("CAPACIDAD", "SALA", ""));
    }
    
//    @FXML
//    void handleFilter() {
//    	String name = nameText.getText().trim();
//        String state = stateText.getText().trim();
//        LocalDate start = startInterval.getValue();
//        LocalDate end = endInterval.getValue();
//        StringBuilder query = new StringBuilder();
//        if (start != null) {
//            if (end != null) {
//                query.append(" AND (TRUNC(p.FECHA) BETWEEN TO_DATE('")
//                     .append(start)
//                     .append("', 'YYYY-MM-DD') AND TO_DATE('")
//                     .append(end)
//                     .append("', 'YYYY-MM-DD'))");
//            } else {
//                query.append(" AND TRUNC(p.FECHA) = TO_DATE('")
//                     .append(start)
//                     .append("', 'YYYY-MM-DD')");
//            }
//        }
//    	if(!typeText.isVisible()) { 
//            String building = buildingText.getText().trim();
//            String flat = flatText.getText().trim();
//
//            if (!name.isEmpty()) {
//                query.append(" AND s.NOMBRE = '").append(name).append("'");
//            }
//            if (!building.isEmpty()) {
//                query.append(" AND u.EDIFICIO = '").append(building).append("'");
//            }
//            if (!flat.isEmpty()) {
//                query.append(" AND u.PISO = '").append(flat).append("'");
//            }
//            if (!state.isEmpty()) {
//                query.append(" AND p.ESTADO = '").append(state).append("'");
//            }
//            if (query.length() > 0) {
//            	fillTable(loanDAO.MyLoansView(sessionManager.getId(), true, query),  "SALA", "UBICACION");
//            } 
//    	} else {
//    		String type = typeText.getText().trim();
//    		
//            if (!name.isEmpty()) {
//                query.append(" AND e.NOMBRE = '").append(name).append("'");
//            }
//            if (!type.isEmpty()) {
//                query.append(" AND e.TIPO_DISPOSITIVO = '").append(type).append("'");
//            }
//            if (!state.isEmpty()) {
//                query.append(" AND p.ESTADO = '").append(state).append("'");
//            }
//            if (query.length() > 0) {
//            	fillTable(loanDAO.MyLoansView(sessionManager.getId(), false, query),  "DISPOSITIVO", "TIPO DISPOSITIVO");
//            } 
//    	}
//    }
    
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
    
    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

}

