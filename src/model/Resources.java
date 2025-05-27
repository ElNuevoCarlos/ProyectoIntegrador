package model;

public class Resources {
	private Long idResource;
    private String name, locationTrademark, typeCapacity, description; 
    private Boolean typeResource;

    public Resources(Long idResource, String name, String locationTrademark, String typeCapacity, String description, Boolean typeResource) {
        super();
        this.idResource = idResource;
        this.name = name;
        this.locationTrademark = locationTrademark;
        this.typeCapacity = typeCapacity;
        this.description = description;
        this.typeResource = typeResource;
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
    
    public Boolean getTypeResource() {
    	return typeResource;
    }
}

