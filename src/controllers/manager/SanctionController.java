package controllers.manager;

import java.sql.Connection;
import java.util.Optional;

import data.DBConnectionFactory;
import data.SanctionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.SanctionInfo;
import model.UserSession;
import utils.ViewUtils;

public class SanctionController {
    @FXML private TextField prestamoField, fechaSancionField, tipoSancionField, estadoField;

    @FXML private TableView<SanctionInfo> tableSanctions;
    @FXML private TableColumn<SanctionInfo, String> tipo_sancion, descripcion, fecha_sancion, fecha_fin, monto, emailClient, state;
    
    private FilteredList<SanctionInfo> listaFiltrada;
    
	public UserSession userSession = UserSession.getInstance();
    public String userRol = userSession.getRole();
    private Connection connection = DBConnectionFactory.getConnectionByRole(userRol).getConnection();
    private SanctionDAO sanctionDao = new SanctionDAO(connection);

    @FXML public void initialize() {
		ObservableList<SanctionInfo> sanctions = FXCollections.observableArrayList();
		
		for (SanctionInfo sanctioninfo : sanctionDao.fetch("")) { 
			sanctions.add(sanctioninfo);
		}
		
		tipo_sancion.setCellValueFactory(new PropertyValueFactory<>("typeSanction"));
		descripcion.setCellValueFactory(new PropertyValueFactory<>("description"));
		fecha_sancion.setCellValueFactory(new PropertyValueFactory<>("dateLoan"));
		fecha_fin.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		monto.setCellValueFactory(new PropertyValueFactory<>("amount"));
		emailClient.setCellValueFactory(new PropertyValueFactory<>("emailClient"));
		state.setCellValueFactory(new PropertyValueFactory<>("state"));
        
	    listaFiltrada = new FilteredList<>(sanctions, p -> true);
	    
		tableSanctions.setItems(listaFiltrada);
		
		prestamoField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		fechaSancionField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		tipoSancionField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
		estadoField.textProperty().addListener((obs, oldVal, newVal) -> filtro());
    }

    
    private void filtro() {
        String prestamo = prestamoField.getText().toLowerCase();
        String fechaSancion = fechaSancionField.getText().toLowerCase();
        String tipoSancion = tipoSancionField.getText().toLowerCase();
        String estadoSancion = estadoField.getText().toLowerCase();

        listaFiltrada.setPredicate(sanction -> {
            String PrestamoStr = sanction.getEmailClient().toLowerCase();
            String TipoSancionStr = sanction.getTypeSanction().toLowerCase();
            String FechaSancion = sanction.getDateLoan().toString();
            String EstadoSancion = sanction.getState().toLowerCase();
            
            boolean bPrestamo = PrestamoStr.contains(prestamo);
            boolean bFechaSancion = FechaSancion.contains(fechaSancion);
            boolean bTipoSancion = TipoSancionStr.contains(tipoSancion);
            boolean bEstadoSancion = EstadoSancion.contains(estadoSancion);

            return bPrestamo && bFechaSancion && bTipoSancion && bEstadoSancion;
        });
    }
    
    private SanctionInfo selectSanction() {
    	SanctionInfo sanction = tableSanctions.getSelectionModel().getSelectedItem();
    	if (sanction == null) {
    		ViewUtils.AlertWindow(null, null, "Debe primero seleccionar una sanción", AlertType.ERROR);
    		return null;
    	}
    	return sanction;
    }
    @FXML private void actualizar() {
    	SanctionInfo sanctionInfo = selectSanction();
    	if (sanctionInfo != null) {
    		
	        Dialog<SanctionInfo> dialog = new Dialog<>();
	        
	        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
	        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
	        
	        GridPane grid = new GridPane();
	        
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(20, 20, 10, 10));
	
	        ComboBox<String> typeSanctionField = new ComboBox<>();
	        
		    ObservableList<String> itemsTypeSanction = FXCollections.observableArrayList(
			        "MAL ESTADO", "ENTREGA TARDE"
			    );
		    typeSanctionField.setPromptText("Tipo de Sanción");
		    typeSanctionField.setPrefWidth(155);
		    typeSanctionField.setItems(itemsTypeSanction);

	        TextField descriptionField = new TextField(sanctionInfo.getDescription());
	        TextField amountField = new TextField(String.valueOf(sanctionInfo.getAmount()));

	        ComboBox<String> stateField = new ComboBox<>();
		    ObservableList<String> itemsState = FXCollections.observableArrayList(
			        "ACTIVA", "CUMPLIDA", "CANCELADA"
			    );

		    stateField.setPromptText("Estado de Sanción");
		    stateField.setPrefWidth(155);
		    stateField.setItems(itemsState);
		   
	        grid.add(new Label("Descripción:"), 0, 1);
	        grid.add(descriptionField, 1, 1);
	        grid.add(new Label("Monto:"), 0, 3);
	        grid.add(amountField, 1, 3);
	        
	        HBox box = new HBox(5); 
	        box.getChildren().addAll(typeSanctionField, stateField);
	        
	        grid.add(box, 0, 4, 2, 2);
	        dialog.getDialogPane().setContent(grid);
	        
	        ButtonType saveButtonType = new ButtonType("Guardar", ButtonData.OK_DONE);
	        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
	        
	        dialog.setResultConverter(dialogButton -> {
	            if (dialogButton == saveButtonType) {
	            	String description = descriptionField.getText().trim();
	            	String typesanction = typeSanctionField.getValue() != null ? typeSanctionField.getValue() : sanctionInfo.getTypeSanction();
	            	String state = stateField.getValue() != null ? stateField.getValue() : sanctionInfo.getState();
	            	
	            	int amount;
	            	try {
	            	    amount = Integer.parseInt(amountField.getText().trim());
	            	} catch (NumberFormatException e) {
	            		ViewUtils.AlertWindow(null, "Valor no admitido", "Coloque un monto valido.", AlertType.ERROR);
	            		return null;
	            	}
	            	
	            	sanctionInfo.setDescription(description);
	            	sanctionInfo.setTypeSanction(typesanction);
	            	sanctionInfo.setState(state);
	            	sanctionInfo.setAmount(amount);
	                
	                return sanctionInfo;
	            }
	            return null;
	        });
	        
	        Optional<SanctionInfo> result = dialog.showAndWait();
	        result.ifPresent(updatedCourse -> {
	            if (sanctionDao.update(updatedCourse)) {
		            initialize();
		            ViewUtils.AlertWindow(null, null, "Sanción actualizada con éxito.", AlertType.INFORMATION);
	            }
	        });
    	}
    }
}
