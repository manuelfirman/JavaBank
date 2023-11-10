package entidad;

import java.time.LocalDate;




public class Cliente {
    private int idCliente; // Clave prAimaria (PK)
    private String usuario;
    private String contrasena;
    private int activo;
    private LocalDate fechaCreacion; // Fecha de creación
    private TipoCliente tipoCliente; // Enum para el tipo de cliente
    private int dni; // Debe ser único
    private String cuil;
    private String nombre;
    private String apellido;
    private Sexo sexo; // Enum para el sexo
    private String nacionalidad;
    private LocalDate fechaNacimiento; // Fecha de nacimiento
    private String direccion;
    private Localidad localidad; // Objeto Localidad
    private Provincia provincia; // Objeto Provincia
    private String correo;
    
    public enum TipoCliente {
        CLIENTE, // 0
        ADMIN    // 1
    }

    public enum Sexo {
        VARON,     // 0
        MUJER,     // 1
        INDEFINIDO // 2
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    public void setTipoCliente(int tipo) {
    	if(tipo == 0)
    		this.tipoCliente = TipoCliente.CLIENTE;
    	else
    		this.tipoCliente = TipoCliente.ADMIN;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Sexo getSexo() {
        return sexo;
    }    

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
    
    public void setSexo(int sexo) {
    	if(sexo == 0 )
    		this.sexo = Sexo.VARON;
    	else if(sexo == 1)
    		this.sexo = Sexo.MUJER;
    	else
    		this.sexo = Sexo.INDEFINIDO;
    }
    

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public Cliente() {
        this.activo = 1;
        this.tipoCliente = TipoCliente.CLIENTE;
        this.sexo = Sexo.INDEFINIDO; 
    }
    
}