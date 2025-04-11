package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) {
        // Definimos las queries para insertar en las tablas PERSONA y CUENTA
        String queryPersona = "INSERT INTO PERSONA (ID, NOMBRE_COMPLETO, TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION, CORREO_INSTITUCIONAL, TELEFONO, PROGRAMA_DEPARTAMENTO) " +
                              "VALUES (SEQ_PERSONA_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        String queryCuenta = "INSERT INTO CUENTA (ID, USUARIO, PASSWORD, ROL, ID_PERSONA) VALUES (SEQ_CUENTA_ID.NEXTVAL, ?, ?, ?, ?)";
        
        try {
            // Comenzamos una transacción
            connection.setAutoCommit(false); // Desactivamos el autocommit

            // Insertamos la persona
            try (PreparedStatement pstmt = connection.prepareStatement(queryPersona, new String[] {"ID"})) {
                pstmt.setString(1, user.getFullName());
                System.out.println("NAME");
                pstmt.setString(2, user.getTI());
                System.out.println("TI");
                pstmt.setString(3, user.getNumIdentification());
                System.out.println("NUMID");
                pstmt.setString(4, user.getEmail());
                System.out.println("EMAIL");
                pstmt.setString(5, user.getPhone());
                System.out.println("PHONE");
                pstmt.setString(6, user.getPro_dep());
                System.out.println("PRO_DEP");

                int rowsAffected = pstmt.executeUpdate();
                System.out.println("UPDATE");
                if (rowsAffected > 0) {
                    System.out.println("User inserted successfully.");
                }
                System.out.println("FUNCIONO");

                // Recuperamos el ID generado para la persona
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idPersona = rs.getInt(1); // ID_PERSONA generado
                        System.out.println("GENERADO");
                        // Ahora insertamos la cuenta, utilizando el ID_PERSONA
                        try (PreparedStatement pstmtCuenta = connection.prepareStatement(queryCuenta)) {
                            pstmtCuenta.setString(1, user.getUsername());
                            pstmtCuenta.setString(2, user.getPassword());
                            pstmtCuenta.setString(3, user.getRole());
                            pstmtCuenta.setInt(4, idPersona); // Usamos el ID de la persona
                            System.out.println("DATOS CUENTA");
                            pstmtCuenta.executeUpdate();        
                            System.out.println("Account inserted successfully.");
                        }
                    }
                }
                
                // Si todo fue exitoso, confirmamos la transacción
                connection.commit();
                System.out.println("COMMIT");

            } catch (SQLException e) {
                // Si ocurre algún error en las inserciones, revertimos todo
                connection.rollback();
                System.out.println("Error occurred, transaction rolled back.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            // Si ocurre un error en la configuración de la transacción, revertimos y mostramos el error
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Error with transaction setup, rolled back.");
            e.printStackTrace();
        } finally {
            try {
                // Restauramos el autoCommit a su valor original
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
