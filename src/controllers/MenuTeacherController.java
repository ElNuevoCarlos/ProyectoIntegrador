package controllers;

import java.sql.Connection;
import application.Main;
import data.DataBase;
import data.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.ViewUtils;


public class MenuTeacherController {
	@FXML
	private BorderPane rootPane;
	@FXML
	private Pane pane;
	@FXML
	private ImageView userImage, myLoansImage, requestConsultImage, closeSession;
	@FXML
	private Hyperlink editUser;
	@FXML
	private Text myLoans, requestConsult;
	@FXML
	private Separator separator1, separator2;

	
	private Connection database = DataBase.getInstance().getConnection();

	@FXML
	public void initialize() {
		rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
		String[] partes = SessionManager.getInstance().getName().trim().split("\\s+");
		editUser.setText(partes[0] + " " + partes[1] + "\n" + SessionManager.getInstance().getRole());
		editUser.setStyle("-fx-text-fill: #0f0f0f;");

		// Usamos Platform.runLater para centrar los elementos una vez que la UI esté
		// cargada
		Platform.runLater(() -> {
			double actualWidth = pane.getWidth();
			double actualHeight = pane.getHeight();
			double ySeparator1 = actualHeight / 3;
			double ySeparator2 = (actualHeight / 3) * 2;
			separator1.setLayoutY(ySeparator1);
			separator2.setLayoutY(ySeparator2);
			userImage.setLayoutX((actualWidth - userImage.getBoundsInParent().getWidth()) / 2);
			myLoansImage.setLayoutX((actualWidth - myLoansImage.getBoundsInParent().getWidth()) / 2);
			requestConsultImage.setLayoutX((actualWidth - requestConsultImage.getBoundsInParent().getWidth()) / 2);
			editUser.setLayoutX((actualWidth - editUser.getWidth()) / 2);
			myLoans.setLayoutX((actualWidth - myLoans.getBoundsInParent().getWidth()) / 2);
			requestConsult.setLayoutX((actualWidth - requestConsult.getBoundsInParent().getWidth()) / 2);

			// Centrar la imagen en el primer tercio
			userImage.setLayoutY((ySeparator1 - userImage.getFitHeight()) / 2);
			editUser.setLayoutY(userImage.getLayoutY() + userImage.getFitHeight());
			myLoansImage.setLayoutY(((ySeparator1 + ySeparator2) - myLoansImage.getFitHeight()) / 2);
			requestConsultImage.setLayoutY(((ySeparator2 + actualHeight) - requestConsultImage.getFitHeight()) / 2);
			myLoans.setLayoutY(myLoansImage.getLayoutY() + myLoansImage.getFitHeight() + 10);
			requestConsult.setLayoutY(requestConsultImage.getLayoutY() + requestConsultImage.getFitHeight() + 10);

		});

		// Listener para cuando cambie el ancho del pane (por redimensionamiento de la
		// ventana, etc.)
		pane.widthProperty().addListener((obs, oldVal, newVal) -> {
			userImage.setLayoutX((newVal.doubleValue() - userImage.getFitWidth()) / 2);
			editUser.setLayoutX((newVal.doubleValue() - editUser.getWidth()) / 2);
			myLoansImage.setLayoutX((newVal.doubleValue() - myLoansImage.getFitWidth()) / 2);
			requestConsultImage.setLayoutX((newVal.doubleValue() - requestConsultImage.getFitWidth()) / 2);
			myLoans.setLayoutX((newVal.doubleValue() - myLoans.getBoundsInParent().getWidth()) / 2);
			requestConsult.setLayoutX((newVal.doubleValue() - requestConsult.getBoundsInParent().getWidth()) / 2);
			separator1.setPrefWidth(newVal.doubleValue());
			separator2.setPrefWidth(newVal.doubleValue());
		});

		pane.heightProperty().addListener((obs, oldVal, newVal) -> {
			double ySeparator1 = newVal.doubleValue() / 3;
			double ySeparator2 = ySeparator1 * 2;

			separator1.setLayoutY(ySeparator1);
			separator2.setLayoutY(ySeparator2);

			// Centrar verticalmente la imagen en el primer tercio
			userImage.setLayoutY((ySeparator1 - userImage.getFitHeight()) / 2);
			editUser.setLayoutY(userImage.getLayoutY() + userImage.getFitHeight());

			myLoansImage.setLayoutY(((ySeparator1 + ySeparator2) - myLoansImage.getFitHeight()) / 2);
			myLoans.setLayoutY(myLoansImage.getLayoutY() + myLoansImage.getFitHeight() + 10);

			requestConsultImage
					.setLayoutY(((ySeparator2 + newVal.doubleValue()) - requestConsultImage.getFitHeight()) / 2);
			requestConsult.setLayoutY(requestConsultImage.getLayoutY() + requestConsultImage.getFitHeight() + 10);

			closeSession.setLayoutY(newVal.doubleValue() - closeSession.getFitHeight() - 15);
		});
	}

	@FXML
	void handleEditUser() {
		Main.datoGlobal = editUser;
		Main.pane = pane;
		ViewUtils.cargarGrid("/views/MyAccount.fxml", rootPane);

	}
	
    @FXML
    void handleMyLoans(MouseEvent event) {
    	ViewUtils.cargarGrid("/views/MyLoans.fxml", rootPane);
    }
    
    @FXML
    void handleRequestConsultation() {
    	ViewUtils.cargarGrid("/views/RequestConsultation.fxml", rootPane);
    }
    
    @FXML
    void handleLogOut() {
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        ViewUtils.loadView("/views/Login.fxml");
    }
}
