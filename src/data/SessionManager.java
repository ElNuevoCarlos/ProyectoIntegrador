package data;

public class SessionManager {
    private static SessionManager instance;
    private String name;
    private String role;
    private String email;

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void setUser(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
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