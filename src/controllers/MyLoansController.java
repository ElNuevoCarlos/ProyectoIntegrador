package controllers;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.Main;
import data.DataBase;
import data.Filter;
import data.LoanDAO;
import data.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import model.LoanTable;

public class MyLoansController {
    @FXML
    private GridPane gripe;

    @FXML 
    private TableView<LoanTable> tableLoan;

    @FXML 
    private TableColumn<LoanTable, Long> idColumn;

    @FXML 
    private TableColumn<LoanTable, Timestamp> dateColumn;
    
    @FXML 
    private TableColumn<LoanTable, String> nameColumn;

    @FXML 
    private TableColumn<LoanTable, String> locationColumn;

    @FXML 
    private TableColumn<LoanTable, String> stateColumn;
    
    @FXML
    private Label title;
    
    @FXML 
    private TextField nameText;
    
    @FXML
    private TextField stateText;
    
    @FXML
    private TextField buildingText;
    
    @FXML
    private TextField flatText;
    
    @FXML
    private TextField typeText;
    
    @FXML
    private DatePicker startInterval;
    
    @FXML 
    private DatePicker endInterval;
    
    @FXML
    private ContextMenu menuName;
    
    @FXML
    private ContextMenu menuState;
    
    @FXML
    private ContextMenu menuBuilding;
    
    @FXML
    private ContextMenu menuFlat;
    
    @FXML
    private ContextMenu menuType;
    
    @FXML
    private Button filterButton;
    
	private Connection database = DataBase.getInstance().getConnection();
	private LoanDAO loanDAO = new LoanDAO(database);
	private Filter filter = new Filter(database);
    private SessionManager sessionManager = SessionManager.getInstance();

    ObservableList<LoanTable> Loans = FXCollections.observableArrayList();

    @FXML
    public void initialize() {  	
    	tableLoan.setRowFactory(tv -> {
    	    TableRow<LoanTable> row = new TableRow<>();

    	    row.setOnMouseClicked(event -> {
    	        if (!row.isEmpty()) {
    	            LoanTable loan = row.getItem();

    	            if (event.getClickCount() == 2) {
    	                // Doble clic
    	            	Main.datoGlobal = loan;
    	            	Main.cargarGrid("/views/ModifyLoan.fxml", Main.rootLayout);
    	            } else {
    	                // Clic simple
    	                if (row.isSelected()) {
    	                    tableLoan.getSelectionModel().clearSelection();
    	                } else {
    	                    tableLoan.getSelectionModel().select(loan);
    	                }
    	            }
    	        }
    	    });

    	    return row;
    	});
    }
    
    @FXML
    void handleHall() {
    	buildingText.setVisible(true);
    	flatText.setVisible(true);
    	typeText.setVisible(false);
    	title.setText("MIS PRESTAMOS\n(SALAS)");
    	visble();
    	fillTable(loanDAO.MyLoansView(sessionManager.getId(), true, new StringBuilder()), "SALA", "UBICACION");
    	contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "SALA", ""));
    	contextualAutocomplete(buildingText, menuBuilding, filter.Options("EDIFICIO", "UBICACION", ""));
    	contextualAutocomplete(stateText, menuState, filter.Options("ESTADO", "PRESTAMO", ""));
    }
    
    void fillTable(ArrayList<LoanTable> listLoans, String name, String locationType) {
    	tableLoan.getItems().clear();
    	nameColumn.setText(name);
    	locationColumn.setText(locationType);
		for (LoanTable loanView : listLoans) {
			Loans.add(loanView);
		}
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("locationType"));
		stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
		tableLoan.setItems(Loans);
    }
    
    @FXML
    void handleDevices() {
    	buildingText.setVisible(false);
    	flatText.setVisible(false);
    	typeText.setVisible(true);
    	title.setText("MIS PRESTAMOS\n(DISPOSITIVOS)");
    	visble();
    	fillTable(loanDAO.MyLoansView(sessionManager.getId(), false, new StringBuilder()), "DISPOSITIVO", "TIPO DISPOSITIVO");
    	contextualAutocomplete(nameText, menuName, filter.Options("NOMBRE", "EQUIPO", ""));
    	contextualAutocomplete(typeText, menuType, filter.Options("TIPO_DISPOSITIVO", "EQUIPO", ""));
    	
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

    @FXML
    void handleFilter() {
    	String name = nameText.getText().trim();
        String state = stateText.getText().trim();
        LocalDate start = startInterval.getValue();
        LocalDate end = endInterval.getValue();
        StringBuilder query = new StringBuilder();
        if (start != null) {
            if (end != null) {
                query.append(" AND (TRUNC(p.FECHA) BETWEEN TO_DATE('")
                     .append(start)
                     .append("', 'YYYY-MM-DD') AND TO_DATE('")
                     .append(end)
                     .append("', 'YYYY-MM-DD'))");
            } else {
                query.append(" AND TRUNC(p.FECHA) = TO_DATE('")
                     .append(start)
                     .append("', 'YYYY-MM-DD')");
            }
        }
        
        if (!state.isEmpty()) {
            query.append(" AND p.ESTADO = '").append(state).append("'");
        }
        
    	if(!typeText.isVisible()) { 
            String building = buildingText.getText().trim();
            String flat = flatText.getText().trim();

            if (!name.isEmpty()) {
                query.append(" AND s.NOMBRE = '").append(name).append("'");
            }
            if (!building.isEmpty()) {
                query.append(" AND u.EDIFICIO = '").append(building).append("'");
            }
            if (!flat.isEmpty()) {
                query.append(" AND u.PISO = '").append(flat).append("'");
            }

            if (query.length() > 0) {
            	fillTable(loanDAO.MyLoansView(sessionManager.getId(), true, query),  "SALA", "UBICACION");
            } else {
            	
            }
    	} else {
    		String type = typeText.getText().trim();
    		
            if (!name.isEmpty()) {
                query.append(" AND e.NOMBRE = '").append(name).append("'");
            }
            if (!type.isEmpty()) {
                query.append(" AND e.TIPO_DISPOSITIVO = '").append(type).append("'");
            }
            if (query.length() > 0) {
            	fillTable(loanDAO.MyLoansView(sessionManager.getId(), false, query),  "DISPOSITIVO", "TIPO DISPOSITIVO");
            } 
    	}
    }
    
    void visble() {
    	filterButton.setVisible(true);
    	tableLoan.setVisible(true);
    	nameText.setVisible(true);
    	nameText.clear();
    	stateText.setVisible(true);
    	stateText.clear();
    	startInterval.setVisible(true);
    	startInterval.setValue(null);
    	endInterval.setVisible(true);
    	startInterval.setValue(null);
    }
    
    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }
}


