package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import application.Main;
import utils.SecurityUtils;
import utils.ViewUtils;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.User;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }
    
    
    public void save(User user) {
        String sql = "{ call TECHLEND.saveUser(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(sql)) {
            cstmt.setString(1, user.getPassword());
            cstmt.setString(2, user.getRole());
            cstmt.setString(3, user.getInstitutionalEmail());
            cstmt.setString(4, user.getIdentificationType());
            cstmt.setString(5, user.getIdentificationNumber());
            cstmt.setString(6, user.getPhone());
            cstmt.setString(7, user.getProgramDepartment());
            cstmt.setString(8, user.getFullName());

            int rowsAffected = cstmt.executeUpdate();
            if (rowsAffected > 0) {
                ViewUtils.AlertWindow(
                	    "Éxito", 
                	    "Cuenta creada", 
                	    "La cuenta ha sido creada con éxito.", 
                	    AlertType.INFORMATION
                	);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    // Aa1234567# CONTRASEÑA CPINTO5@UDI.EDU.CO   Brayan1#
	public String verifyEmail(String email) {
	    String name = null;
	    String sql = "{ ? = call TECHLEND.verifyEmail(?) }";       

	    try (CallableStatement stmt = connection.prepareCall(sql)) {
	        stmt.registerOutParameter(1, Types.VARCHAR);
	        stmt.setString(2, email);
	        
	        stmt.execute();

	        name = stmt.getString(1);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return name; 
	}

	
	public String[] verifyPassword(String email, String password) {
	    String result = null;
	    String role = null;
	    long id = -1;
	    boolean verification = false; 

	    String sql = "{ ? = call TECHLEND.verifyPassword(?) }";
	    try (CallableStatement stmt = connection.prepareCall(sql)) {
	        stmt.registerOutParameter(1, Types.VARCHAR);
	        stmt.setString(2, email);
	        stmt.execute();

	        result = stmt.getString(1);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    if (result != null) {
	        try {
	            String[] parts = result.split(" ", 3);
	            role = parts[0];
	            id = Long.parseLong(parts[1]);
	            String decryptedPassword = SecurityUtils.decrypt(parts[2]);

	            if (password.equals(decryptedPassword)) {
	                verification = true;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            ViewUtils.AlertWindow("Error", "Descifrado fallido", "Ocurrió un error al descifrar la contraseña. Inténtalo de nuevo.", AlertType.ERROR);
	        }
	    }

	    return new String[] {String.valueOf(verification), role != null ? role : "", String.valueOf(id)             
	    };
	}

	
	
	public Long verifyId(Long id) {
		Long idUser = null;
        String sql = "{ ? = call TECHLEND.verifyId(?) }";       
        try (CallableStatement stmt = connection.prepareCall(sql)){
	        stmt.registerOutParameter(1, Types.VARCHAR);
	        stmt.setLong(2, id);
	        stmt.execute();

	        idUser = stmt.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idUser; 
	}
	
	
	public String otherData(String email) {
	    String sql = "{ ? = call TECHLEND.otherData(?) }";
	    String data = "";

	    try (CallableStatement stmt = connection.prepareCall(sql)) {
	        stmt.registerOutParameter(1, Types.VARCHAR);
	        stmt.setString(2, email);
	        stmt.execute();

	        data = stmt.getString(1);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return data;
	}
	
	public boolean updatePassword(String email, String newPassword) {
		String sql = "{ call TECHLEND.updatePassword(?, ?) }";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, newPassword);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
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
                long id = rs.getLong("ID");
                
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

	public boolean update(User user) {
		String sql = "{ call TECHLEND.updateUser(?, ?, ?, ?, ?, ?, ?, ?, ?) } ";
		try (CallableStatement pstmt = connection.prepareCall(sql)) {
			pstmt.setLong(1, user.getId());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.setString(4, user.getInstitutionalEmail());
			pstmt.setString(5, user.getIdentificationType());
			pstmt.setString(6, user.getIdentificationNumber());
			pstmt.setString(7, user.getPhone());
			pstmt.setString(8, user.getProgramDepartment());
			pstmt.setString(9, user.getFullName());
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, "No se pudo actualizar", "Verifique los siguientes aspectos\n"
					+ "- Qué ese dato no esté repetido.\n"
					+ "- Verifique que escribió todo correctamente.", AlertType.ERROR);
			return false;
		}	
	}  
    
	public void delete(String identificationNumber) {
		String sql = "{ call TECHLEND.deleteUser(?) }";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.setString(1, identificationNumber);
            stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
}
