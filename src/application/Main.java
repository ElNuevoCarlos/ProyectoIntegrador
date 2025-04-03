package application;

import java.sql.SQLException;

import data.UserDataManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	private UserDataManager userManager = UserDataManager.getInstance();
    private static BorderPane rootLayout;
    @SuppressWarnings("exports")
	@Override
    public void start(Stage primaryStage) throws SQLException {
    	userManager.loadData();
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            rootLayout = root.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            //primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("exports")
	public static void loadView(String fxmlFile, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Scene newScene = new Scene(newRoot);
            stage.setScene(newScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}

