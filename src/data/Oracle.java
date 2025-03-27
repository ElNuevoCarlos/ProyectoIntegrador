package data;

import java.sql.*;

public class Oracle {
	// SERVIDOR DE LA UDI
	//private final String stringConexion = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";
	private final String stringConexion = "jdbc:oracle:thin:@localhost:1521:xe";

	public Connection getConexionTeacher() {
		Connection conexion = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");

			conexion = DriverManager.getConnection(stringConexion, "zoologico069","zoologico069");
		} catch (ClassNotFoundException e) {
			System.out.println("No se encuentra el driver");
		}
		catch(SQLException e){
			//System.out.println(e);
			System.out.println("Error en la conexi�n");
		}
		return conexion;
	}
	public Connection getConexionAdministrative() {
		Connection conexion = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");

			conexion = DriverManager.getConnection(stringConexion, "zoologico069","zoologico069");
		} catch (ClassNotFoundException e) {
			System.out.println("No se encuentra el driver");
		}
		catch(SQLException e){
			//System.out.println(e);
			System.out.println("Error en la conexi�n");
		}
		return conexion;
	}
}
