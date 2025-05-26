package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Sanction;

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
        		+", s.FECHA_FIN, s.MONTO, s.ESTADO, s.ID_USUARIO, s.ID_PRESTAMO"
                + " FROM SANCION s JOIN USUARIO u ON s.ID_USUARIO = u.ID WHERE u.ID = ?";
 
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
                Long idLoanHall = rs.getLong(8);
                Long idLoanDevice = rs.getLong(9);
                
                Sanction sanction = new Sanction(
                		id, 
                		typeSanction, 
                		descripcion,
                		state,
                		sanctionDate, 
                		endDate, 
                		amount, 
                		idLoanHall,
                		idLoanDevice
                		);
                sanctions.add(sanction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sanctions;
	}

	@Override
	public ArrayList<Sanction> fetch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Sanction entity) {
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


    

