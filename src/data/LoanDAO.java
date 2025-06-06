package data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import model.Block;
import model.Loan;
import model.LoanTable;
import model.Loans;
import utils.ViewUtils;

public class LoanDAO {
    private Connection connection;

    public LoanDAO(Connection connection) {
        this.connection = connection;
    }

	public ArrayList<LoanTable> MyLoansView(Long idUser, Boolean type, StringBuilder second) {
        ArrayList<LoanTable> loansView = new ArrayList<>();
        String query;
        
        if (type) {
            query = "SELECT p.ID, s.NOMBRE, p.FECHA, u.EDIFICIO || ' - ' || u.PISO AS LOCALIZACION, p.ESTADO, p.ESPECIFICACIONES, s.CAPACIDAD "
                  + "FROM PRESTAMO p JOIN SALA s ON p.ID_SALA = s.ID "
                  + "JOIN UBICACION u ON s.ID_UBICACION = u.ID "
                  + "WHERE p.ID_USUARIO = ?" + second;
        } else {
            query = "SELECT p.ID, e.NOMBRE, p.FECHA, e.TIPO_DISPOSITIVO, p.ESTADO, p.ESPECIFICACIONES, NULL "
                  + "FROM PRESTAMO p JOIN EQUIPO e ON p.ID_EQUIPO = e.ID "
                  + "WHERE p.ID_USUARIO = ?" + second;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(1);
                String name = rs.getString(2);
                Timestamp ts = rs.getTimestamp(3);
                String locationType = rs.getString(4);
                String state = rs.getString(5);
                String specs = rs.getString(6);
                String capacity = String.valueOf(rs.getInt(7));
                
                LocalDate date = ts.toLocalDateTime().toLocalDate();
                
                LoanTable loanView = new LoanTable(id, name, date,locationType, state, specs, capacity);
                loansView.add(loanView);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loansView;
    }
	
    
    public void save(Loan loan) {
        String queryLoan = "INSERT INTO PRESTAMO (ID, FECHA, ESPECIFICACIONES, ID_SALA, ID_USUARIO, ID_EQUIPO, ESTADO) " +
                              "VALUES (SEQ_PRESTAMO.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        String queryLoanBlock = "INSERT INTO PRESTAMO_BLOQUE (ID_PRESTAMO, ID_BLOQUE) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false); // Inicia la transacción

            Long idLoan = null;

            // Insertamos en PERSONA y obtenemos la clave generada
            try (PreparedStatement pstmtPersona = connection.prepareStatement(queryLoan,new String[] { "ID" })) {

                pstmtPersona.setDate(1, java.sql.Date.valueOf(loan.getDate()));
                pstmtPersona.setString(2, loan.getSpecs());
                if (loan.getIdHall() != null) {
                    pstmtPersona.setLong(3, loan.getIdHall());
                } else {
                    pstmtPersona.setNull(3, java.sql.Types.BIGINT);
                }
                pstmtPersona.setLong(4, loan.getIdUser());
                if (loan.getIdEquipment() != null) {
                    pstmtPersona.setLong(5, loan.getIdEquipment());
                } else {
                    pstmtPersona.setNull(5, java.sql.Types.BIGINT);
                }
                pstmtPersona.setString(6, loan.getState());

                int rows = pstmtPersona.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = pstmtPersona.getGeneratedKeys()) {
                        if (rs.next()) {
                        	idLoan = rs.getLong(1);
                        }
                    }
                }
            }

            if (idLoan != null) {
                try (PreparedStatement pstmtBlock = connection.prepareStatement(queryLoanBlock)) {
                    for (Block block : loan.getBlocks()) {
                        pstmtBlock.setLong(1, idLoan);
                        pstmtBlock.setLong(2, block.getId());
                        pstmtBlock.executeUpdate();
                    }
                }
            }

            connection.commit(); // Confirmamos la transacción
            System.out.println("Prestamo y bloques insertados correctamente.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Revertimos si hay error
                System.out.println("Error al insertar, se hizo rollback.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Restauramos autocommit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
	public ArrayList<Loans> fetchLoan(Long teacher, StringBuilder more) {
        ArrayList<Loans> loans = new ArrayList<>();
        String query = "SELECT p.ID, s.NOMBRE, us.CORREO_INSTITUCIONAL, p.FECHA, u.EDIFICIO || ' - ' || u.PISO AS LOCALIZACION,"
        		+ " p.ESTADO, p.ESPECIFICACIONES"
        		+ " FROM PRESTAMO p JOIN SALA s ON p.ID_SALA = s.ID "
        		+ " JOIN USUARIO us ON p.ID_USUARIO = us.ID\r\n"
        		+ " JOIN UBICACION u ON s.ID_UBICACION = u.ID";

        	if (teacher != null) {
        	    query += " WHERE p.ID_USUARIO = " + teacher;
        	}

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Long id = rs.getLong(1);
                String nombreSala = rs.getString(2);
                String correoUsuario = rs.getString(3);
                Timestamp dateTimestamp = rs.getTimestamp(4); 
                String location = rs.getString(5);
                String state = rs.getString(6); 
                String specs = rs.getString(7);
                
                LocalDate date = dateTimestamp.toLocalDateTime().toLocalDate();
                
                Loans loan = new Loans(id,
                		nombreSala,
                		location,
                		correoUsuario,
                		date,
                		specs,
                		state);
                
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
	}
	
	public boolean updateStateEquipment(Long entity) {
		String query = "UPDATE PRESTAMO"
				+ " SET ESTADO = 'CANCELADO'"
				+ " WHERE ID_EQUIPO = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, entity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, null, "No se pudo actualizar", AlertType.ERROR);
			return false;
		}	
		return true;
	}
	
	public boolean updatesStateEquipment(String state, Long entity) {
		String query = "UPDATE EQUIPO"
				+ " SET ESTADO = ?"
				+ " WHERE ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, state);
			pstmt.setLong(2, entity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, null, "No se pudo actualizar", AlertType.ERROR);
			return false;
		}	
		return true;
	}

	public boolean updatesState(Long entity) {
		String query = "UPDATE PRESTAMO"
				+ " SET ESTADO = 'CANCELADO'"
				+ " WHERE ID_SALA = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, entity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, null, "No se pudo actualizar", AlertType.ERROR);
			return false;
		}	
		return true;
	}
	public boolean updatesStateHall(String state, Long entity) {
		String query = "UPDATE SALA"
				+ " SET ESTADO = ?"
				+ " WHERE ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, state);
			pstmt.setLong(2, entity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, null, "No se pudo actualizar", AlertType.ERROR);
			return false;
		}	
		return true;
	}
	public boolean updateState(Long entity, String state) {
		String query = "UPDATE PRESTAMO"
				+ " SET ESTADO = ?"
				+ " WHERE ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, state);
			pstmt.setLong(2, entity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			ViewUtils.AlertWindow(null, "No se pudo actualizar", "Verifique los siguientes aspectos\n"
					+ "- Qué el estado está bien escrito.", AlertType.ERROR);
			return false;
		}	
		return true;
	}
}


    

