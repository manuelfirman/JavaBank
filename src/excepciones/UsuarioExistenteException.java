package excepciones;

public class UsuarioExistenteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UsuarioExistenteException() {
		
	}

	@Override
	public String getMessage() {
		return "El Usuario ingresado ya existe";
	}
	
	
}
