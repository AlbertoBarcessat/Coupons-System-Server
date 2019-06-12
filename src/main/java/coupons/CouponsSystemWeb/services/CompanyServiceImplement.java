package coupons.CouponsSystemWeb.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.InvalidAmountException;
import coupons.CouponsSystemWeb.exceptions.InvalidDateException;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.exceptions.UnauthorizedException;
import coupons.CouponsSystemWeb.exceptions.UniqueValueException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;
import coupons.CouponsSystemWeb.repositories.CompanyRepository;
import coupons.CouponsSystemWeb.repositories.CouponRepository;
import coupons.CouponsSystemWeb.repositories.CustomerRepository;

/**
 * 
 * Service for implementing company client operations
 *
 */
@Service
public class CompanyServiceImplement implements CompanyService {

	@Autowired
	private CompanyRepository companyRep;

	@Autowired
	private CouponRepository couponRep;

	@Autowired
	private CustomerRepository customerRep;

	/**
	 * Login into system using name, password and client type
	 * 
	 * @param name       Name of client
	 * @param password   User password
	 * @param clientType Type of client - Should be 'Company'
	 * @return id Id of company user
	 * @throws InvalidLoginException When credentials are invalid for company user
	 */
	@Override
	public long login(String name, String password, ClientType clientType) throws InvalidLoginException {
		// Check client is a company
		if (!clientType.equals(ClientType.COMPANY)) {
			throw new InvalidLoginException("Failed to login with invalid company client type");
		}

		// Find company
		List<Company> companies = companyRep.findByCompanyNameAndPassword(name, password);
		if (companies.size() != 1) {
			throw new InvalidLoginException("Failed to login with invalid credentials for company " + name);
		}

		// Return found company
		return companies.get(0).getCompanyId();
	}

	/**
	 * Creates a new coupon for company. Only than one coupon with the same name can
	 * be created. Coupon should be created with positive amount in stock, with end
	 * date not passed and with positive price
	 * 
	 * @param coupon Coupon to be created
	 * @param userId Id of user who is logged in
	 * @throws UniqueValueException   When there is a coupon in system with same
	 *                                title
	 * @throws InvalidAmountException When amount is not positive
	 * @throws ValueNotFoundException When company is not found
	 */
	@Override
	@Transactional
	public Coupon createCoupon(Coupon coupon, long userId)
			throws UniqueValueException, InvalidAmountException, ValueNotFoundException {

		// Check amount is positive
		if (coupon.getAmount() <= 0) {
			throw new InvalidAmountException("Can't create coupon with non positive amount");
		}

		// Add coupon to company
		String title = coupon.getTitle();
		List<Coupon> coupons = couponRep.findByTitle(title);
		if (coupons.isEmpty()) {
			Company company;
			try {
				company = companyRep.findById(userId).get();
			} catch (NoSuchElementException e) {
				throw new ValueNotFoundException("Company not found");
			}
			coupon.setCompany(company);
			company.addCoupon(coupon);
			couponRep.save(coupon);
			companyRep.save(company);
			return coupon;
		}
		throw new UniqueValueException("Can't create duplicate coupon " + title);
	}

	/**
	 * Removes a coupon from Coupon System, including its purchases by customers
	 * 
	 * @param couponId Id of coupon to be removed
	 * @param userId   Id of user who is logged in
	 * @throws ValueNotFoundException When coupon was not found
	 * @throws UnauthorizedException  When user is not owner of coupon
	 */
	@Override
	@Transactional
	public void removeCoupon(long couponId, long userId) throws ValueNotFoundException, UnauthorizedException {
		try {
			// Find coupon, remove it from company, from customers and finally delete it
			Coupon coupon = couponRep.findById(couponId).get();
			if (coupon.getCompany().getCompanyId() == userId) {
				Collection<Customer> customers = coupon.getCustomers();
				for (Customer customer : customers) {
					customer.removeCoupon(coupon);
					customerRep.save(customer);
				}
				Company company = companyRep.findById(userId).get();
				company.removeCoupon(coupon);
				companyRep.save(company);
				couponRep.deleteById(couponId);
			} else {
				throw new UnauthorizedException("Can't remove coupon for invalid coupon owner");
			}
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Coupon to remove for company not found: " + couponId);
		}
	}

