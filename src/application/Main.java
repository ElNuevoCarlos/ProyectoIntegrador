package application;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	public static Object datoGlobal;
	public static Pane pane;
	public static BorderPane rootLayout;

    public void start(Stage primaryStage) throws SQLException {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            rootLayout = root.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
    		Image icon = new Image(getClass().getResourceAsStream("/img/teacher.png"));
    		primaryStage.getIcons().add(icon);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void loadView(String fxmlFile) {
		Stage secondStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            secondStage.setScene(scene);
    		Image icon = new Image(Main.class.getResourceAsStream("/img/teacher.png"));
    		secondStage.getIcons().add(icon);
            secondStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("exports")
	public static void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // Lo puse aquí para el boton de añadir docentes, la razón es que al poner campos invalidos y cerrar esa venta, también cerraba la ventana
    // en la que se estaban llenando los datos, y con este bloque lo que hago es especificar el Stage que quiero cerrar
    
    @SuppressWarnings("exports")
    public static void AlertWindowStage(Window owner, String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.initModality(Modality.WINDOW_MODAL); 
        if (owner != null) {
            alert.initOwner(owner); 
        }                
        alert.showAndWait();
    }
    public static void cargarGrid(String archivoFXML, BorderPane rootPane) {
        try {
            GridPane nuevoGrid = FXMLLoader.load(Main.class.getResource(archivoFXML));
            rootPane.setCenter(nuevoGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean showConfirmation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
		return result == ButtonType.OK;
	}
    
    @SuppressWarnings("exports")
    public static void AlertWindow(String title, String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

