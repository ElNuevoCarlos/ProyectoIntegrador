package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.Alert.AlertType;
import model.Equipment;
import model.EqupmentInfo;
import model.Hall;
import model.Location;
import model.Resources;
import utils.ViewUtils;

public class ResourcesDAO {
    private Connection connection;

    public ResourcesDAO(Connection connection) {
        this.connection = connection;
    }
    
	public ArrayList<Equipment> EquipmentView() {
        ArrayList<Equipment> equipments = new ArrayList<>();
        
        String query = "SELECT ID, NOMBRE, TIPO_DISPOSITIVO, CATEGORIA, MARCA, NUMERO_SERIE, ESTADO, DESCRIPCION "
            		+ " FROM EQUIPO";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
               while (rs.next()) {
            	   long id = rs.getLong(1);
                   String name = rs.getString(2);
                   String type = rs.getString(3);
                   String category = rs.getString(4);
                   String Brand = rs.getString(5); // Marca
                   String Series = rs.getString(6);
            	   String state = rs.getString(7);
            	   String description = rs.getString(8);

                   Equipment equipment = new Equipment(id, name, category, type, Brand, Series, state, description, null);
                   equipments.add(equipment);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

        return equipments;
    }
	
	public ArrayList<EqupmentInfo> EquipmentLoanView() {
        ArrayList<EqupmentInfo> equipments = new ArrayList<>();
        
        String query = "SELECT P.ID, E.NOMBRE, E.DESCRIPCION, U.CORREO_INSTITUCIONAL, P.ESPECIFICACIONES, E.NUMERO_SERIE, P.FECHA, P.ESTADO, E.TIPO_DISPOSITIVO, E.MARCA"
            		+ " FROM EQUIPO E JOIN PRESTAMO P ON P.ID_EQUIPO = E.ID"
            		+ " JOIN USUARIO U ON P.ID_USUARIO = U.ID";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
               while (rs.next()) {
            	   long id = rs.getLong(1);
                   String name = rs.getString(2);
                   String description = rs.getString(3);
                   String emailClient = rs.getString(4);
                   String specs = rs.getString(5);
                   String series = rs.getString(6); 
                   LocalDate dateLoan = rs.getDate(7).toLocalDate();;
            	   String state = rs.getString(8);
            	   String type = rs.getString(9);
            	   String brand = rs.getString(10);

            	   EqupmentInfo equipment = new EqupmentInfo(id, 
            			   name, description, 
            			   emailClient, specs, 
            			   series, dateLoan, 
            			   state, type, brand);
            	   
                   equipments.add(equipment);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

        return equipments;
    }

	public ArrayList<Resources> ResourcesView(Boolean type, StringBuilder second) {
        ArrayList<Resources> loansDeviceList = new ArrayList<>();
        String query;

        if (type) {
            query = "SELECT s.NOMBRE, s.CAPACIDAD ,u.EDIFICIO || ' - ' || u.PISO AS LOCALIZACION, s.DESCRIPCION, s.ID, s.ESTADO"
            		+ " FROM SALA s JOIN UBICACION u ON s.ID_UBICACION = u.ID "+ second;
        } else {
            query = "SELECT NOMBRE, TIPO_DISPOSITIVO, MARCA, DESCRIPCION, ID"
            		+ " FROM EQUIPO"
            		+ " WHERE ESTADO = 'Disponible' AND CATEGORIA = 'DISPOSITIVO'" + second;
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
	
	
	public void saveEquipment(Equipment equipment) {
    	String sql = "{ call TECHLEND.saveEquipment(?, ?, ?, ?, ?, ?, ?) }";

	    	try (CallableStatement stmt = connection.prepareCall(sql)) {
	    		stmt.setString(1, equipment.getName());
	    		stmt.setString(2, equipment.getCategory());
	    		stmt.setString(3, equipment.getDeviceType());
	    		stmt.setString(4, equipment.getBrand());
	    		stmt.setString(5, equipment.getSerialNumber());
	    		stmt.setString(6, equipment.getDescription());
	    		stmt.setLong(7, equipment.getIdHall());
	            
	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	            	ViewUtils.AlertWindow(
	            		    "Éxito", 
	            		    "Equipo creado", 
	            		    "El equipo ha sido creado con éxito.", 
	            		    AlertType.INFORMATION
	            		);
	            }
	    	} catch (SQLException e) {
	    		ViewUtils.AlertWindow(null, "Ocurrio un error", e.getMessage(), AlertType.INFORMATION);
			}
	}
	
    public void saveHall(Hall hall) {
    	String sql = "{call saveHall(?, ?, ?, ?)}";

    	try (CallableStatement cstmt = connection.prepareCall(sql)) {
    	    cstmt.setString(1, hall.getName());
    	    cstmt.setLong(2, hall.getIdLocation());
    	    cstmt.setString(3, hall.getCapacity());
    	    cstmt.setString(4, hall.getDescription());

            int rowsAffected = cstmt.executeUpdate();
            if (rowsAffected > 0) {
            	ViewUtils.AlertWindow(
            		    "Éxito", 
            		    "Sala creado", 
            		    "La sala ha sido creada con éxito.", 
            		    AlertType.INFORMATION
            		);
            }
    	} catch (SQLException e) {
    	    ViewUtils.AlertWindow(null, "Ocurrió un error", e.getMessage(), AlertType.INFORMATION);
    	}

    }
    
    public void saveLocation(Location location) {
    	String sql = "{call saveLocation(?, ?)}";

    	try (CallableStatement cstmt = connection.prepareCall(sql)) {
    	    cstmt.setString(1, location.getBuilding());
    	    cstmt.setString(2, location.getFloor());

    	    cstmt.execute();
    	} catch (SQLException e) {
    		ViewUtils.AlertWindow(null, "Ocurrió un error", e.getMessage(), AlertType.INFORMATION);
    	}

    }
    
	
	public Long verifyLocation(String name, String floor) {
	    Long id = null;
	    String sql = "{ ? = call verifyLocation(?, ?) }";

	    try (CallableStatement cstmt = connection.prepareCall(sql)) {
	        cstmt.registerOutParameter(1, Types.BIGINT);
	        cstmt.setString(2, name);
	        cstmt.setString(3, floor);

	        cstmt.execute();

	        id = cstmt.getLong(1);
	        if (cstmt.wasNull()) {
	            id = null;
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
