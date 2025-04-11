package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.User;

public class UserDAO implements CRUD_operation<User, String>{
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) {
        // Definimos las queries para insertar en las tablas PERSONA y CUENTA
        String queryPersona = "INSERT INTO PERSONA (ID, NOMBRE_COMPLETO, TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION, CORREO_INSTITUCIONAL, TELEFONO, PROGRAMA_DEPARTAMENTO) " +
                              "VALUES (SEQ_PERSONA_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        String queryCuenta = "INSERT INTO CUENTA (ID, USUARIO, PASSWORD, ROL, ID_PERSONA) VALUES (SEQ_CUENTA_ID.NEXTVAL, ?, ?, ?, ?)";
        
        try {
            // Comenzamos una transacción
            connection.setAutoCommit(false); // Desactivamos el autocommit

            // Insertamos la persona
            try (PreparedStatement pstmt = connection.prepareStatement(queryPersona, new String[] {"ID"})) {
                pstmt.setString(1, user.getFullName());
                pstmt.setString(2, user.getTI());
                pstmt.setString(3, user.getNumIdentification());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getPhone());
                pstmt.setString(6, user.getPro_dep());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User inserted successfully.");
                }

                // Recuperamos el ID generado para la persona
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idPersona = rs.getInt(1); // ID_PERSONA generado
                        // Ahora insertamos la cuenta, utilizando el ID_PERSONA
                        try (PreparedStatement pstmtCuenta = connection.prepareStatement(queryCuenta)) {
                            pstmtCuenta.setString(1, user.getUsername());
                            pstmtCuenta.setString(2, user.getPassword());
                            pstmtCuenta.setString(3, user.getRole());
                            pstmtCuenta.setInt(4, idPersona); // Usamos el ID de la persona
                            pstmtCuenta.executeUpdate();        
                            System.out.println("Account inserted successfully.");
                        }
                    }
                }
                
                // Si todo fue exitoso, confirmamos la transacción
                connection.commit();

            } catch (SQLException e) {
                // Si ocurre algún error en las inserciones, revertimos todo
                connection.rollback();
                System.out.println("Error occurred, transaction rolled back.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            // Si ocurre un error en la configuración de la transacción, revertimos y mostramos el error
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Error with transaction setup, rolled back.");
            e.printStackTrace();
        } finally {
            try {
                // Restauramos el autoCommit a su valor original
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
	public String verifyUser(String user) {
		String name = null;
        String query = "SELECT p.NOMBRE_COMPLETO FROM PERSONA p, CUENTA c WHERE c.ID_PERSONA = p.ID AND c.USUARIO = ?";       
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
        	pstmt.setString(1, user);
        	ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("NOMBRE_COMPLETO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name; 
	}
	
	public String[] verifyPassword(String user, String password) {
		String verification = null;
		String role = null;
		String query = "SELECT ROL, CASE WHEN PASSWORD = ? THEN 'Y' ELSE 'N'  END AS VERIFICATION FROM CUENTA WHERE USUARIO = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, password);
			pstmt.setString(2, user);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				role = rs.getString("ROL");
				verification = rs.getString("VERIFICATION");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new String[] {verification, role};
	}
	
	@Override
	public ArrayList<User> fetch() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM PERSONA";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String fullName = rs.getString("fullName");
                String TI = rs.getString("TI");
                String numIdentification = rs.getString("numIdentification");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String pro_dep = rs.getString("pro_dep");
                String username = rs.getString("username");
                String role = rs.getString("role");
                String password = rs.getString("password");
                
                User user = new User(fullName, TI, numIdentification, email, phone, pro_dep, username, role, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
	}

	@Override
	public void update(User entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean authenticate(String id) {
		// TODO Auto-generated method stub
		return false;
	}
}
