package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class ModifyLoanController {
	@FXML
	private Spinner<Integer> hourSpinner;

	@FXML
	private Spinner<Integer> minuteSpinner;

	@FXML
	public void initialize() {
	    // Rango de horas: 0 a 23
	    SpinnerValueFactory<Integer> hourFactory =
	        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12); // Valor inicial: 12
	    hourSpinner.setValueFactory(hourFactory);

	    // Rango de minutos: 0 a 59
	    SpinnerValueFactory<Integer> minuteFactory =
	        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0); // Valor inicial: 0
	    minuteSpinner.setValueFactory(minuteFactory);
	}
}
