package application;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    
    public static void cargarGrid(String archivoFXML, BorderPane rootPane) {
        try {
            GridPane nuevoGrid = FXMLLoader.load(Main.class.getResource(archivoFXML));
            rootPane.setCenter(nuevoGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

