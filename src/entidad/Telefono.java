package entidad;

public class Telefono {
	private int idTelefono;
	private Cliente cliente;
	private String numero;
	private int activo;
	
	public Telefono() {
		
	}

	public Telefono(int idTelefono, Cliente cliente, String numero, int activo) {
		super();
		this.idTelefono = idTelefono;
		this.cliente = cliente;
		this.numero = numero;
		this.activo = activo;
	}

	public int getIdTelefono() {
		return idTelefono;
	}

	public void setIdTelefono(int idTelefono) {
		this.idTelefono = idTelefono;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	
}