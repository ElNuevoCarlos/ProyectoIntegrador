package model;

public class Hall {
	private Long id, id_location;
    private String name, capacity, state, description; 

    public Hall(Long id, String name, Long id_location, String capacity, String description, String state) {
        super();
        this.id = id; 
        this.name = name;
        this.id_location= id_location;
        this.capacity = capacity;
        this.state = state; 
        this.description = description; 
    }
    public Long getId() {
    	return id;
    }
    public String getName() {
        return name;
    }
    public Long getIdLocation() {
        return id_location;
    }
    public String getCapacity() {
        return capacity;
    }
    public String getDescription() {
    	return description;
    }
    public String getState() {
    	return state;
    }
}

