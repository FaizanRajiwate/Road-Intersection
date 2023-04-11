package exceptions;
public class InaccurateDataException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4228770876983541244L;

	public InaccurateDataException(String errorMessage) {
		super(errorMessage);
	}
}
