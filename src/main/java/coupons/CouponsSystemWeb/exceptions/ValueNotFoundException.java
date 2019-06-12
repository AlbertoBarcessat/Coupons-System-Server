package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when trying to fetch data that was not found in system
 *
 */
public class ValueNotFoundException extends CouponsSystemException {
	/**
	 * Exception used when trying to fetch data that was not found insystem
	 * 
	 * @param message Message to be displayed to client
	 */
	public ValueNotFoundException(String message) {
		super(message);
	}
}
