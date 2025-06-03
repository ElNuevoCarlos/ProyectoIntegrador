package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.control.Alert.AlertType;
import model.Hall;
import model.Location;
import model.Resources;
import utils.ViewUtils;

public class ResourcesDAO {
    private Connection connection;

    public ResourcesDAO(Connection connection) {
        this.connection = connection;
    }
    
	public ArrayList<Resources> ResourcesView(Boolean type, StringBuilder second) {
        ArrayList<Resources> loansDeviceList = new ArrayList<>();
        String query;

        if (type) {
        	
            query = "SELECT s.NOMBRE, s.CAPACIDAD ,u.EDIFICIO || ' - ' || u.PISO AS LOCALIZACION, s.DESCRIPCION, s.ID, s.ESTADO\r\n"
            		+ "FROM SALA s JOIN UBICACION u ON s.ID_UBICACION = u.ID\r\n"
            		+ second;
        } else {
            query = "SELECT NOMBRE, TIPO_DISPOSITIVO, MARCA, DESCRIPCION, ID\r\n"
            		+ "FROM EQUIPO\r\n"
            		+ "WHERE ESTADO = 'Disponible' AND CATEGORIA = 'DISPOSITIVO'" + second;
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
               while (rs.next()) {
                   String name = rs.getString(1);
                   String type_capacity = rs.getString(2);
                   String location_trademark = rs.getString(3);
                   String description = rs.getString(4);
            	   long idResource = rs.getLong(5);
            	   String state = rs.getString(6);
                   
                   Resources loansDevice = new Resources(idResource, name, location_trademark, type_capacity, description, type, state);
                   loansDeviceList.add(loansDevice);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

        return loansDeviceList;
    }
	public ArrayList<Location> LocationView() {
        ArrayList<Location> list = new ArrayList<>();
        String query = "SELECT id, edificio, piso FROM UBICACION";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
           	
               while (rs.next()) {
            	   long id = rs.getLong(1);
                   String building = rs.getString(2);
                   String floor = rs.getString(3);
           		
                   Location location = new Location(id, building, floor);
                   list.add(location);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        return list;
    }
	public ArrayList<Hall> HallView() {
        ArrayList<Hall> list = new ArrayList<>();
        String query = "SELECT id, nombre, id_ubicacion, capacidad, estado, descripcion FROM SALA";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
           	
               while (rs.next()) {
            	   long id = rs.getLong(1);
                   String name = rs.getString(2);
                   Long id_location = rs.getLong(3);
                   String capacity = rs.getString(4);
                   String state = rs.getString(5);
                   String description = rs.getString(6);
                   
                   Hall hall = new Hall(id, name, id_location, capacity, description, state);
                   list.add(hall);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        return list;
    }
	
    public void saveHall(Hall hall) {
    	String queryLocation = "INSERT INTO SALA"
    			+ " (ID, NOMBRE, ID_UBICACION, CAPACIDAD, ESTADO, DESCRIPCION)"
    			+ " VALUES (SEQ_SALA_ID.NEXTVAL, ?, ?, ?, ?, ?)";

	    	try (PreparedStatement pstmt = connection.prepareStatement(queryLocation)) {
				pstmt.setString(1, hall.getName());
	            pstmt.setLong(2, hall.getIdLocation());
	            pstmt.setString(3, hall.getCapacity());
	            pstmt.setString(4, hall.getState());
	            pstmt.setString(5, hall.getDescription());
	            
	            pstmt.executeUpdate();
	    	} catch (SQLException e) {
	    		ViewUtils.AlertWindow(null, "Ocurrio un error", e.getMessage(), AlertType.INFORMATION);
			}
    }
    
    public void saveLocation(Location location) {
    	String queryLocation = "INSERT INTO UBICACION"
    			+ " (ID, EDIFICIO, PISO)"
    			+ " VALUES (SEQ_UBICACION_ID.NEXTVAL, ?, ?)";

	    	try (PreparedStatement pstmt = connection.prepareStatement(queryLocation)) {
				pstmt.setString(1, location.getBuilding());
	            pstmt.setString(2, location.getFloor());
	
	            pstmt.executeUpdate();
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
    }
	public Long verifyLocation(String name, String floor) {
		Long id = null;
        String query = "SELECT ID FROM UBICACION WHERE UPPER(EDIFICIO) = ? AND PISO = ?";       
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
        	pstmt.setString(1, name.toUpperCase());
        	pstmt.setString(2, floor);
        	ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id; 
	}
    
	public boolean AuthenticateHall(String name) {
		for (Hall hall : HallView()) {
			if (hall.getName().equals(name)) return true;
		}
		return false;
	}
	public boolean AuthenticateBuildingFloor(String building, String floor) {
		for (Location location : LocationView()) {
			if (location.getBuilding().equals(building) && location.getFloor().equals(floor)) return true;
		}
		return false;
	}
}
