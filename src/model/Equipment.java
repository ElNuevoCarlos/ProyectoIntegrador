package model;

public class Equipment {
	private long id, idHall;
	private String name, category, deviceType, brand, serialNumber, state, description;
	
	
	public Equipment(long id, String name, String category, String deviceType, 
			String brand, String serialNumber, String state, String description, long idHall) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.deviceType = deviceType;
		this.brand = brand;
		this.serialNumber = serialNumber;
		this.state = state;
		this.description = description;
		this.idHall = idHall;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	
	public String getBrand() {
		return brand;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getState() {
		return state;
	}
	
	public String getDescription() {
		return description;
	}
	
	public long getIdHall() {
		return idHall;
	}
}
