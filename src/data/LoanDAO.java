package data;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Loan;
import model.LoanTable;

public class LoanDAO implements CRUD_operation<Loan, String>{
    private Connection connection;

    public LoanDAO(Connection connection) {
        this.connection = connection;
    }


	public ArrayList<LoanTable> MyLoansView(Long idUser, Boolean type, StringBuilder second) {
        ArrayList<LoanTable> loansView = new ArrayList<>();
        String query;

        if (type) {
            query = "SELECT p.ID, s.NOMBRE, p.FECHA_INICIO, p.FECHA_FIN, u.EDIFICIO || ' - ' || u.PISO AS LOCALIZACION, p.ESTADO "
                  + "FROM PRESTAMO p JOIN SALA s ON p.ID_SALA = s.ID "
                  + "JOIN UBICACION u ON s.ID_UBICACION = u.ID "
                  + "WHERE p.ID_USUARIO = ?" + second;
        } else {
            query = "SELECT p.ID, e.NOMBRE, p.FECHA_INICIO, p.FECHA_FIN, e.TIPO_DISPOSITIVO, p.ESTADO "
                  + "FROM PRESTAMO p JOIN EQUIPO e ON p.ID_EQUIPO = e.ID "
                  + "WHERE p.ID_USUARIO = ?" + second;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(1);
                String name = rs.getString(2);
                Timestamp startTime = rs.getTimestamp(3);
                Timestamp endTime = rs.getTimestamp(4);
                String fifthCol = rs.getString(5); // LOCALIZACION o TIPO_DISPOSITIVO
                String state = rs.getString(6);

                LoanTable loanView = new LoanTable(id, name, startTime, endTime, fifthCol, state);
                loansView.add(loanView);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loansView;
    }

    
	@Override
	public void save(Loan entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Loan> fetch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Loan entity) {
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


    

