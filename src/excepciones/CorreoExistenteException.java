package excepciones;

public class CorreoExistenteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CorreoExistenteException() {
		
	}

	@Override
	public String getMessage() {
		return "El Correo Electronico ingresado ya existe";
	}
	
	
}
