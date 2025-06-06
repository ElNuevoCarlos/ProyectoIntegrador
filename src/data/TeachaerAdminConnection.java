package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TeachaerAdminConnection implements DBConnection {
    private static TeachaerAdminConnection instance; //Singleton
    private Connection connection;
	private final String username="US_DOCENTE_ADMINISTRATIVO";
	private final String password="zsRui7Eg07uSPp";
	private final String host = "localhost";
	private final String service = "xe";
	private final String port = "1521";
//	private final String host = "192.168.254.215";

//	private final String service = "orcl";

    private TeachaerAdminConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static TeachaerAdminConnection getInstance() {
        if (instance == null) instance = new TeachaerAdminConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
	public String getConnectionString() {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
	}
}

