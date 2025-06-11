package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
	private static DataBase instance;
	private Connection connection;
	private final String host = "192.168.254.215";
	private final String service = "orcl";
	private final String port = "1521";

	private DataBase() {
		try {
			connection = DriverManager.getConnection(getConnectionString(), "TECHLEND", "xmbW4xXdX87Ts0");
		} catch (SQLException e) {
			throw new RuntimeException("Error connecting to the database."+e.getMessage());
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