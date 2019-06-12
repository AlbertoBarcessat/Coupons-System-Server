package coupons.CouponsSystemWeb.exceptions;

/**
 * 
 * Exception used when trying to purchase a coupon out of stock (amount is 0)
 *
 */
public class OutofStockException extends CouponsSystemException {
	/**
	 * Exception used when trying to purchase a coupon out of stock (amount is 0)
	 * 
	 * @param message Message to be displayed to client
	 */
	public OutofStockException(String message) {
		super(message);
	}
}
