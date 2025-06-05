package data;

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
    	String querySancion = "INSERT INTO SANCION"
    			+ " (ID, TIPO_SANCION, DESCRIPCION, FECHA_FIN, MONTO, ESTADO, ID_PRESTAMO)"
    			+ " VALUES (SEQ_SANCION_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";

	    	try (PreparedStatement pstmt = connection.prepareStatement(querySancion)) {
				pstmt.setString(1, entity.getTypeSanction());
	            pstmt.setString(2, entity.getDescription());
	            pstmt.setDate(3, Date.valueOf(entity.getEndDate()));
	            pstmt.setLong(4, entity.getAmount());
	            pstmt.setString(5, entity.getState());
	            pstmt.setLong(6, entity.getIdLoanDevice());
	
	            pstmt.executeUpdate();
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
		String query = "UPDATE SANCION "
				+ "SET TIPO_SANCION = ?, DESCRIPCION = ?, MONTO = ?, ESTADO = ?"
				+ "WHERE ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, entity.getTypeSanction());
			pstmt.setString(2, entity.getDescription());
			pstmt.setInt(3, entity.getAmount());
			pstmt.setString(4, entity.getState());
			pstmt.setLong(5, entity.getidLoan());
			pstmt.executeUpdate();
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


    

