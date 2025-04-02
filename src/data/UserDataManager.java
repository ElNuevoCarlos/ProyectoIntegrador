package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.User;

public class UserDataManager {
	private static UserDataManager instance;
	
	private ArrayList<User> users = new ArrayList<>();
	
	public static UserDataManager getInstance() {
		if (instance == null) instance = new UserDataManager();
		return instance;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) { 
		users.add(user);
	}
	
	public void loadData() throws SQLException {
		String sql = "SELECT * FROM USERTEACHER";
		
		try (Connection conexion = new Oracle().getConexionTeacher();
				Statement statement = conexion.createStatement();
				ResultSet rs = statement.executeQuery(sql)){
            
            while (rs.next()) {
            	String card = rs.getString("CAR");
            	String name = rs.getString("NAME");
            	String email = rs.getString("EMAIL");
            	String phone = rs.getString("PHONE");
            	String estate = rs.getString("ESTATE");
            	String password = rs.getString("PASSWORD");
            	String role = rs.getString("ROLE");
                users.add(new User(card, name, email, phone, estate, password, role));
            }
		} catch (SQLException e) {
            System.err.println("Error: "+e.getMessage());
        }
	}
}
