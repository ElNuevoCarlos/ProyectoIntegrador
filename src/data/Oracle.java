package data;

import java.sql.*;

public class Oracle {
	//private final String host = "192.168.254.215";
	private final String host = "localhost";
	private final String service = "xe";
	//private final String service = "orcl";
	private final String port = "1521";


	public Connection getConexionTeacher() {
		Connection conexion = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conexion = DriverManager.getConnection(getConnection(), "TECHLEND","xmbW4xXdX87Ts0");
		} catch (ClassNotFoundException e) {
			System.out.println("No se encuentra el driver");
		}
		catch(SQLException e){
			//System.out.println(e);
			System.out.println("Error en la conexi�n");
		}
		return conexion;
	}

	public String getConnection() {
		return "jdbc:oracle:thin:@"+this.host+":"+this.port+":"+this.service;
	}
}
