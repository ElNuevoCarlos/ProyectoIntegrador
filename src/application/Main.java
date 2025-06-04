package application;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	public static Object datoGlobal;
	public static boolean datoGlobalTwo;
	public static boolean isSuperManager;
	public static Pane pane;
	public static BorderPane rootLayout;

    public void start(Stage primaryStage) throws SQLException {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            rootLayout = root.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
    		Image icon = new Image(getClass().getResourceAsStream("/img/logo.png"));
    		primaryStage.getIcons().add(icon);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

