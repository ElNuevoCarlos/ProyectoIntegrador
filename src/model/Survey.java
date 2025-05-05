package model;

public class Survey {
	private long id, idLoanHall, idLoanDevice;
	private String functionality, qualityService, infrastructure, suggestions;
	
	public Survey(long id, String functionality, String qualityService, String infrastructure, String suggestions, long idLoanHall, long idLoanDevice) {
		super();
		this.id = id;
		this.functionality = functionality;
		this.qualityService = qualityService;
		this.infrastructure = infrastructure;
		this.suggestions = suggestions;
		this.idLoanHall = idLoanHall;
		this.idLoanDevice = idLoanDevice;	
	}
	
	public long getId() {
		return id;
	}
	
	public String getFunctionality() {
		return functionality;
	}
	
	public String getQualityService() {
		 return qualityService;
	 }
	
	public String getInfrastructure() {
		return infrastructure;
	}
	
	public String getSuggestions() {
		return suggestions;
	}
	
	public long getIdLoanHall() {
		return idLoanHall;
	}
	
	public long getIdLoanDevice() {
		return idLoanDevice;
	}
}
