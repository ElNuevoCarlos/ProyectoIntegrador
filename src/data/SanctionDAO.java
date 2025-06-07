package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import model.Sanction;
import model.SanctionInfo;
import utils.ViewUtils;

public class SanctionDAO {
    private Connection connection;

    public SanctionDAO(Connection connection) {
        this.connection = connection;
    }
    
	public void save(Sanction entity) {
    	String sql = "{ call TECHLEND.saveSancion(?, ?, ?, ?, ?) }";

	    	try (CallableStatement stmt = connection.prepareCall(sql)) {
	    		stmt.setString(1, entity.getTypeSanction());
	    		stmt.setString(2, entity.getDescription());
	    		stmt.setDate(3, Date.valueOf(entity.getEndDate()));
	    		stmt.setLong(4, entity.getAmount());
	    		stmt.setLong(5, entity.getIdLoanDevice());
	
	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	            	ViewUtils.AlertWindow(
	            		    "Éxito", 
	            		    "Sanción creada", 
	            		    "La sanción ha sido creada con éxito.", 
	            		    AlertType.INFORMATION
	            		);
	            }
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public ArrayList<Sanction> fetchUser(String userId) {
        ArrayList<Sanction> sanctions = new ArrayList<>();
        String query = "SELECT s.ID, s.TIPO_SANCION, s.DESCRIPCION,"
        		+" s.FECHA_FIN, s.MONTO, s.ESTADO, s.ID_PRESTAMO"
                + " FROM SANCION s JOIN PRESTAMO p ON s.ID_PRESTAMO = p.ID JOIN USUARIO u ON p.ID_USUARIO = u.ID"
                + " WHERE u.ID = ?";
 
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Long id = rs.getLong(1);
                String typeSanction = rs.getString(2);
                String descripcion = rs.getString(3);
                LocalDate endDate = rs.getDate(4).toLocalDate();
                int amount = rs.getInt(5);
                String state = rs.getString(6);  
                Long idLoanDevice = rs.getLong(7);
                
                Sanction sanction = new Sanction(
                		id, typeSanction, descripcion,
                		endDate, amount, 
                		state, idLoanDevice);
                
                sanctions.add(sanction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sanctions;
	}

	public ArrayList<SanctionInfo> fetch(String more) {
        ArrayList<SanctionInfo> sanctions = new ArrayList<>();
        String query = "SELECT S.TIPO_SANCION, S.DESCRIPCION, S.ESTADO, P.FECHA, S.FECHA_FIN, S.MONTO, U.CORREO_INSTITUCIONAL, S.ID"
                + " FROM SANCION S JOIN PRESTAMO P ON S.ID_PRESTAMO = P.ID"
                + " JOIN USUARIO U ON P.ID_USUARIO = U.ID"+more;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
        	
            while (rs.next()) {
                String typeSanction = rs.getString(1);
                String descripcion = rs.getString(2);
                String state = rs.getString(3);  
                LocalDate dateLoan = rs.getDate(4).toLocalDate();;
                LocalDate endDate = rs.getDate(5).toLocalDate();;
                int amount = rs.getInt(6);
                String emailClient = rs.getString(7);
                Long idLoan = rs.getLong(8);
                
                SanctionInfo sanctionInfo = new SanctionInfo(typeSanction, descripcion, state, dateLoan, endDate, amount, emailClient, idLoan);
                
                sanctions.add(sanctionInfo);
           
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sanctions;
	}
	public boolean update(SanctionInfo entity) {
		String sql = "{ call TECHLEND.updateSancion(?, ?, ?, ?, ?, ?) }";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.setLong(1, entity.getidLoan());
			stmt.setString(2, entity.getTypeSanction());
			stmt.setString(3, entity.getDescription());
			stmt.setDate(4, Date.valueOf(entity.getEndDate()));
			stmt.setInt(5, entity.getAmount());
			stmt.setString(6, entity.getState());
			stmt.executeUpdate();
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, "No se pudo actualizar", "Verifique los siguientes aspectos\n"
					+ "- Qué ese dato no esté repetido.\n"
					+ "- Verifique que escribió todo correctamente.", AlertType.ERROR);
			return false;
		}	
		return true;
	}

	public boolean delete(Long entity) {
		String sql = "DELETE SANCION WHERE ID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, entity);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}


    

