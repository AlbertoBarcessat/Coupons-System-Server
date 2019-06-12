package coupons.CouponsSystemWeb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.entities.Customer;

/**
 * Repository for customer
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * Checks if there is a customer with specified name and password
	 * 
	 * @param custName Name of customer
	 * @param password Password of customer
	 * @return List of customers for name and password
	 */
	List<Customer> findByCustNameAndPassword(String custName, String password);

	/**
	 * Checks if there is a customer with specified name
	 * 
	 * @param custName Name of customer
	 * @return List of customers for name
	 */
	List<Customer> findByCustName(String custName);

	/**
	 * Returns all coupons purchased by customer
	 * 
	 * @param customerId Id of customer who purchased the coupons
	 * @return List of coupons for customer
	 */
	@Query("select c from Coupon c join c.customers cust where cust.customerId = ?1")
	List<Coupon> findPurchasedCoupons(long customerId);

	/**
	 * Returns all coupons purchased by customer by specific type
	 * 
	 * @param customerId Id of customer who purchased the coupons
	 * @param couponType Type of coupon purchased
	 * @return List of coupons for customer by specific type
	 */
	@Query("select c from Coupon c join c.customers cust where cust.customerId = ?1 and c.couponType = ?2")
	List<Coupon> findPurchasedCouponsByType(long customerId, CouponType couponType);

	/**
	 * Returns all coupons purchased by customer with price less than a certain
	 * price
	 * 
	 * @param customerId Id of customer who purchased the coupons
	 * @param price      Price of coupon purchased
	 * @return List of coupons for customer by price less than a certain price
	 */
	@Query("select c from Coupon c join c.customers cust where cust.customerId = ?1 and c.price <= ?2")
	List<Coupon> findPurchasedCouponsByPriceLessThan(long customerId, double price);

}
