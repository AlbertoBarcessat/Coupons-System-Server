package coupons.CouponsSystemWeb.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;

/**
 * Repository for company
 *
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	/**
	 * Checks if there is a company with specified name and password
	 * 
	 * @param companyName Name of company
	 * @param password    Password of company
	 * @return List of companies for name and password
	 */
	List<Company> findByCompanyNameAndPassword(String companyName, String password);

	/**
	 * Checks if there is a company with specified name
	 * 
	 * @param companyName Name of company
	 * @return List of companies for name
	 */
	List<Company> findByCompanyName(String companyName);

	/**
	 * Returns all coupons owned by a company
	 * 
	 * @param companyId Id of company owner of coupons
	 * @return List of coupons owned by company
	 */
	@Query("select c from Coupon c join c.company comp where comp.companyId = ?1")
	List<Coupon> findAllCoupons(long companyId);

	/**
	 * Returns all coupons for company with specified coupon type
	 * 
	 * @param companyId  Id of company owner of coupons
	 * @param couponType Type of coupon
	 * @return List of coupons owned by company
	 */
	@Query("select c from Coupon c join c.company comp where comp.companyId = ?1 and c.couponType = ?2")
	List<Coupon> findCouponsByType(long companyId, CouponType couponType);

	/**
	 * Returns all coupons for company up to a specified price
	 * 
	 * @param companyId Id of company owner of coupons
	 * @param price     Maximum price for coupon
	 * @return List of coupons owned by company
	 */
	@Query("select c from Coupon c join c.company comp where comp.companyId = ?1 and c.price <= ?2")
	List<Coupon> findCouponsByPriceLessThan(long companyId, double price);

	/**
	 * Returns all coupons for company up to a specified end date
	 * 
	 * @param companyId Id of company owner of coupons
	 * @param endDate   Maximum end date for coupon
	 * @return List of coupons owned by company
	 */
	@Query("select c from Coupon c join c.company comp where comp.companyId = ?1 and c.endDate <= ?2")
	List<Coupon> findCouponsByBeforeEndDate(long companyId, Date endDate);
}
