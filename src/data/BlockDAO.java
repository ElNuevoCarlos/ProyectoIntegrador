package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Block;

public class BlockDAO {
    private Connection connection;

    public BlockDAO(Connection connection) {
        this.connection = connection;
    }
    
	public ArrayList<Block> findAvailableBlocks(LocalDate date, String hallDevice) {
		ArrayList<Block> blocks = new ArrayList<>();
		String query = "SELECT b.ID, b.HORA_INICIO, b.HORA_FIN"
				+ " FROM BLOQUE b"
				+ " WHERE b.ID NOT IN ("
				+ " SELECT pb.ID_BLOQUE"
				+ " FROM TECHLEND.PRESTAMO_BLOQUE pb"
				+ " JOIN TECHLEND.PRESTAMO p ON pb.ID_PRESTAMO = p.ID"
				+ " WHERE TRUNC(p.FECHA) = ? AND " + hallDevice + ")";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        	pstmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Long id = rs.getLong(1);
                Time startTime = rs.getTime(2);
                Time endTime = rs.getTime(3);
                Block block = new Block(id, startTime, endTime);
                blocks.add(block);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blocks;
	}
	
	
	public ArrayList<Block> findBlocksByLoanId(long idLoan) {
		ArrayList<Block> blocks = new ArrayList<>();
		String query = "SELECT b.ID, b.HORA_INICIO, b.HORA_FIN FROM TECHLEND.BLOQUE b"
				+ " WHERE b.ID IN (SELECT pb.ID_BLOQUE FROM TECHLEND.PRESTAMO_BLOQUE pb WHERE pb.ID_PRESTAMO = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, idLoan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Long id = rs.getLong(1);
                Time startTime = rs.getTime(2);
                Time endTime = rs.getTime(3);
                Block block = new Block(id, startTime, endTime);
                blocks.add(block);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blocks;
	}
	
	public void deleteBlock(long idLoan, long idBlock) {
	    String sql = "DELETE FROM TECHLEND.PRESTAMO_BLOQUE WHERE ID_PRESTAMO = ? AND ID_BLOQUE = ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setLong(1, idLoan);
	        stmt.setLong(2, idBlock);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	    }
	}
	
	public void saveBlock(long idLoan, long idBlock) {
	    String sql = "INSERT INTO TECHLEND.PRESTAMO_BLOQUE (ID_PRESTAMO, ID_BLOQUE) VALUES (?, ?)";

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setLong(1, idLoan);
	        stmt.setLong(2, idBlock);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


}
