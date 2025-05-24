package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Resources;

public class ResourcesDAO {
    private Connection connection;

    public ResourcesDAO(Connection connection) {
        this.connection = connection;
    }
    
	public ArrayList<Resources> ResourcesView(Boolean type, StringBuilder second) {
        ArrayList<Resources> loansDeviceList = new ArrayList<>();
        String query;

        if (type) {
            query = "SELECT s.NOMBRE, s.CAPACIDAD ,u.EDIFICIO || ' - ' || u.PISO AS LOCALIZACION\r\n"
            		+ "FROM SALA s JOIN UBICACION u ON s.ID_UBICACION = u.ID\r\n"
            		+ "WHERE ESTADO = 'Disponible'" + second;
        } else {
            query = "SELECT NOMBRE, TIPO_DISPOSITIVO, MARCA\r\n"
            		+ "FROM EQUIPO\r\n"
            		+ "WHERE ESTADO = 'Disponible' AND CATEGORIA = 'DISPOSITIVO'" + second;
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
           	
               while (rs.next()) {
                   String name = rs.getString(1);
                   String type_capacity = rs.getString(2);
                   String location_trademark = rs.getString(3);
                   
                   Resources loansDevice = new Resources(name, location_trademark, type_capacity);
                   loansDeviceList.add(loansDevice);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

        return loansDeviceList;
    }

}
