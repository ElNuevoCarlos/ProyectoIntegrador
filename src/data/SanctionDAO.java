package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Sanction;
import model.User;

public class SanctionDAO implements CRUD_operation<Sanction, String>{
    private Connection connection;

    public SanctionDAO(Connection connection) {
        this.connection = connection;
    }
	@Override
	public void save(Sanction entity) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Sanction> fetchUser(String userId) {
        ArrayList<Sanction> sanctions = new ArrayList<>();
        String query = "SELECT s.ID, s.TIPO_SANCION, s.DESCRIPCION, s.FECHA_SANCION"
        		+", s.FECHA_FIN, s.MONTO, s.ESTADO, s.ID_PRESTAMO"
                + " FROM SANCION s JOIN PRESTAMO p ON s.ID_PRESTAMO = p.ID JOIN USUARIO u ON p.ID_USUARIO = u.ID"
                + " WHERE u.ID = ?";
 
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Long id = rs.getLong(1);
                String typeSanction = rs.getString(2);
                String descripcion = rs.getString(3);
                Date sanctionDate = rs.getDate(4);
                Date endDate = rs.getDate(5);
                int amount = rs.getInt(6);
                String state = rs.getString(7);  
                Long idLoanDevice = rs.getLong(8);
                
                Sanction sanction = new Sanction(
                		id, typeSanction, 
                		descripcion, sanctionDate, 
                		endDate, amount, 
                		state, idLoanDevice);
                
                sanctions.add(sanction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sanctions;
	}

	@Override
	public ArrayList<Sanction> fetch() {
        ArrayList<Sanction> sanctions = new ArrayList<>();
        String query = "SELECT ID, TIPO_SANCION, DESCRIPCION, FECHA_SANCION"
        		+", FECHA_FIN, MONTO, ESTADO, ID_PRESTAMO"
                + " FROM SANCION";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
        	
            while (rs.next()) {
            	Long id = rs.getLong(1);
                String typeSanction = rs.getString(2);
                String descripcion = rs.getString(3);
                Date sanctionDate = rs.getDate(4);
                Date endDate = rs.getDate(5);
                int amount = rs.getInt(6);
                String state = rs.getString(7);  
                Long idLoanDevice = rs.getLong(8);
                
                Sanction sanction = new Sanction(
                		id, typeSanction, 
                		descripcion, sanctionDate, 
                		endDate, amount, 
                		state, idLoanDevice);
                sanctions.add(sanction);
           
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sanctions;
	}

	@Override
	public boolean update(Sanction entity) {
		return false;
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


    

