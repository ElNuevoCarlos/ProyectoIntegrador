package controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.LoanTable;

public class ModifyLoanController {
	@FXML
	private DatePicker date;

	@FXML
	private TextArea specs;
	
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
	
	@FXML
	public void initialize() {
	    Object dato = Main.datoGlobal;
	    if (dato instanceof LoanTable) {
	    	loan = (LoanTable) dato;
	    }
	    String[] partsLocation = loan.getLocationType().split("-");
	    date.setValue(loan.getDate());
	    specs.setText(loan.getSpecs());
		name.setText(loan.getName());
		building.setText(partsLocation[0].trim());
		flat.setText(partsLocation[1].trim());
		capacity.setText(String.valueOf(loan.getCapacity()));
	}
}
