package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when trying to add a value that should be unique in system
 *
 */
public class UniqueValueException extends CouponsSystemException {
	/**
	 * Exception used when trying to add a value that should be unique in system
	 * 
	 * @param message Message to be displayed to client
	 */
	public UniqueValueException(String message) {
		super(message);
	}
}
