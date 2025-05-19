package data;

public class SessionManager {
    private static SessionManager instance;
    private Long id;
    private String name, role, email;

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void setUser(Long id, String name, String role, String email) {
    	this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
    }
    
    public Long getId() {
    	return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
    
    public String getEmail() {
    	return email;
    }
}