package coupons.CouponsSystemWeb.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.InvalidDateException;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.exceptions.OutofStockException;
import coupons.CouponsSystemWeb.exceptions.UniqueValueException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;
import coupons.CouponsSystemWeb.repositories.CouponRepository;
import coupons.CouponsSystemWeb.repositories.CustomerRepository;

/**
 * 
 * Service for implementing customer client operations
 *
 */
@Service
public class CustomerServiceImplement implements CustomerService {

	@Autowired
	private CustomerRepository customerRep;

	@Autowired
	private CouponRepository couponRep;

	/**
	 * Login into system using name, password and client type
	 * 
	 * @param name       Name of client
	 * @param password   User password
	 * @param clientType Type of client - Should be 'Customer'
	 * @return id Id of customer user
	 * @throws InvalidLoginException When credentials are invalid for customer user
	 */
	@Override
	public long login(String name, String password, ClientType clientType) throws InvalidLoginException {
		// Check client is a customer
		if (!clientType.equals(ClientType.CUSTOMER)) {
			throw new InvalidLoginException("Failed to login with invalid customer client type");
		}

		// Find customer
		List<Customer> customers = customerRep.findByCustNameAndPassword(name, password);
		if (customers.size() != 1) {
			throw new InvalidLoginException("Failed to login with invalid credentials for customer " + name);
		}

		// Return found customer
		return customers.get(0).getCustomerId();
	}

	/**
	 * Purchases a coupon for logged customer. Customers can't purchase the same
	 * coupon twice, or if coupon is out of stock or expired.
	 * 
	 * @param couponId Id of coupon to be purchased
	 * @param userId   Id of user who is logged in
	 * @throws UniqueValueException   When coupon was already purchased by same
	 *                                customer
	 * @throws OutofStockException    When coupon is out of stock
	 * @throws InvalidDateException   When end date of coupon has passed or is
	 *                                invalid
	 * @throws ValueNotFoundException When customer or coupon are not found
	 */
	@Override
	@Transactional
	public synchronized void purchaseCoupon(long couponId, long userId)
			throws OutofStockException, UniqueValueException, InvalidDateException, ValueNotFoundException {
		// Check coupon
		Coupon coupon;
		try {
			coupon = couponRep.findById(couponId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Coupon to purchase not found");
		}

		// Check title
		String title = coupon.getTitle();
		if (coupon.getAmount() <= 0) {
			throw new OutofStockException("Can't purchase Coupon " + title + " out of stock");
		}

		// Check customer
		Customer customer;
		try {
			customer = customerRep.findById(userId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Customer not found");
		}
		if (customer.couponExists(coupon)) {
			throw new UniqueValueException(
					"Can't purchase again coupon '" + title + "' for customer " + customer.getCustName());
		}

		// Check end date is a future or present date without time portion
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date todayWithZeroTime;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(new Date()));
		} catch (ParseException e) {
			throw new InvalidDateException("Can't purchase coupon " + title + " with invalid date");

		}
		if (coupon.getEndDate().compareTo(todayWithZeroTime) < 0) {
			throw new InvalidDateException("Can't purchase coupon " + title + " with expired date");
		}

		// Decrease amount by 1 and update entities
		coupon.setAmount(coupon.getAmount() - 1); // Decrement amount after purchase
		coupon.addCustomer(customer);
		customer.addCoupon(coupon);
	}

	/**
	 * Gets a list of all coupons in the system ordered by coupon type
	 * 
	 * @return A list of all coupons in the system
	 */
	@Override
	public List<Coupon> getAllCoupons() {
		return couponRep.findAllByOrderByCouponTypeAsc();
	}

	/**
	 * Gets a list of all coupons purchased by the customer
	 * 
	 * @param userId Id of user who is logged in
	 * @return A list of coupons owned by customer
	 */
	@Override
	public List<Coupon> getAllPurchasedCoupons(long userId) {
		return customerRep.findPurchasedCoupons(userId);
	}

	/**
	 * Gets a list of all coupons of purchased by customer by a specific type
	 * 
	 * @param couponType Type of coupon that was purchased
	 * @param userId     Id of user who is logged in
	 * @return Customer who purchased the coupons
	 */
	@Override
	public List<Coupon> getAllPurchasedCouponsbyType(CouponType couponType, long userId) {
		return customerRep.findPurchasedCouponsByType(userId, couponType);
	}

	/**
	 * Gets a list of all coupons of purchased by customer up to a certain price
	 * 
	 * @param price  Maximum price for coupons to be returned
	 * @param userId Id of user who is logged in
	 * @return A list of coupons for the customer up to a requested price
	 */
	@Override
	public List<Coupon> getAllPurchasedCouponsByPriceLessThan(double price, long userId) {
		return customerRep.findPurchasedCouponsByPriceLessThan(userId, price);
	}

	/**
	 * Gets current logged customer
	 * 
	 * @param userId Id of user who is logged in
	 * @return Customer Current logged customer
	 * @throws ValueNotFoundException When customer is not found
	 */
	@Override
	public Customer getCustomer(long userId) throws ValueNotFoundException {
		try {
			return customerRep.findById(userId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Customer not found");
		}
	}

}
