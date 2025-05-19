package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Filter {
    private Connection connection;

    public Filter(Connection connection) {
        this.connection = connection;
    }
    
    public List<String> Options(String column, String table, String building) {
    	List<String> listOptions = new ArrayList<>();
    	String query = "SELECT DISTINCT " + column +" FROM " + table;
    	if (column.equals("PISO")) {
    		query += " WHERE EDIFICIO = '" + building + "'";
    	}
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
           	
               while (rs.next()) {
                   String options = rs.getString(1); 
                   listOptions.add(options);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
        return listOptions;
    }
}
