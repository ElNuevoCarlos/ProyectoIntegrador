package model;

import java.time.LocalDate;

public class EqupmentInfo {
	private Long id;
	private LocalDate dateLoan;
	private String name, description, emailClient, specs, serialNumber, state;
	
	
	public EqupmentInfo(Long id, String name, String description, String emailClient,
			String specs, String serialNumber, LocalDate dateLoan, String state) {
		
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.emailClient = emailClient;
		this.specs = specs;
		this.serialNumber = serialNumber;
		this.dateLoan = dateLoan;
		this.state = state;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getEmailClient() {
		return emailClient;
	}
	
	public void setSpecs(String newSpecs) {
		this.specs = newSpecs;
	}
	
	public String getSpecs() {
		return specs;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public LocalDate getDateLoan() {
		return dateLoan;
	}
	
	public String getState() {
		return state;
	}
}
