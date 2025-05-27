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
    
	public ArrayList<Block> hallBlocks(LocalDate date, String hallDevice) {
		ArrayList<Block> blocks = new ArrayList<>();
		String query = "SELECT b.ID, b.HORA_INICIO, b.HORA_FIN\r\n"
				+ "FROM BLOQUE b\r\n"
				+ "WHERE b.ID NOT IN (\r\n"
				+ "    SELECT pb.ID_BLOQUE\r\n"
				+ "    FROM PRESTAMO_BLOQUE pb\r\n"
				+ "    JOIN PRESTAMO p ON pb.ID_PRESTAMO = p.ID\r\n"
				+ "    WHERE TRUNC(p.FECHA) = ? AND " + hallDevice + "\r\n"
				+ ")";
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
}
