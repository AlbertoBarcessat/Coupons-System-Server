package coupons.CouponsSystemWeb.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import coupons.CouponsSystemWeb.entities.CouponType;

/**
 * 
 * Service for implementing shared client operations
 *
 */
@Service
public class SharedServiceImplement implements SharedService {

	/**
	 * Gets list of all coupon types
	 * 
	 * @return A list of coupons types
	 */
	public List<CouponType> getCouponTypes() {
		List<CouponType> coupontypes = Arrays.asList(CouponType.values());
		return coupontypes;
	}

}
