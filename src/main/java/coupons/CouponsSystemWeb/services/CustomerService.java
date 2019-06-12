package coupons.CouponsSystemWeb.services;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.InvalidDateException;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.exceptions.OutofStockException;
import coupons.CouponsSystemWeb.exceptions.UniqueValueException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;

/**
 * 
 * Service for implementing customer client operations
 *
 */
@Validated
public interface CustomerService {

	long login(@NotNull String name, @NotNull String password, @NotNull ClientType clientType)
			throws InvalidLoginException;

	public void purchaseCoupon(@Positive long couponId, @Positive long userId)
			throws OutofStockException, UniqueValueException, InvalidDateException, ValueNotFoundException;

	public List<Coupon> getAllCoupons();

	public List<Coupon> getAllPurchasedCoupons(@Positive long userId);

	public List<Coupon> getAllPurchasedCouponsbyType(@NotNull CouponType couponType, @Positive long userId);

	public List<Coupon> getAllPurchasedCouponsByPriceLessThan(@Positive double price, @Positive long userId);

	public Customer getCustomer(@Positive long userId) throws ValueNotFoundException;

}
