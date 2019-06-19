package coupons.CouponsSystemWeb.services;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.exceptions.InvalidAmountException;
import coupons.CouponsSystemWeb.exceptions.InvalidDateException;
import coupons.CouponsSystemWeb.exceptions.InvalidIdException;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.exceptions.UnauthorizedException;
import coupons.CouponsSystemWeb.exceptions.UniqueValueException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;

/**
 * 
 * Service for implementing company client operations
 *
 */
@Validated
public interface CompanyService {

	public long login(@NotNull String name, @NotNull String password, @NotNull ClientType clientType)
			throws InvalidLoginException;

	public Coupon createCoupon(@Valid Coupon coupon, @Positive long userId)
			throws UniqueValueException, InvalidAmountException, ValueNotFoundException;

	public void removeCoupon(@Positive long couponId, @Positive long userId)
			throws ValueNotFoundException, UnauthorizedException;

	public void updateCoupon(@Valid Coupon coupon, @Positive long couponId, @Positive long userId)
			throws UniqueValueException, UnauthorizedException, InvalidIdException;

	public Company getCompany(@Positive long userId) throws ValueNotFoundException;

	public Coupon getCoupon(@Positive long couponId, @Positive long userId) throws ValueNotFoundException;

	public List<Coupon> getAllCoupons(@Positive long userId);

	public List<Coupon> getCouponsByType(@NotNull CouponType couponType, @Positive long userId);

	public List<Coupon> getCouponsByPriceLessThan(@Positive double price, @Positive long userId);

	public List<Coupon> getCouponsByBeforeEndDate(@NotNull String date, @Positive long userId)
			throws InvalidDateException;

}
