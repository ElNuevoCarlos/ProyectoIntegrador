package model;

public class User {
	public String nombre_completo, numero_identificacion, tipo_identificacion, 
	correo_institucional, programa_departamento, telefono, estado, rol, password;
	public Long id;
	
	public User(String nombre_completo, String numero_identificacion, String tipo_identificacion, 
			String correo_institucional, String programa_departamento, 
			String telefono, String estado, String rol, String password, Long  id) {
		super();
		this.nombre_completo = nombre_completo;
		this.numero_identificacion = numero_identificacion;
		this.tipo_identificacion = tipo_identificacion;
		this.correo_institucional = correo_institucional;
		this.programa_departamento = programa_departamento;
		this.telefono = telefono;
		this.estado = estado;
		this.rol = rol;
		this.password = password;
		this.id = id;
	}

	public String getNombre_completo() {
		return nombre_completo;
	}

    public String getName() {
        return name;
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
	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
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

