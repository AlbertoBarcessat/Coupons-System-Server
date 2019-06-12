package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when login fails
 *
 */
public class InvalidLoginException extends CouponsSystemException {
	/**
	 * Exception used when login fails
	 * 
	 * @param message Message to be displayed to client
	 */
	public InvalidLoginException(String message) {
		super(message);
	}
}
