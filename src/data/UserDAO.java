package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.User;

public class UserDAO {
	private Connection connection;
	
	public UserDAO(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public void save(User user) {
        String queryPersona = "INSERT INTO PERSONA (NOMBRE_COMPLETO, TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION, CORREO_INSTITUCIONAL, TELEFONO, PROGRAMA_DEPARTAMENTO) VALUES (?, ?, ?, ?, ?, ?)";
        String queryCuenta = "INSERT INTO CUENTA (USUARIO, PASSWORD, ROL) VALUES (?, ?, ?)";
        
        try {
            connection.setAutoCommit(false); // Inicia la transacción

            // Insertar en PERSONA
            try (PreparedStatement pstmtPersona = connection.prepareStatement(queryPersona)) {
                pstmtPersona.setString(1, user.getFullName());
                pstmtPersona.setString(2, user.getTI());
                pstmtPersona.setString(3, user.getNumIdentification());
                pstmtPersona.setString(4, user.getEmail());
                pstmtPersona.setString(5, user.getPhone());
                pstmtPersona.setString(6, user.getPro_dep());
                pstmtPersona.executeUpdate();
            }

            // Insertar en CUENTA
            try (PreparedStatement pstmtCuenta = connection.prepareStatement(queryCuenta)) {
                pstmtCuenta.setString(1, user.getUsername());
                pstmtCuenta.setString(2, user.getPassword());
                pstmtCuenta.setString(3, user.getRole());
                pstmtCuenta.executeUpdate();
            }

            connection.commit(); // Confirma la transacción si todo va bien
            System.out.println("Usuario insertado correctamente.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Cancela la transacción si algo falla
                System.out.println("Inserción fallida, se ha hecho rollback.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Vuelve al modo automático
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }}
