package model;

public class Resources {
	private Long idResource;
    private String name, locationTrademark, typeCapacity, description;

    public Resources(Long idResource, String name, String locationTrademark, String typeCapacity, String description) {
        super();
        this.idResource = idResource;
        this.name = name;
        this.locationTrademark = locationTrademark;
        this.typeCapacity = typeCapacity;
        this.description = description;
    }

    public Long getIdResource() {
    	return idResource;
    }
    
    public String getName() {
        return name;
    }

    public String getLocationTrademark() {
        return locationTrademark;
    }

    public String getTypeCapacity() {
        return typeCapacity;
    }
    
    public String getDescription() {
    	return description;
    }
}

