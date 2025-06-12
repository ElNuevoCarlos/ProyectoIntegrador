package controllers.manager;

import application.Main;
import javafx.fxml.FXML;
import model.UserSession;
import utils.ViewUtils;

public class MenuLoandsController {
	public UserSession userSession = UserSession.getInstance();
    @FXML void equipos() {
    	if (userSession.getRole().equals("ENCARGADO") || userSession.getRole().equals("SUPERENCARGADO")) {
			ViewUtils.cargarGrid("/views/Manager/EquipmentLoan.fxml", Main.rootLayout);
    	} else {
    		// Si es true, me manda al apartado de mis equipos
    		if (Main.datoGlobalTwo) {
    			ViewUtils.cargarGrid("/views/Teacher/MyHalls.fxml", Main.rootLayout);
    		} else {
    			ViewUtils.cargarGrid("/views/Teacher/Equipment.fxml", Main.rootLayout);
    		}
    	}
    }

    @FXML void salas() {
    	if (userSession.getRole().equals("ENCARGADO") || userSession.getRole().equals("SUPERENCARGADO")) {
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
