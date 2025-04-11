package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
	private static DataBase instance;
	private Connection connection;
	//private final String host = "192.168.254.215";
	private final String host = "localhost";
	private final String service = "xe";
	//private final String service = "orcl";
	private final String port = "1521";

	private DataBase() {
		try {
			// Contraseña pc Yesid xmbW4xXdX87Ts0
			// Contraseña pc Carlos TECHLEND
			connection = DriverManager.getConnection(getConnectionString(), "TECHLEND", "TECHLEND");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error connecting to the database.");
		}
	}
    
    public static DataBase getInstance() {
        if (instance == null) instance = new DataBase();
        return instance;
    }
    
    public String getConnectionString() {
        return String.format("jdbc:oracle:thin:@//%s:%s/%s", this.host, this.port, this.service);
    }
	public Connection getConnection() {
		return connection;
	}
}