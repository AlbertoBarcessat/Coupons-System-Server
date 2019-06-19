package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when trying to update data with an Id in path different from object Id
 *
 */
public class InvalidIdException extends CouponsSystemException {
	/**
	 * Exception used when trying to update data with an Id in path different from object Id
	 * 
	 * @param message Message to be displayed to client
	 */
	public InvalidIdException(String message) {
		super(message);
	}
}
