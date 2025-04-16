package model;

public class User {
	public String fullName, TI, numIdentification, email, phone, pro_dep, password, role;
	// falta definir el estado de la cuenta
	public User(String fullName, String TI, String numIdentification, String email, String phone, String pro_dep, String role, String password) {
		super();
		this.fullName = fullName;
		this.TI = TI;
		this.numIdentification = numIdentification;
		this.email = email;
		this.phone = phone;
		this.pro_dep = pro_dep;
		this.role = role;
		this.password = password;
		
	}

    public String getFullName() {
        return fullName;
    }
    
	public String getTI() {
		return TI;
	}
	
	public String getNumIdentification() {
		return numIdentification;
	}
	
    public String getEmail() {
        return email;
    }
	
	public String getPhone() {
		return phone;
	}
    
	public String getPro_dep() {
		return pro_dep;
	}
	
	public String getRole() {
		return role;
	}
	
    public String getPassword() {
        return password;
    }
}
