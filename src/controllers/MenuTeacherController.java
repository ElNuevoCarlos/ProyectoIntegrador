package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import data.DataBase;
import data.SessionManager;
import data.UserDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Loan;
import model.User;


public class MenuTeacherController {
	@FXML
	private BorderPane rootPane;
	@FXML
	private Pane pane;
	@FXML
	private GridPane grip;
	@FXML
	private ImageView userImage, myLoansImage, requestConsultImage, closeSession;
	@FXML
	private Hyperlink editUser;
	@FXML
	private Label nameText, numIdentificationText, tiText, emailText, phoneText, roleText, pro_depText, password1Text,
			password2Text, title;
	@FXML
	private Text myLoans, requestConsult;
	@FXML
	private Separator separator1, separator2;
	@FXML
	private TextField name, numIdentification, email, phone, pro_dep, password1, password2;
	@FXML
	private ComboBox<String> ti, role;
	@FXML
	private Button save;
	
	private Connection database = DataBase.getInstance().getConnection();
	private UserDAO userDao = new UserDAO(database);
	private SessionManager sessionManager = SessionManager.getInstance();
	private ArrayList<String> otherData;

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
		grip.setVisible(true);
		ObservableList<String> itemsTi = FXCollections.observableArrayList("CC", "CE");
		ti.setItems(itemsTi);
		ObservableList<String> itemsRole = FXCollections.observableArrayList("DOCENTE", "ADMINISTRATIVO");
		role.setItems(itemsRole);
		otherData = userDao.otherData(sessionManager.getEmail());
//		nameText.setVisible(true);
//		numIdentificationText.setVisible(true);
//		tiText.setVisible(true);
//		emailText.setVisible(true);
//		phoneText.setVisible(true);
//		roleText.setVisible(true);
//		pro_depText.setVisible(true);
//		password1Text.setVisible(true);
//		password2Text.setVisible(true);
//		title.setVisible(true);
//
//		name.setVisible(true);
//		numIdentification.setVisible(true);
//		ti.setVisible(true);
//		email.setVisible(true);
//		phone.setVisible(true);
//		role.setVisible(true);
//		pro_dep.setVisible(true);
//		password1.setVisible(true);
//		password2.setVisible(true);
//
//		save.setVisible(true);
		name.setText(sessionManager.getName());
		numIdentification.setText(otherData.get(0));
		ti.setValue(otherData.get(1));
		email.setText(sessionManager.getEmail());
		phone.setText(otherData.get(4));
		role.setValue(sessionManager.getRole());
		password1.setText(otherData.get(2));
		password2.setText(otherData.get(2));
		pro_dep.setText(otherData.get(3));

	}

	@FXML
	void handleSave() {
		String Name = name.getText().trim();
		String TI = ti.getValue() != null ? ti.getValue().trim() : "";
		String NumIdentification = numIdentification.getText().trim();
		String Pro_dep = pro_dep.getText().trim();
		String Phone = phone.getText().trim();
		String Email = email.getText().trim();
		String Role = role.getValue() != null ? role.getValue().trim() : "";
		String Password1 = password1.getText().trim();
		String Password2 = password2.getText().trim();

		if (Name.isEmpty() || TI.isEmpty() || NumIdentification.isEmpty() || Pro_dep.isEmpty() || Phone.isEmpty()
				|| Email.isEmpty() || Role.isEmpty() || Password1.isEmpty() || Password2.isEmpty()) {

			showAlert("Error", "Campos vacíos", "Por favor, complete todos los campos.");
			return;
		}
		String regexEmail = "^[a-zA-Z0-9._%+-]+@udi\\.edu\\.co$";
		if (!Email.matches(regexEmail)) {
			showAlert("Error", "Correo inválido", "El correo debe tener el formato usuario@udi.edu.co");
			return;
		}
		if (!NumIdentification.matches("\\d{6,10}")) {
			showAlert("Error", "Número inválido", "Debe ingresar entre 6 y 10 dígitos numéricos.");
			return;
		}
		if (!Phone.matches("\\d{10}")) {
			showAlert("Error", "Número inválido", "Debe ingresar exactamente 10 dígitos numéricos.");
			return;
		}
		if (Password1.equals(Password2)) {
			if (!Password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$")) {
				showAlert("Formato inválido", "Contraseña insegura",
						"La contraseña debe tener:\n" + "- Entre 8 y 20 caracteres\n"
								+ "- Al menos una letra mayúscula\n" + "- Al menos una letra minúscula\n"
								+ "- Al menos un número\n" + "- Al menos un carácter especial (@#$%^&+=!)");
				return;
			} else {
				ArrayList<String> modifiedData = new ArrayList<>();
				if (!Name.equals(sessionManager.getName())) {
					modifiedData.add(nameText.getText());
				}
				if (!TI.equals(otherData.get(1))) {
					modifiedData.add(tiText.getText());
				}
				if (!NumIdentification.equals(otherData.get(0))) {
					modifiedData.add(numIdentificationText.getText());
				}
				if (!Pro_dep.equals(otherData.get(3))) {
					modifiedData.add(pro_depText.getText());
				}
				if (!Phone.equals(otherData.get(4))) {
					modifiedData.add(phoneText.getText());
				}
				if (!Email.equals(sessionManager.getEmail())) {
					modifiedData.add(emailText.getText());
				}
				if (!Role.equals(sessionManager.getRole())) {
					modifiedData.add(roleText.getText());
				}
				if (!Password1.equals(otherData.get(2))) {
					modifiedData.add(password1Text.getText());
				}
				
				if (!modifiedData.isEmpty()) {
					User newUser = new User(Name, NumIdentification, TI, Email, Pro_dep, Phone, "ACTIVA", Role, Password1, sessionManager.getId());
					sessionManager.setUser(sessionManager.getId(), Name, Role, Email);
					if (showConfirmation("Confirmación", "¿Desea actualizar " + modifiedData + "?")) {
						userDao.update(newUser); 
						showAlert("Actualizado", "Información actualizada",
								"Los datos "+ modifiedData + " actualizados exitosamente.");
						modifiedData.clear();
						otherData.set(0, NumIdentification);
						otherData.set(1, TI);
						otherData.set(2, Password1);
						otherData.set(3, Pro_dep);
						otherData.set(4, Phone);
						String[] partesUptate = Name.split("\\s+");
						editUser.setText(partesUptate[0] + " " + partesUptate[1] + "\n" + Role);
						Platform.runLater(() -> {
						    editUser.setLayoutX((pane.getWidth() - editUser.getWidth()) / 2);
						});
					} else {
						showAlert("Cancelado", "Actualización cancelada", "No se realizaron cambios");
					}
				} else {
				    showAlert("Sin cambios", "Ningún dato fue modificado",
				              "No se detectaron cambios en la información. Verifique e intente de nuevo si desea actualizar algún dato.");
				    return;
				}
			}
		} else {
			showAlert("Error", "Contraseñas no coinciden",
					"Las contraseñas ingresadas no son iguales. Por favor, verifíquelas.");
			return;
		}
	}

	private void showAlert(String title, String head, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(head);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private boolean showConfirmation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
		return result == ButtonType.OK;
	}
	
    private void cargarGrid(String archivoFXML) {
        try {
            GridPane nuevoGrid = FXMLLoader.load(getClass().getResource(archivoFXML));
            rootPane.setCenter(nuevoGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleMyLoans(MouseEvent event) {
        cargarGrid("/views/MyLoans.fxml");
    }
}
