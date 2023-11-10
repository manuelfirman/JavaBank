package excepciones;

public class DNIExistenteException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DNIExistenteException() {
	
	}

	@Override
	public String getMessage() {
		return "El DNI ingresado ya existe";
	}
	
	
	

}
