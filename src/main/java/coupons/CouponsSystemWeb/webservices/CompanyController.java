package coupons.CouponsSystemWeb.webservices;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.exceptions.CouponsSystemException;
import coupons.CouponsSystemWeb.services.CompanyService;

/**
 * 
 * Rest Controller for company users
 *
 */
@RestController
@RequestMapping("/rest/company")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@Autowired
	SharedController sharedController;

	/**
	 * Creates a coupon
	 * 
	 * @param coupon  Coupon to be created
	 * @param session HttpSession
	 * @return Coupon created
	 * @throws CouponsSystemException If any error occurred
	 */
	@PostMapping(path = "coupon")
	public Coupon createCoupon(@RequestBody Coupon coupon, HttpSession session) throws CouponsSystemException {
		return companyService.createCoupon(coupon, sharedController.getUserId(session));
	}

	/**
	 * Removes a coupon
	 * 
	 * @param couponId Id of coupon to be removed
	 * @param session  HttpSession
	 * @throws CouponsSystemException If any error occurred
	 */
	@DeleteMapping(path = "coupon/{couponId}")
	public void removeCoupon(@PathVariable long couponId, HttpSession session) throws CouponsSystemException {
		companyService.removeCoupon(couponId, sharedController.getUserId(session));
	}

	/**
	 * Updates a coupon
	 * 
	 * @param couponId Id of coupon to be removed
	 * @param coupon   Coupon containing data to be updated
	 * @param session  HttpSession
	 * @throws CouponsSystemException If any error occurred
	 */
	@PutMapping(path = "coupon/{couponId}")
	public void updateCoupon(@PathVariable long couponId, @RequestBody Coupon coupon, HttpSession session)
			throws CouponsSystemException {
		coupon.setCouponId(couponId);
		companyService.updateCoupon(coupon, sharedController.getUserId(session));
	}

	/**
	 * Gets current company data
	 * 
	 * @param session HttpSession
	 * @return Company Current company
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "company")
	public Company getCompany(HttpSession session) throws CouponsSystemException {
		return companyService.getCompany(sharedController.getUserId(session));
	}

	/**
	 * Gets a coupon
	 * 
	 * @param couponId Id of coupon to be retrieved
	 * @param session  HttpSession
	 * @return Coupon required
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "coupon/{couponId}")
	public Coupon getCoupon(@PathVariable long couponId, HttpSession session) throws CouponsSystemException {
		return companyService.getCoupon(couponId, sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons owned by current company
	 * 
	 * @param session HttpSession
	 * @return List of coupons owned by company
	 */
	@GetMapping(path = "coupon")
	public List<Coupon> getAllCoupons(HttpSession session) {
		return companyService.getAllCoupons(sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons owned by current company by a specific type
	 * 
	 * @param couponType Type of coupon
	 * @param session    HttpSession
	 * @return List of coupons owned by company by a specific type
	 */
	@GetMapping(path = "couponsByType/{couponType}")
	public List<Coupon> getCouponsByType(@PathVariable("couponType") CouponType couponType, HttpSession session) {
		return companyService.getCouponsByType(couponType, sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons owned by current company up to a certain price
	 * 
	 * @param price   Maximum price for coupons
	 * @param session HttpSession
	 * @return List of coupons owned by company up to a certain price
	 */
	@GetMapping(path = "couponsByPrice/{price}")
	public List<Coupon> getCouponsbyPrice(@PathVariable("price") double price, HttpSession session) {
		return companyService.getCouponsByPriceLessThan(price, sharedController.getUserId(session));
	}

	/**
	 * Gets all coupons owned by current company up to a certain end date
	 * 
	 * @param endDate Maximum end date for coupons
	 * @param session HttpSession
	 * @return List of coupons owned by company up to a certain end date
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "couponsByEndDate/{endDate}")
	public List<Coupon> getCouponsByEndDate(@PathVariable("endDate") String endDate, HttpSession session)
			throws CouponsSystemException {
		return companyService.getCouponsByBeforeEndDate(endDate, sharedController.getUserId(session));
	}

}
