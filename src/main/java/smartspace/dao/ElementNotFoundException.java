package smartspace.dao;

public class ElementNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4749745643925803534L;

	public ElementNotFoundException() {
	}

	public ElementNotFoundException(String message) {
		super(message);
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
	}

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
