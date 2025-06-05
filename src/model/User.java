package model;

public class User {
	public String fullName, identificationNumber, identificationType, 
	institutionalEmail, programDepartment, phone, status, role, password;
	public Long id;
	
	public User(String fullName, String identificationNumber, String identificationType, 
			String institutionalEmail, String programDepartment, 
			String phone, String status, String role, String password, Long  id) {
		super();
		this.fullName = fullName;
		this.identificationNumber = identificationNumber;
		this.identificationType = identificationType;
		this.institutionalEmail = institutionalEmail;
		this.programDepartment = programDepartment;
		this.phone = phone;
		this.status = status;
		this.role = role;
		this.password = password;
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}


    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public String getProgramDepartment() {
        return programDepartment;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
	public void setFullName(String nombre_completo) {
		this.fullName = nombre_completo;
	}

    public Long getId() {
        return id;
    }


    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail;
    }

    public void setProgramDepartment(String programDepartment) {
        this.programDepartment = programDepartment;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

