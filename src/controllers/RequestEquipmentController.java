package controllers;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import application.Main;
import data.BlockDAO;
import data.DataBase;
import data.LoanDAO;
import data.SessionManager;
import data.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import model.Block;
import model.Equipment;
import model.EqupmentInfo;
import model.Loan;
import model.Resources;
import utils.ViewUtils;

public class RequestEquipmentController {
    @FXML private Label nombre, tipo, marca, descripcion;
    @FXML private GridPane gripe;
    @FXML private DatePicker datePicker;
    @FXML private ListView<Block> blocks, myBlocks;
    @FXML private TextArea specsText;

    private ArrayList<Block> blocksArray;
    private ObservableList<Block> myBlocksArray;

    private Equipment resource;

    private Connection database = DataBase.getInstance().getConnection();
    private SessionManager sessionManager = SessionManager.getInstance();
    private BlockDAO blockDAO = new BlockDAO(database);
    private LoanDAO loanDAO = new LoanDAO(database);
    private UserDAO userDao = new UserDAO(database);
    private LocalDate  date;

    @FXML void initialize() {
        Object dato = Main.datoGlobal;
        if (dato instanceof Equipment) {
            resource = (Equipment) dato;
        }

        myBlocksArray = FXCollections.observableArrayList();
        
        myBlocks.setItems(myBlocksArray);

        nombre.setText(resource.getName());
        tipo.setText(resource.getDeviceType());
        marca.setText(resource.getBrand());
        descripcion.setText(resource.getDescription());



        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            
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
            blocksArray = blockDAO.findAvailableBlocks(newValue, "p.ID_EQUIPO = " + resource.getId());


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
    
    @FXML void handleReserve() {
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

        	loan = new Loan(null, null, sessionManager.getId(), resource.getId(), date, specsText.getText().trim(), "SOLICITADO", myBlocksArray);
        	
        	loanDAO.save(loan);
        	myBlocks.getItems().clear();
        	ViewUtils.AlertWindow(
        		    "Reserva solicitada",
        		    "Reserva pendiente",
        		    "La reserva del equipo está solicitada para los bloques, esperando a que sea aprobada.",
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
}

