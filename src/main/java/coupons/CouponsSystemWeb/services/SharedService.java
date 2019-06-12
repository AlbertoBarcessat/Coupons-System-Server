package coupons.CouponsSystemWeb.services;

import java.util.List;

import coupons.CouponsSystemWeb.entities.CouponType;

/**
 * 
 * Service for shared client operations
 *
 */
public interface SharedService {

	public List<CouponType> getCouponTypes();

}
