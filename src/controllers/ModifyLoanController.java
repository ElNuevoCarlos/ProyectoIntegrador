package controllers;

import java.sql.Connection;
import java.util.ArrayList;

import application.Main;
import data.DataBase;
import data.LoanDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.LoanTable;

public class ModifyLoanController {
	@FXML
	private DatePicker date;

	@FXML
	private TextArea info;
	
	@FXML
	private ListView<String> blocks;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField building;
	
	@FXML
	private TextField flat;
	
	@FXML
	private TextField capacity;
	
	
	LoanTable loan;
	
	private Connection database = DataBase.getInstance().getConnection();
	private LoanDAO loanDAO = new LoanDAO(database);
	
	@FXML
	public void initialize() {
	    Object dato = Main.datoGlobal;
	    if (dato instanceof LoanTable) {
	    	loan = (LoanTable) dato;
	    }
	    ArrayList<String> blocksArray = loanDAO.hallBlocks(loan.getId());
	    System.out.println(blocksArray);
	    ObservableList<String> observableList = FXCollections.observableArrayList(blocksArray);
	    System.out.println(observableList);
	    blocks.setItems(observableList);
	    String[] partsLocation = loan.getLocationType().split("-");
	    date.setValue(loan.getDate());
	    info.setText("Sala: " +loan.getName()+"\n"+"Fecha:" + loan.getDate() + "\n" +"Capacidad: "+ loan.getCapacity() + "\n"+ "Edificio: " + partsLocation[0].trim() + "\n" + "Piso: " + partsLocation[1].trim() + "\n" + "Especificaciones: " + loan.getSpecs());
		name.setText(loan.getName());
		building.setText(partsLocation[0].trim());
		flat.setText(partsLocation[1].trim());
		info.setText("Sala: " +loan.getName()+"\n"+"Fecha:" + loan.getDate() + "\n" +"Capacidad: "+ loan.getCapacity() + "\n"+ "Edificio: " + partsLocation[0].trim() + "\n" + "Piso: " + partsLocation[1].trim() + "\n" + "Especificaciones: Se solicita sala con capacidad mínima para 50 personas, equipada con aire acondicionado, proyector en buen estado, sistema de audio funcional, cortinas para evitar luz solar directa, pizarra acrílica con marcadores disponibles, conexión a internet estable mediante red WiFi, alargadores eléctricos disponibles, acceso controlado con llave, buena ventilación y cercanía a baños. Actividad programada es una capacitación técnica para docentes y estudiantes universitarios.");
		capacity.setText(loan.getCapacity());
		
	}
}
