package model;

public class Resources {
	private Long idResource;
    private String name, locationTrademark, typeCapacity, description; 
    private Boolean typeResource;

    public Resources(Long idResource, String name, String locationTrademark, String typeCapacity, String description, Boolean typeResource) {
        super();
        this.idResource = idResource; // ID SALA, ID DISPOSITIVO
        this.name = name; // NOMBRE DISPOSITIVO, NOMBRE SALA
        this.locationTrademark = locationTrademark; //UBICACIÓN SALA, MARCA DEL DISPOSITIVO
        this.typeCapacity = typeCapacity; // TIPO DISPOSITIVO, CAPACIDAD SALA
        this.description = description; // DESCRIPCIÓN SALA, DISPOSITIVO
        this.typeResource = typeResource; // SALA, DISPOSITIVO
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

