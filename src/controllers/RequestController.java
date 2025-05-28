package controllers;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

import application.Main;
import data.BlockDAO;
import data.DataBase;
import data.LoanDAO;
import data.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import model.Block;
import model.Loan;
import model.Resources;
import utils.ViewUtils;

public class RequestController {
    @FXML
    private Label info;

    @FXML
    private GridPane gripe;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<Block> blocks;

    @FXML
    private ListView<Block> myBlocks;
    
    @FXML
    private TextArea specsText;

    private ArrayList<Block> blocksArray;
    private ObservableList<Block> myBlocksArray;

    private Resources resource;

    private Connection database = DataBase.getInstance().getConnection();
    private BlockDAO blockDAO = new BlockDAO(database);
    private LoanDAO loanDAO = new LoanDAO(database);
    
    private LocalDate  date;
    
    private SessionManager sessionManager = SessionManager.getInstance();

    @FXML
    public void initialize() {
        Object dato = Main.datoGlobal;
        if (dato instanceof Resources) {
            resource = (Resources) dato;
        }

        myBlocksArray = FXCollections.observableArrayList();
        myBlocks.setItems(myBlocksArray);
        if (resource.getTypeResource()) {
            info.setText("Sala: " + resource.getName() + "\n"
                    + "Ubicación: " + resource.getLocationTrademark() + "\n"
                    + "Capacidad: " + resource.getTypeCapacity() + "\n"
                    + "Descripción: " + resource.getDescription());
        } else {
            info.setText("Dispositivo: " + resource.getName() + "\n"
                    + "Marca: " + resource.getLocationTrademark() + "\n"
                    + "Tipo dispositivo: " + resource.getTypeCapacity() + "\n"
                    + "Descripción: " + resource.getDescription());
        }


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
            if (resource.getTypeResource()) {
                blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_SALA = " + resource.getIdResource());
            } else {
                blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_EQUIPO = " + resource.getIdResource());
            }

            ObservableList<Block> observableList = FXCollections.observableArrayList(blocksArray);
            blocks.setItems(observableList);
            date = newValue;
        });


        blocks.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Block selected = blocks.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    blocks.getItems().remove(selected);
                    myBlocksArray.add(selected);
                }
            }
        });

        myBlocks.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Block selected = myBlocks.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    myBlocksArray.remove(selected);
                    blocks.getItems().add(selected);
                }
            }
        });
    }
    
    @FXML
    void handleReserve() {
    	if (date == null || myBlocksArray.isEmpty()) {
    		ViewUtils.AlertWindow(
    			    "Campos incompletos",
    			    "Faltan datos requeridos",
    			    "Por favor, completa todos los campos: fecha y horarios. Las especificaciones son opcionales.",
    			    AlertType.WARNING
    			);
    		return;
    	}
    	if (ViewUtils.showConfirmation("Confirmación", "¿Desea hacer la reserva de " + resource.getName() + ", en los horarios " + myBlocksArray + "?")) {
        	Loan loan;
        	if (resource.getTypeResource()) {
        		loan = new Loan(null, resource.getIdResource(), sessionManager.getId(), null, date, specsText.getText().trim(), "SOLICITADO", myBlocksArray);
        	} else {
        		loan = new Loan(null, null, sessionManager.getId(), resource.getIdResource(), date, specsText.getText().trim(), "SOLICITADO", myBlocksArray);
        	}
        	loanDAO.save(loan);
        	myBlocks.getItems().clear();
        	ViewUtils.AlertWindow(
        		    "Reserva solicitada",
        		    "Reserva pendiente",
        		    "La reserva de la sala está solicitada para los bloques, esperando a que sea aprobada.",
        		    AlertType.INFORMATION
        		);

    	} else {
    		ViewUtils.AlertWindow(
    			    "Reserva cancelada",
    			    "Reserva cancelada por ti",
    			    "Has cancelado la reserva. Si quieres, puedes hacer una nueva solicitud en cualquier momento.",
    			    AlertType.INFORMATION
    			);
    	}

    }
    
    @FXML
    void handleReturn() {
    	ViewUtils.cargarGrid("/views/RequestConsultation.fxml", Main.rootLayout);
    }

}

