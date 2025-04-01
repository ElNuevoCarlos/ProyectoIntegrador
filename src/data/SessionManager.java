package data;

public class SessionManager {
    private static SessionManager instance;
    private String name;
    private String role;

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void setUser(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void logout() {
        instance = null;
    }
}
