package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Teacher;

public class TeacherDataManager {
	private static TeacherDataManager instance;
	
	private ArrayList<Teacher> teachers = new ArrayList<>();
	
	public static TeacherDataManager getInstance() {
		if (instance == null) instance = new TeacherDataManager();
		return instance;
	}
	
	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}
	public void Data() throws SQLException {
		Connection conexion = new Oracle().getConexionTeacher();
		
		if(conexion != null){
			Statement stmt = conexion.createStatement();
            String sql = "SELECT NOMBRE, IDESPECIE FROM animal WHERE UPPER(NOMBRE) LIKE 'MARMOTE'";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String id = rs.getString("NOMBRE");
                int IDESPECIE = rs.getInt("IDESPECIE");

                System.out.println("NAME: "+id+", IDESPECIE: "+IDESPECIE);

                //Teacher teacher = new Teacher("1095788069", "Carlos", "cpinto5@udi.edu.co", "3112685852", "x", "carlos123", "Profesor");
                //teachers.add(teacher);
            }
		} else {
			System.out.println("Conexi�n fallida");
		}
	}
	
	
	

}
