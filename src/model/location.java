package model;

public class Location {
	private Long id;
    private String building, floor;

    public Location(Long id, String building, String floor) {
        super();
        this.id = id;
        this.building = building; 
        this.floor = floor; 
    }

    public Long getId() {
    	return id;
    }
    
    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }
}
