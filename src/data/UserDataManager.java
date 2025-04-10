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
	
	public boolean verifyUser(String name) {
        for (User userx : getUsers()) {
            if (name.equals(userx.fullName)) {
            	return true;
            }
        }
        return false;
	}
	public User verifyPassword(String name, String password) {
        for (User userx : getUsers()) {
        	if (name.equals(userx.fullName) && password.equals(userx.password)) {
            	return userx;
            }
        }
        return null;
	}
	
	public void loadData() throws SQLException {
		String sql = "SELECT * FROM USERTEACHER";
		
		try (Connection conexion = new DataBase().getConexionTeacher();
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
                addUser(new User(card, name, email, phone, estate, password, role));
            }
		} catch (SQLException e) {
            System.err.println(e.getMessage());
        }
	}
}