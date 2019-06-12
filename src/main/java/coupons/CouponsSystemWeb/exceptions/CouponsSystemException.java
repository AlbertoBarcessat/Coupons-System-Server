package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Main exception in Coupon System, other exceptions extend this exception
 *
 */
public class CouponsSystemException extends Exception {
	/**
	 * Main exception in Coupon System, other exceptions extend this exception
	 * 
	 * @param message Message to be displayed to client
	 */
	public CouponsSystemException(String message) {
		super(message);
	}
}