	/**
	 * Updates price and end date for coupon. Coupon should be updated with end date
	 * not passed and with positive price
	 * 
	 * @param coupon Coupon including values to be updated
	 * @param userId Id of user who is logged in
	 * @throws UniqueValueException  When there is a coupon in system with same
	 *                               title
	 * @throws UnauthorizedException When company is not owner of coupon
	 */
	@Override
	public void updateCoupon(Coupon coupon, long userId) throws UniqueValueException, UnauthorizedException {
		try {
			Coupon existingCoupon = couponRep.findById(coupon.getCouponId()).get();
			if (existingCoupon.getCompany().getCompanyId() == userId) {
				existingCoupon.setPrice(coupon.getPrice());
				existingCoupon.setEndDate(coupon.getEndDate());
				couponRep.save(existingCoupon);
			} else {
				throw new UnauthorizedException("Can't update coupon for invalid coupon owner");
			}
		} catch (NoSuchElementException e) {
			throw new UniqueValueException("Coupon to update not found: " + coupon.getTitle());
		}
	}

	/**
	 * Gets currently logged company
	 * 
	 * @param userId Id of user who is logged in
	 * @return Company required
	 * @throws ValueNotFoundException When company is not found
	 */
	@Override
	public Company getCompany(long userId) throws ValueNotFoundException {
		try {
			return companyRep.findById(userId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Company not found");
		}
	}

	/**
	 * Gets a coupon by Id
	 * 
	 * @param couponId Id of the desired coupon
	 * @param userId   Id of user who is logged in
	 * @return Coupon required
	 * @throws ValueNotFoundException When coupon is not found
	 */
	@Override
	public Coupon getCoupon(long couponId, long userId) throws ValueNotFoundException {
		try {
			return couponRep.findById(couponId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Coupon not found");
		}
	}

	/**
	 * Gets coupons owned by current logged company
	 * 
	 * @param userId Id of user who is logged in
	 * @return List of coupons owned by current logged company
	 */
	@Override
	public List<Coupon> getAllCoupons(long userId) {
		return companyRep.findAllCoupons(userId);
	}

	/**
	 * Gets a list of all coupons of owned by company by a specific type
	 * 
	 * @param couponType Type of coupons to be returned
	 * @param userId     Id of user who is logged in
	 * @return A list of coupons for the company with requested type
	 */
	@Override
	public List<Coupon> getCouponsByType(CouponType couponType, long userId) {
		return companyRep.findCouponsByType(userId, couponType);
	}

	/**
	 * Gets a list of all coupons of owned by company up to a certain price
	 * 
	 * @param price  Maximum price for coupons to be returned
	 * @param userId Id of user who is logged in
	 * @return A list of coupons for the company up to a requested price
	 */
	@Override
	public List<Coupon> getCouponsByPriceLessThan(double price, long userId) {
		return companyRep.findCouponsByPriceLessThan(userId, price);
	}

	/**
	 * Gets a list of all coupons of owned by company up to a certain end date
	 * 
	 * @param endDate Maximum end date for coupons to be returned
	 * @param userId  Id of user who is logged in
	 * @return A list of coupons for the company up to a requested end date
	 * @throws InvalidDateException When it's an invalid date format for end date
	 */
	@Override
	public List<Coupon> getCouponsByBeforeEndDate(String endDate, long userId) throws InvalidDateException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return companyRep.findCouponsByBeforeEndDate(userId, formatter.parse(endDate));
		} catch (ParseException e) {
			throw new InvalidDateException("Invalid date format for end date");
		}
	}

}
