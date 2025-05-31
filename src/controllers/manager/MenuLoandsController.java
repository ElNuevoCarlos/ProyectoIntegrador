package controllers.manager;

import application.Main;
import javafx.fxml.FXML;
import utils.ViewUtils;

public class MenuLoandsController {

    @FXML void equipos() {

    }

    @FXML void salas() {
    	ViewUtils.cargarGrid("/views/Manager/Loands.fxml", Main.rootLayout);
    }

}
