package coupons.CouponsSystemWeb.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import coupons.CouponsSystemWeb.entities.Coupon;

/**
 * Repository for coupon
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
	/**
	 * Checks if are there any coupons with specified title
	 * 
	 * @param title Title of coupon
	 * @return List of coupons with specified title
	 */
	List<Coupon> findByTitle(String title);

	/**
	 * Gets a list of all coupons in the system ordered by coupon type
	 * 
	 * @return List of coupons ordered by coupon type
	 */
	List<Coupon> findAllByOrderByCouponTypeAsc();

	/**
	 * 
	 * Gets a list of coupons with end date before specified and date
	 * 
	 * @param endDate End date of coupon
	 * @return List of coupons with end date before specified and date
	 */
	@Query("select c from Coupon c where c.endDate < ?1")
	List<Coupon> findByEndDateBefore(Date endDate);

}
