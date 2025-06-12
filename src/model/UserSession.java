package model;

public class UserSession {
    private static UserSession instance;
    private Long id;
    private String name;
    private String role;
    private String email;

    // Private constructor to prevent instantiation
    private UserSession(Long id, String name, String role, String email) {
    	this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    // Static method to initialize or get the instance
    public static UserSession getInstance(Long id, String name, String role, String email) {
        if (instance == null) {
            instance = new UserSession(id, name, role, email);
        }
        return instance;
    }

    // Overload for just accessing the session
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("User session has not been initialized.");
        }
        return instance;
    }
    
    

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
    
    public Long getId() {
    	return id;
    }
    
    public String getEmail() {
    	return email;
    }

    // Method to destroy session (e.g., on logout)
    public static void destroy() {
        instance = null;
    }
}
