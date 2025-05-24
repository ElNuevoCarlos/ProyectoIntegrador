package model;

public class Resources {
    private String name, locationTrademark, typeCapacity;

    public Resources(String name, String locationTrademark, String typeCapacity) {
        super();
        this.name = name;
        this.locationTrademark = locationTrademark;
        this.typeCapacity = typeCapacity;
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
}

