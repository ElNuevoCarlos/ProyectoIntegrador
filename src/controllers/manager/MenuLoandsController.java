package controllers.manager;

import application.Main;
import data.SessionManager;
import javafx.fxml.FXML;
import utils.ViewUtils;

public class MenuLoandsController {
	private SessionManager sessionManager = SessionManager.getInstance();
    @FXML void equipos() {
    	if (sessionManager.getRole().equals("ENCARGADO") || sessionManager.getRole().equals("SUPERENCARGADO")) {
			ViewUtils.cargarGrid("/views/Manager/EquipmentLoan.fxml", Main.rootLayout);
    	} else {
    		// Si es true, me manda al apartado de mis equipos
    		if (Main.datoGlobalTwo) {
    			ViewUtils.cargarGrid("/views/Teacher/MyHalls.fxml", Main.rootLayout);
    		} else {
    			ViewUtils.cargarGrid("/views/Teacher/Loans.fxml", Main.rootLayout);
    		}
    	}
    }

    @FXML void salas() {
    	if (sessionManager.getRole().equals("ENCARGADO") || sessionManager.getRole().equals("SUPERENCARGADO")) {
			ViewUtils.cargarGrid("/views/Manager/Loans.fxml", Main.rootLayout);
    	} else {
    		// Si es true, me manda al apartado de mis prestamos
    		if (Main.datoGlobalTwo) {
    			ViewUtils.cargarGrid("/views/Teacher/MyHalls.fxml", Main.rootLayout);
    		} else {
    			ViewUtils.cargarGrid("/views/Teacher/Loans.fxml", Main.rootLayout);
    		}
    	}
    }

}
