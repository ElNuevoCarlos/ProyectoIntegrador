package model;

public class User {
	public String card, name, email, phone, estate, password, role;
	
	public User(String card, String name, String email, String phone, String estate, String password, String role) {
		super();
		this.card = card;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.estate = estate;
		this.password = password;
		this.role = role;
	}

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getEstate() {
		return estate;
	}
}
