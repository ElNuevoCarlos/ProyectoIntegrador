package controllers.teacher;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.UserSession;
import utils.ViewUtils;

public class MenuController {
	@FXML private Text username, role;
	@FXML private BorderPane rootPane;
	public UserSession userSession = UserSession.getInstance();
	public String userRol = userSession.getRole();	
    @FXML void initialize() {
	    String[] partes = userSession.getName().split(" ");
	    String primerNombre = partes[0];
	    String primerApellido = partes.length > 2 ? partes[partes.length - 2] : partes[1];
        
    	username.setText(primerNombre + " " +primerApellido);
    	role.setText(userSession.getRole());
    	Main.rootLayout = rootPane;
    }
	
	@FXML void userInfo() {
		Main.datoGlobal = username;
		Main.datoGlobalTree = role;
		ViewUtils.cargarGrid("/views/MyAccount.fxml", rootPane);
	}
	
    @FXML void handleMyLoans() {
    	Main.datoGlobalTwo = true;
    	ViewUtils.cargarGrid("/views/MyLoans.fxml", rootPane);
    }
    
    @FXML void handleRequestConsultation() {
    	Main.datoGlobalTwo = false;
    	ViewUtils.cargarGrid("/views/Manager/MenuLoands.fxml", rootPane);
    }
    
    @FXML void goToBack() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
    }
}
