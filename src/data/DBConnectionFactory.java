package data;

public class DBConnectionFactory {
	public static DBConnection getConnectionByRole(String role) {
		switch (role.toLowerCase()) {
	    case "superencargado":
	        return SuperManagerConnection.getInstance();
	    case "encargado":
	        return ManagerConnection.getInstance();
	    case "docente":
	    case "administrativo":
	        return TeachaerAdminConnection.getInstance();
	    default:
	        throw new IllegalArgumentException("Rol no válido: " + role);
	}
}
}
