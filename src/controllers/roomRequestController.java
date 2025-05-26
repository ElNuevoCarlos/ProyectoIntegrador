package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Block;
import model.Loan;
import model.Resources;

public class roomRequestController {
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
        info.setText("Sala: " + resource.getName() + "\n"
                + "Ubicación: " + resource.getLocationTrademark() + "\n"
                + "Capacidad: " + resource.getTypeCapacity() + "\n"
                + "Descripción: " + resource.getDescription());

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            blocksArray = blockDAO.hallBlocks(newValue, resource.getIdResource());
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
//    	Loan loan = new Loan(null, resource.getIdResource(), sessionManager.getId(), null, date, specsText.getText(), null);
//    	loanDAO.save(loan);
    }

}

