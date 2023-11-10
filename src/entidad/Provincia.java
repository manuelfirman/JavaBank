package entidad;

public class Provincia {
	int id;
	String nombre;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Provincia(int iD, String nombre) {
		super();
		this.id = iD;
		this.nombre = nombre;
	}
	
	public Provincia() {}
}