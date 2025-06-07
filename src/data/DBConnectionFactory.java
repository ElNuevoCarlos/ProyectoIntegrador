package data;

public class DBConnectionFactory {
	public static DBConnection getConnectionByRole(String role) {
		switch (role.toLowerCase()) {
		case "SUPERENCARGADO":
			return SuperManagerConnection.getInstance();
		case "ENCARGADO":
			return ManagerConnection.getInstance();
		case "DOCENTE":
			return TeachaerAdminConnection.getInstance();
		case "ADMINISTRATIVO":
			return TeachaerAdminConnection.getInstance();
		default:
		throw new IllegalArgumentException("Rol no válido: " + role);}}
}
