package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when trying to create a coupon with non positive amount
 *
 */
public class InvalidAmountException extends CouponsSystemException {
	/**
	 * Exception used when trying to create a coupon with non positive amount
	 * 
	 * @param message Message to be displayed to client
	 */
	public InvalidAmountException(String message) {
		super(message);
	}
}
