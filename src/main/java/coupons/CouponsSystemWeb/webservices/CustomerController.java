package coupons.CouponsSystemWeb.webservices;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.CouponsSystemException;
import coupons.CouponsSystemWeb.services.CustomerService;

/**
 * 
 * Rest Controller for customer users
 *
 */
@RestController
@RequestMapping("/rest/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@Autowired
	SharedController sharedController;

	/**
	 * Purchases a coupon
	 * 
	 * @param couponId Id of coupon to purchase
	 * @param session  HttpSession
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "purchaseCoupon/{couponId}")
	public void purchaseCoupon(@PathVariable("couponId") long couponId, HttpSession session)
			throws CouponsSystemException {
		customerService.purchaseCoupon(couponId, sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons in the system
	 * 
	 * @return List of coupons in the system
	 */
	@GetMapping(path = "allCoupons")
	public List<Coupon> getAllCoupons() {
		return customerService.getAllCoupons();
	}

	/**
	 * Gets all coupons purchased by current customer
	 * 
	 * @param session HttpSession
	 * @return List of all coupons purchased by customer
	 */
	@GetMapping(path = "couponsPurchased")
	public List<Coupon> getAllPurchasedCoupons(HttpSession session) {
		return customerService.getAllPurchasedCoupons(sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons purchased by current customer by a specific type
	 * 
	 * @param couponType Type of coupon
	 * @param session    HttpSession
	 * @return List of coupons purchased by customer by a specific type
	 */
	@GetMapping(path = "couponsByType/{couponType}")
	public List<Coupon> getCouponsByType(@PathVariable("couponType") CouponType couponType, HttpSession session) {
		return customerService.getAllPurchasedCouponsbyType(couponType, sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons purchased by current customer up to a certain price
	 * 
	 * @param price   Maximum price for coupons
	 * @param session HttpSession
	 * @return List of coupons purchased by customer up to a certain price
	 */
	@GetMapping(path = "couponsByPrice/{price}")
	public List<Coupon> getCouponsbyPrice(@PathVariable("price") double price, HttpSession session) {
		return customerService.getAllPurchasedCouponsByPriceLessThan(price, sharedController.getUserId(session));
	}

	/**
	 * Gets current customer data
	 * 
	 * @param session HttpSession
	 * @return Customer Current customer
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "customer")
	public Customer getCustomer(HttpSession session) throws CouponsSystemException {
		return customerService.getCustomer(sharedController.getUserId(session));
	}

}
