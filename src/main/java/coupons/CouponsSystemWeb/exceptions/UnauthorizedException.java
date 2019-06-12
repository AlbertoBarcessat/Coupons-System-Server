package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when user is not authorized for operation
 *
 */
public class UnauthorizedException extends CouponsSystemException {
	/**
	 * Exception used when user is not authorized for operation
	 * 
	 * @param message Message to be displayed to client
	 */
	public UnauthorizedException(String message) {
		super(message);
	}
}
