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
    	String queryUsuario = "INSERT INTO USUARIO "
    			+ "(ID, "
    			+ "NOMBRE_COMPLETO, "
    			+ "NUMERO_IDENTIFICACION, "
    			+ "TIPO_IDENTIFICACION, "
    			+ "CORREO_INSTITUCIONAL, "
    			+ "PROGRAMA_DEPARTAMENTO, "
    			+ "TELEFONO, "
    			+ "ESTADO, "
    			+ "ROL, "
    			+ "PASSWORD) "+
    						  "VALUES (SEQ_PERSONA_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    	try (PreparedStatement pstmt = connection.prepareStatement(queryUsuario)) {
				pstmt.setString(1, user.getNombre_completo());
	            pstmt.setString(2, user.getNumero_identificacion());
	            pstmt.setString(3, user.getTipo_identificacion());
	            pstmt.setString(4, user.getCorreo_institucional());
	            pstmt.setString(5, user.getPrograma_departamento());
	            pstmt.setString(6, user.getTelefono());
	            pstmt.setString(7, user.getEstado());
	            pstmt.setString(8, user.getRol());
	            pstmt.setString(9, user.getPassword());
	
	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("User inserted successfully.");
	            }
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
    	
    }
    // Aa1234567# CONTRASEÑA CPINTO5@UDI.EDU.CO
	public String verifyUser(String email) {
		String name = null;
        String query = "SELECT NOMBRE_COMPLETO FROM USUARIO WHERE CORREO_INSTITUCIONAL = ?";       
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
        	pstmt.setString(1, email);
        	ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("NOMBRE_COMPLETO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name; 
	}
	
	public String[] verifyPassword(String email, String password) {
		String verification = null;
		String role = null;
		String query = "SELECT ROL, CASE WHEN PASSWORD = ? THEN 'Y' ELSE 'N'  END AS VERIFICATION FROM CUENTA WHERE CORREO_INSTITUCIONAL = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, password);
			pstmt.setString(2, email);
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
	
	public boolean updatePassword(String email, String newPassword) {
		String query = "UPDATE USUARIO SET PASSWORD = ? WHERE CORREO_INSTITUCIONAL = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, newPassword);
			pstmt.setString(2, email);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0; 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public ArrayList<User> fetch() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM USUARIO";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
        	
            while (rs.next()) {
                String nombre_completo = rs.getString("NOMBRE_COMPLETO");
                String numero_identificacion = rs.getString("NUMERO_IDENTIFICACION");
                String tipo_identificacion = rs.getString("TIPO_IDENTIFICACION");
                String correo_institucional = rs.getString("CORREO_INSTITUCIONAL");
                String programa_departamento = rs.getString("PROGRAMA_DEPARTAMENTO");
                String telefono = rs.getString("TELEFONO");
                String estado = rs.getString("ESTADO");
                String rol = rs.getString("ROL");
                String password = rs.getString("PASSWORD");
                String id = rs.getString("ID");
                
                User user = new User(nombre_completo, 
                		numero_identificacion, 
                		tipo_identificacion, 
                		correo_institucional,
                		programa_departamento,
                		telefono,
                		estado,
                		rol,
                		password,
                		id);
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
