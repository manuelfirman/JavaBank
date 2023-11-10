package entidad;

public class Localidad {
	int id;
	int idProvincia;
	String nombre;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Localidad(int iD, int iDProvincia, String nombre) {
		super();
		id = iD;
		idProvincia = iDProvincia;
		this.nombre = nombre;
	}

	public Localidad() {}
}