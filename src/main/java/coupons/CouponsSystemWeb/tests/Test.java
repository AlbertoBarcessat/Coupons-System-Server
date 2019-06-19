package coupons.CouponsSystemWeb.tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.CouponsSystemException;
import coupons.CouponsSystemWeb.exceptions.InvalidDateException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;
import coupons.CouponsSystemWeb.services.AdminService;
import coupons.CouponsSystemWeb.services.CompanyService;
import coupons.CouponsSystemWeb.services.CustomerService;
import coupons.CouponsSystemWeb.services.SharedServiceImplement;

/**
 * 
 * Create and run test for all services
 *
 */
@Component
public class Test {

	@Autowired
	SharedServiceImplement loginManager;

	@Autowired
	AdminService adminService;

	@Autowired
	CompanyService companyService;

	@Autowired
	CustomerService customerService;

	@Autowired
	Logger logger;

	/**
	 * Test Coupon System - tests can be run more than one time and they will create
	 * the same results (except incremented id numbers). Current test data is
	 * removed at the beginning, then the tests for the three client types are run.
	 */
	public void tests() {
		RemoveAllPresentData();
		CheckAdministrator();
		CheckCompany();
		CheckCustomer();
	}

	/**
	 * Remove all present data before running new tests
	 */
	private void RemoveAllPresentData() {
		logger.debug("*************** Removing Present Data ****************");
		try {
			adminService.login("admin", "1234", ClientType.ADMINISTRATOR);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		logger.debug("Logged as administrator");

		// Remove all companies, including coupons and customer purchases
		List<Company> companies = new ArrayList<>();
		companies = adminService.getAllCompanies();
		for (Company comp : companies) {
			try {
				adminService.removeCompany(comp.getCompanyId());
			} catch (ConstraintViolationException | CouponsSystemException e) {
				logger.debug(e.getMessage());
			}
		}
		logger.debug("Companies and coupons removed");

		// Remove all customers
		List<Customer> customers = new ArrayList<>();
		customers = adminService.getAllCustomers();
		for (Customer cust : customers) {
			try {
				adminService.removeCustomer(cust.getCustomerId());
			} catch (ConstraintViolationException | CouponsSystemException e) {
				logger.debug(e.getMessage());
			}
		}
		logger.debug("Customers removed");
	}

	/**
	 * Administrator checks
	 */
	private void CheckAdministrator() {
		logger.debug("\n\n*************** Checking Administrator ****************");
		// Login as administrator
		try {
			adminService.login("admin", "1234", ClientType.ADMINISTRATOR);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		logger.debug("Logged as administrator");

		// Check create new company
		logger.debug("\nCreate companies test - creating some companies");
		try {
			adminService.createCompany(new Company("Rishon Chocolates", "456789", "chocolates@gmail.com"));
			adminService.createCompany(new Company("Pizza Hut", "123456", "alber.barce@gmail.com"));
			adminService.createCompany(new Company("World Travel", "789789", "world.barce@gmail.com"));
			adminService.createCompany(new Company("Spa Services", "56887120", "spa.services@mail.com"));
			adminService.createCompany(new Company("Sports Coupons", "abcdser", "sports.coupons@sports.com"));
			adminService.createCompany(new Company("Market Online", "56887120", "market@online.com"));
			adminService.createCompany(new Company("Cakes For All", "we34b12", "cakes@forall.com"));
			adminService.createCompany(new Company("Falafel Discounts", "r54df123", "falafel@zol.com"));
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check get all companies
		logger.debug("\nGet all companies test:");
		List<Company> companies = new ArrayList<>();
		companies = adminService.getAllCompanies();
		printList(companies);

		// Check get company details
		logger.debug("\nGet company details test - first company:");
		Company comp = null;
		try {
			comp = adminService.getCompany(companies.get(0).getCompanyId());
			logger.debug(comp.toString());
		} catch (ValueNotFoundException e) {
			logger.debug(e.getMessage());
		}

		// Check company update - update email
		logger.debug("\nUpdate company test - update email to 555@org.il:");
		if (comp != null) {
			try {
				comp.setEmail("555@org.il");
				adminService.updateCompany(comp, comp.getCompanyId());
				logger.debug(adminService.getCompany(comp.getCompanyId()).toString());
			} catch (ConstraintViolationException | CouponsSystemException e) {
				logger.debug(e.getMessage());
			}
		}

		// Check remove of company
		logger.debug("\nRemove company test - remove first company and show remaning companies:");
		if (comp != null) {
			try {
				adminService.removeCompany(comp.getCompanyId());
			} catch (ConstraintViolationException | CouponsSystemException e) {
				logger.debug(e.getMessage());
			}
			companies = adminService.getAllCompanies();
			printList(companies);
		}

		// Check create new company as existing company and get error
		logger.debug("\nCreate company test - create a company with same name and get error:");
		try {
			adminService.createCompany(new Company("Pizza Hut", "777777", "aaa@org.il"));
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check create new company with invalid email and get error
		logger.debug("\nCreate company test - create a company with invalid email and get error:");
		try {
			adminService.createCompany(new Company("Samsung", "123456", "aaa.co.il"));
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check update company with invalid email and get error
		logger.debug("\nUpdate company test - update a company with invalid email and get error:");
		comp = companies.get(0);
		comp.setEmail("aaa.co.il");
		try {
			adminService.updateCompany(comp, comp.getCompanyId());
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check create new customer
		logger.debug("\nCreate customers test - creating some customers");
		try {
			adminService.createCustomer(new Customer("Limor Meir", "789000"));
			adminService.createCustomer(new Customer("Alberto", "456654"));
			adminService.createCustomer(new Customer("Yael Cohen", "012345"));
			adminService.createCustomer(new Customer("Linoy Levi", "321321"));
			adminService.createCustomer(new Customer("Shaked Moshe", "898989"));
			adminService.createCustomer(new Customer("Michal Goldberg", "asd543fd"));
			adminService.createCustomer(new Customer("Dima Bar Lev", "542dfgdfg"));
			adminService.createCustomer(new Customer("Tami Raviv", "85gtr567"));
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check get all customers
		logger.debug("\nGet all customers test:");
		List<Customer> customers = new ArrayList<>();
		customers = adminService.getAllCustomers();
		printList(customers);

		// Check get customer details
		logger.debug("\nGet customer details test - first customer:");
		Customer cust = null;
		try {
			cust = adminService.getCustomer(customers.get(0).getCustomerId());
			logger.debug(cust.toString());
		} catch (ValueNotFoundException e) {
			logger.debug(e.getMessage());
		}

		// Check update customer - update password
		logger.debug("\nUpdate customer test - update password to 111111:");
		if (cust != null) {
			try {
				cust.setPassword("111111");
				adminService.updateCustomer(cust, cust.getCustomerId());
				logger.debug(adminService.getCustomer(cust.getCustomerId()).toString());
			} catch (ConstraintViolationException | CouponsSystemException e) {
				logger.debug(e.getMessage());
			}
		}

		// Check remove customer
		logger.debug("\nRemove customer test - remove first customer and show remaning customers:");
		if (cust != null) {
			try {
				adminService.removeCustomer(cust.getCustomerId());
			} catch (ConstraintViolationException | CouponsSystemException e) {
				logger.debug(e.getMessage());
			}
			printList(adminService.getAllCustomers());
		}

		// Check create new customer as existing customer and get error
		logger.debug("\nCreate customers test - create a customer with same name and get error:");
		try {
			adminService.createCustomer(new Customer("Alberto", "888888"));
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * Company checks
	 */
	private void CheckCompany() {
		logger.debug("\n\n***************** Checking Company ******************");
		// Login as company
		long companyId = 0;
		try {
			companyId = companyService.login("Pizza Hut", "123456", ClientType.COMPANY);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check create some new coupons for company
		logger.debug("\nCreate coupons test - creating four coupons for first company");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			companyService.createCoupon(new Coupon("Family Pizza", "Big family pizza with all extras. "
					+ "Enter coupon code VISAFIVE to get $5 off an order of $25 or more when you pay with a Visa card."
					+ " Not good on special deals.  Enter the coupon code on the checkout page.  See screen capture below for where to enter the code.",
					"pizza_hut1.jpg", formatter.parse("2020-11-15"), 200, CouponType.FOOD, 50), companyId);
			companyService.createCoupon(new Coupon("Pizza and Pasta",
					"Best price for big pizza and all types of pasta. "
							+ "Find tasty options at savory prices and make tonight a pizza night when you order with Pizza Hut coupon codes. "
							+ "Pizza Hut offers pizza, wings and more and you can order online for delivery or in-store pickup.",
					"pizza_hut3.jpg", formatter.parse("2019-09-01"), 1, CouponType.FOOD, 59.9), companyId);
			companyService.createCoupon(new Coupon("Pizza with Cola",
					"1 Medium Cheese Stuffed Crust pizza + 1 Medium Beef & Cheddar Stuffed Crust + 1 liter Pepsi."
							+ " Get 2 Free items from Cinnaparts, Potato Wedges or Pepsi.This offer is not valid on: "
							+ "Cheese Lovers, Sea FOOD Lovers, Prawns. Offer valid all day Sunday to Thursday, excluding Bank Holidays."
							+ " 20 percent off is valid on full price menu items only. Valid only for Dine in at participating Pizza Hut Restaurants."
							+ " Excludes Buffet, Sunday Buffet, Kids Birthday Parties, Drinks, and all other vouchers, discounts and promotions. "
							+ " Valid for tables of up to 4 people. Products subject to availability. Photography for illustrative purposes only. "
							+ " Pizza Hut reserves the right to remove or amend this offer at any time."
							+ " A valid photographic Student ID card must be shown on payment\r\n",
					"pizza_hut2.jpg", formatter.parse("2019-12-01"), 220, CouponType.FOOD, 69.9), companyId);
			companyService.createCoupon(new Coupon("Pizza 1+1", "Two pizas for the price of one", "pizza_hut4.jpg",
					formatter.parse("2019-10-20"), 150, CouponType.FOOD, 29.9), companyId);
		} catch (ConstraintViolationException | CouponsSystemException | ParseException e) {
			logger.debug(e.getMessage());
		}

		// Check get coupons of company
		logger.debug("\nGet company coupons test for companyId: " + companyId);
		List<Coupon> coupons = new ArrayList<>();
		coupons = companyService.getAllCoupons(companyId);
		printList(coupons);

		// Check get current company details
		logger.debug("\nGet company details test - current logged company:");
		try {
			Company company = companyService.getCompany(companyId);
			logger.debug(company.toString());
		} catch (ValueNotFoundException e) {
			logger.debug(e.getMessage());
		}

		// Check coupon update - update price
		logger.debug("\nUpdate coupons test - update price to 40 NIS");
		coupons.get(0).setPrice(40);
		try {
			companyService.updateCoupon(coupons.get(0), coupons.get(0).getCouponId(), companyId);
		} catch (CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check get coupon details
		logger.debug("\nGet coupon details test - get first coupon:");
		try {
			logger.debug(companyService.getCoupon(coupons.get(0).getCouponId(), companyId).toString());
		} catch (ValueNotFoundException e) {
			logger.debug(e.getMessage());
		}

		// Check get coupons by price
		logger.debug("\nGet coupons up to a price test, up to 45 NIS:");
		printList(companyService.getCouponsByPriceLessThan(45, companyId));

		// Check get coupons by type
		logger.debug("\nGet coupons by type " + CouponType.FOOD + " test:");
		printList(companyService.getCouponsByType(CouponType.FOOD, companyId));

		// Check get coupons by date
		logger.debug("\nGet coupons up to end date 2019-09-25 test:");
		try {
			printList(companyService.getCouponsByBeforeEndDate("2019-09-25", companyId));
		} catch (InvalidDateException e) {
			logger.debug(e.getMessage());
		}

		// Check remove of coupon
		logger.debug("\nRemove coupon test - remove last coupon and show remaning company coupons:");
		try {
			companyService.removeCoupon(coupons.get(3).getCouponId(), companyId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		printList(companyService.getAllCoupons(companyId));

		// Check create coupon with same title and get error
		logger.debug("\nCreate coupons test - creating coupon with same title and get error:");
		try {
			companyService.createCoupon(new Coupon("Family Pizza", "Big family pizza with all extras and cola",
					"pizza_hut5.jpg", formatter.parse("2019-12-15"), 200, CouponType.FOOD, 80), companyId);
		} catch (ConstraintViolationException | CouponsSystemException | ParseException e) {
			logger.debug(e.getMessage());
		}

		// Check create coupon with invalid amount and get error
		logger.debug("\nCreate coupons test - creating coupon with  invalid amount and get error:");
		try {
			companyService.createCoupon(new Coupon("Mini Pizza", "Mini pizza with all extras and cola",
					"pizza_hut5.jpg", formatter.parse("2019-12-15"), 0, CouponType.FOOD, 80), companyId);
		} catch (ConstraintViolationException | CouponsSystemException | ParseException e) {
			logger.debug(e.getMessage());
		}

		// Check create coupon with passed end date and get error
		logger.debug("\nCreate coupons test - creating coupon with passed end date and get error:");
		try {
			companyService.createCoupon(new Coupon("Mini Pizza", "Mini pizza with all extras and cola",
					"pizza_hut5.jpg", formatter.parse("2018-01-01"), 200, CouponType.FOOD, 80), companyId);
		} catch (ConstraintViolationException | CouponsSystemException | ParseException e) {
			logger.debug(e.getMessage());
		}

		// Check create coupon with invalid price
		logger.debug("\nCreate coupons test - creating coupon with invalid price and get error:");
		try {
			companyService.createCoupon(new Coupon("Mini Pizza", "Mini pizza with all extras and cola",
					"pizza_hut5.jpg", formatter.parse("2019-12-15"), 200, CouponType.FOOD, -5), companyId);
		} catch (ConstraintViolationException | CouponsSystemException | ParseException e) {
			logger.debug(e.getMessage());
		}

		// Check coupon update - update end date to invalid date
		logger.debug("\nUpdate coupons test - update date to invalid date and get error:");
		Coupon coupon = coupons.get(2);
		try {
			coupon.setEndDate(formatter.parse("2017-02-01"));
		} catch (ParseException e) {
			logger.debug(e.getMessage());
		}
		try {
			companyService.updateCoupon(coupon, coupon.getCouponId(), companyId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check coupon update - update price to invalid price
		logger.debug("\nUpdate coupons test - update price to invalid price and get error:");
		// return date to valid value
		coupon.setPrice(-6);
		try {
			companyService.updateCoupon(coupon, coupon.getCouponId(), companyId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Create more coupons
		try {
			companyId = companyService.login("World Travel", "789789", ClientType.COMPANY);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check create some new coupons for company
		logger.debug("\nCreate coupons test - creating four coupons for last company");
		try {
			companyService.createCoupon(new Coupon("Bangkok to Phuket - 4 Days 3 Nights",
					"Gleaming temples, lively markets and opulent palaces meet lush jungles and tropical beaches on this unforgettable journey to Bangkok and Phuket."
							+ " Your adventure begins in Thailand’s exhilarating capital, with the opportunity to explore its colorful floating markets,"
							+ " gold-leafed shrines, and other historical highlights."
							+ " Next comes Phuket, with time to laze on the sandy beaches of this idyllic Thai island in the Andaman Sea."
							+ " Be sure to make your vacation extra special by adding on our optional tours and experiences:"
							+ " tailoring your time in Thailand around your personal wishes.",
					"thailand.jpg", formatter.parse("2019-10-15"), 130, CouponType.TRAVELLING, 570), companyId);
			companyService.createCoupon(new Coupon("China Tour - 6 Days 5 Nights",
					"Take a spellbinding tour around the wonders of China, on a 5 night odyssey to Beijing, Shanghai, Guilin and Xian."
							+ " Marvel at the iconic Great Wall in Beijing, gaze out over Xian’s vast Terracotta Army, and witness the dreamlike karst scenery of the Li River at Guilin"
							+ " – a serene landscape beloved in Chinese art history."
							+ " Finish in pulsating Shanghai, where cutting-edge towers, old temples and colonial architecture from the country’s most electrifying metropolis."
							+ " Enjoy the highest level of service at an unbeatable value. Your China adventure begins with World Travel."
							+ " According to the popularity of our China tours and the positive feed backs from our customers,"
							+ " China's top 10 tours are selected for you after our careful considerations. Major popular destinations and the essence of China."
							+ " Meets different people’s needs with the flexible and comfortable pace. A private tour is well designed in chronological order day by day.",
					"china.jpg", formatter.parse("2019-09-07"), 150, CouponType.TRAVELLING, 860), companyId);
			companyService.createCoupon(new Coupon("Japan Discovery - 5 Days 4 Nights",
					"Experience Japan's highlights and heartlands on this 5-day journey that encompasses its cutting-edge cities, ancient temples, serene countryside, and rich culture."
							+ " Absorb the bright lights of Tokyo, dive into the UNESCO-listed shrines of Kyoto and admire Mt Fuji, Japan's iconic peak."
							+ " Add an optional ride on the Bullet Train - an astonishing testament to the technological innovation of the country known as the Land of the Rising Sun.",
					"japan.jpg", formatter.parse("2019-12-01"), 220, CouponType.TRAVELLING, 950), companyId);
		} catch (ConstraintViolationException | CouponsSystemException | ParseException e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * Customer checks
	 */
	private void CheckCustomer() {
		logger.debug("\n\n***************** Checking Customer ******************");
		// Login as company
		long companyId = 0;
		try {
			companyId = companyService.login("Pizza Hut", "123456", ClientType.COMPANY);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		logger.debug("Logged as company user ID " + companyId);

		// Login as customer
		long customerId = 0;
		try {
			customerId = customerService.login("Alberto", "456654", ClientType.CUSTOMER);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		logger.debug("Logged as customer user ID " + customerId);

		// Check purchase of coupon
		logger.debug("\nPurchase coupon test - purchasing first two coupons");
		logger.debug(companyService.getAllCoupons(companyId).toString());
		try {
			customerService.purchaseCoupon((companyService.getAllCoupons(companyId)).get(0).getCouponId(), customerId);
			customerService.purchaseCoupon((companyService.getAllCoupons(companyId)).get(1).getCouponId(), customerId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check get all purchased coupons
		logger.debug("\nGet all purchased coupons test, check stock amount decrease by one:");
		List<Coupon> coupons = new ArrayList<>();
		coupons = customerService.getAllPurchasedCoupons(customerId);
		printList(coupons);

		// Check get all purchased coupons by type
		logger.debug("\nGet purchased coupons by type " + CouponType.FOOD + " test:");
		coupons = customerService.getAllPurchasedCouponsbyType(CouponType.FOOD, customerId);
		printList(coupons);

		// Check get all purchased coupons up to a certain price
		logger.debug("\nGet purchased coupons up to a price test, up to 55 NIS:");
		coupons = customerService.getAllPurchasedCouponsByPriceLessThan(55, customerId);
		printList(coupons);

		// Check purchase of same coupon again and get error
		logger.debug("\nPurchase same first coupon again and get error:");
		try {
			customerService.purchaseCoupon(coupons.get(0).getCouponId(), customerId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Check purchase of coupon out of stock by another customer and get error -
		// will try to purchase same coupon as first customer
		coupons = customerService.getAllPurchasedCoupons(customerId);
		try {
			customerId = customerService.login("Yael Cohen", "012345", ClientType.CUSTOMER);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		logger.debug("\nLogged as customer user ID " + customerId);
		logger.debug("\nPurchase coupon out of stock and get error:");
		try {
			customerService.purchaseCoupon(coupons.get(1).getCouponId(), customerId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Login as company
		try {
			companyId = companyService.login("World Travel", "789789", ClientType.COMPANY);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

		// Login as another customer
		try {
			customerId = customerService.login("Linoy Levi", "321321", ClientType.CUSTOMER);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		logger.debug("Logged as customer user ID " + customerId);

		// Check purchase of coupon again
		logger.debug("\nPurchase coupon test - purchasing three coupons");
		coupons = companyService.getAllCoupons(companyId);
		try {
			customerService.purchaseCoupon(coupons.get(0).getCouponId(), customerId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		try {
			customerService.purchaseCoupon(coupons.get(1).getCouponId(), customerId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}
		try {
			customerService.purchaseCoupon(coupons.get(2).getCouponId(), customerId);
		} catch (ConstraintViolationException | CouponsSystemException e) {
			logger.debug(e.getMessage());
		}

	}

	/**
	 * Print objects in list nicely
	 * 
	 * @param list List to print
	 */
	private void printList(List list) {
		if (list.size() == 1) {
			logger.debug(list.get(0).toString());
		} else {
			for (int i = 0; i < list.size(); i++) {
				logger.debug(i + 1 + ". " + list.get(i));
			}
		}
	}

}
