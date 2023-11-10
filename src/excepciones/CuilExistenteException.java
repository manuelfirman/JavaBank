package excepciones;

public class CuilExistenteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CuilExistenteException() {
		
	}

	@Override
	public String getMessage() {
		return "El CUIL ingresado ya existe";
	}
	
	
}
