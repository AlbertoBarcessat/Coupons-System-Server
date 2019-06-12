package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when trying to create of purchase a coupon with a passed end
 * date
 *
 */
public class InvalidDateException extends CouponsSystemException {
	/**
	 * Exception used when trying to purchase a coupon with a passed end date
	 * 
	 * @param message Message to be displayed to client
	 */
	public InvalidDateException(String message) {
		super(message);
	}
}
