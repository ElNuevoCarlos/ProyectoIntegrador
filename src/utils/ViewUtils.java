package utils;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ViewUtils {
	
    public static void loadView(String fxmlFile) {
        Stage secondStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(ViewUtils.class.getResource(fxmlFile));
            BorderPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            secondStage.setScene(scene);
            Image icon = new Image(ViewUtils.class.getResourceAsStream("/img/teacher.png"));
            secondStage.getIcons().add(icon);
            secondStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    public static void AlertWindow(String title, String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
