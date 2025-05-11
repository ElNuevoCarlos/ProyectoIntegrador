package model;

public class User {
	public String nombre_completo, numero_identificacion, tipo_identificacion, 
	correo_institucional, programa_departamento, telefono, estado, rol, password;
	public Long id;
	
	public User(String nombre_completo, String numero_identificacion, String tipo_identificacion, 
			String correo_institucional, String programa_departamento, 
			String telefono, String estado, String rol, String password, Long id) {
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

	public String getNumero_identificacion() {
		return numero_identificacion;
	}

	public String getTipo_identificacion() {
		return tipo_identificacion;
	}

	public String getCorreo_institucional() {
		return correo_institucional;
	}

	public String getPrograma_departamento() {
		return programa_departamento;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getEstado() {
		return estado;
	}

	public String getRol() {
		return rol;
	}

	public String getPassword() {
		return password;
	}

	public String getId() {
		return id;
	}

	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

	public void setNumero_identificacion(String numero_identificacion) {
		this.numero_identificacion = numero_identificacion;
	}

	public void setTipo_identificacion(String tipo_identificacion) {
		this.tipo_identificacion = tipo_identificacion;
	}

	public void setCorreo_institucional(String correo_institucional) {
		this.correo_institucional = correo_institucional;
	}

	public void setPrograma_departamento(String programa_departamento) {
		this.programa_departamento = programa_departamento;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
