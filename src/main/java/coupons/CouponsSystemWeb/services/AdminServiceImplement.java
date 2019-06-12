package coupons.CouponsSystemWeb.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Coupon;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.exceptions.UniqueValueException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;
import coupons.CouponsSystemWeb.repositories.CompanyRepository;
import coupons.CouponsSystemWeb.repositories.CouponRepository;
import coupons.CouponsSystemWeb.repositories.CustomerRepository;

/**
 * 
 * Service for implementing administrator operations
 *
 */
@Service
public class AdminServiceImplement implements AdminService {

	@Autowired
	private CompanyRepository companyRep;

	@Autowired
	private CustomerRepository customerRep;

	@Autowired
	private CouponRepository couponRep;

	@Autowired
	Logger logger;

	/**
	 * Login into system using name, password and client type
	 * 
	 * @param name       Name of client
	 * @param password   User password
	 * @param clientType Type of client - Should be 'Administrator'
	 * @return id is always 0 for administrator user
	 * @throws InvalidLoginException When credentials are invalid for administrator
	 */
	@Override
	public long login(String name, String password, ClientType clientType) throws InvalidLoginException {

		// check if user, password and client type are valid
		if (name.equals("admin") && password.equals("1234") && clientType.equals(ClientType.ADMINISTRATOR)) {
			return 0L;
		}
		throw new InvalidLoginException("Failed to login with invalid credentials for Administrator");
	}

	/**
	 * Creates a new company. Only one company with the same name can be created
	 * 
	 * @param company Company to be created
	 * @throws UniqueValueException When there is a company with same name
	 */
	@Override
	public Company createCompany(Company company) throws UniqueValueException {

		// Look for a company with same name
		String companyName = company.getCompanyName();
		List<Company> companies = companyRep.findByCompanyName(companyName);

		// Check that company doesn't exist and create it
		if (companies.isEmpty()) {
			companyRep.save(company);
			return company;
		}
		throw new UniqueValueException("Can't create duplicate company " + companyName);
	}

	/**
	 * Removes a company and all its coupons and purchases in Coupon System
	 * 
	 * @param companyId Id of company to be removed
	 * @throws ValueNotFoundException When company to be removed is not found
	 */
	@Override
	@Transactional
	public void removeCompany(long companyId) throws ValueNotFoundException {
		try {

			// Find company, then find its coupons and their customers and delete purchases
			Company company = companyRep.findById(companyId).get();
			Collection<Coupon> coupons = company.getCoupons();
			for (Coupon coupon : coupons) {
				Collection<Customer> customers = coupon.getCustomers();
				for (Customer customer : customers) {
					customer.removeCoupon(coupon);
					customerRep.save(customer);
				}
			}

			// Delete company
			companyRep.deleteById(companyId); // coupons are deleted automatically by cascade delete option
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Company to remove not found: " + companyId);
		}
	}

	/**
	 * Updates a company - password and email only
	 * 
	 * @param company Company including values to be updated
	 * @throws ValueNotFoundException When company to be updated is not found
	 */
	@Override
	public void updateCompany(Company company) throws ValueNotFoundException {
		try {
			Company existingCompany = companyRep.findById(company.getCompanyId()).get();
			existingCompany.setPassword(company.getPassword());
			existingCompany.setEmail(company.getEmail());
			companyRep.save(existingCompany);
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Company to update not found: " + company.getCompanyName());
		}
	}

	/**
	 * Gets a list of all companies in Coupon System
	 * 
	 * @return List of all companies
	 */
	@Override
	public List<Company> getAllCompanies() {
		return companyRep.findAll();
	}

	/**
	 * Gets a required company
	 * 
	 * @param companyId Id of company to be returned
	 * @return Company by Id
	 * @throws ValueNotFoundException When company is not found
	 */
	@Override
	public Company getCompany(long companyId) throws ValueNotFoundException {
		try {
			return companyRep.findById(companyId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Company not found");
		}
	}

	/**
	 * Creates a new customer. Only than one customer with the same name can be
	 * created
	 * 
	 * @param customer Customer to be created
	 * @throws UniqueValueException When there is a customer with same name
	 */
	@Override
	public Customer createCustomer(Customer customer) throws UniqueValueException {
		String customerName = customer.getCustName();
		List<Customer> customers = customerRep.findByCustName(customerName);
		if (customers.isEmpty()) {
			customerRep.save(customer);
			return customer;
		}
		throw new UniqueValueException("Can't create duplicate customer " + customerName);
	}

	/**
	 * Removes a customer and all its purchases in Coupon System
	 * 
	 * @param customerId Id of customer to be removed
	 * @throws ValueNotFoundException When customer to be removed is not found
	 */
	@Override
	@Transactional
	public void removeCustomer(long customerId) throws ValueNotFoundException {
		try {

			// Find customer and remove his purchased coupons
			Customer customer = customerRep.findById(customerId).get();
			Collection<Coupon> coupons = customer.getCoupons();
			for (Coupon coupon : coupons) {
				coupon.removeCustomer(customer);
				couponRep.save(coupon);
			}

			// Delete customer
			customerRep.deleteById(customerId);
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Customer to remove not found: " + customerId);
		}
	}

	/**
	 * Updates a customer - password only
	 * 
	 * @param customer Customer including values to be updated
	 * @throws ValueNotFoundException When customer to be removed is not found
	 */
	@Override
	public void updateCustomer(Customer customer) throws ValueNotFoundException {
		try {
			Customer existingCustomer = customerRep.findById(customer.getCustomerId()).get();
			existingCustomer.setPassword(customer.getPassword());
			customerRep.save(existingCustomer);
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Customer to update not found: " + customer.getCustName());
		}
	}

	/**
	 * Gets all customers in Coupon System
	 * 
	 * @return List of all customers
	 */
	@Override
	public List<Customer> getAllCustomers() {
		return customerRep.findAll();
	}

	/**
	 * Gets a customer by Id
	 * 
	 * @param customerId Id of customer to be returned
	 * @return Customer required
	 * @throws ValueNotFoundException When customer is not found
	 */
	@Override
	public Customer getCustomer(long customerId) throws ValueNotFoundException {
		try {
			return customerRep.findById(customerId).get();
		} catch (NoSuchElementException e) {
			throw new ValueNotFoundException("Customer not found");
		}
	}

	/**
	 * Remove coupons with expired end date, including their links with companies
	 * and customers. All the relations with customers an companies are also deleted
	 */
	@Override
	@Transactional
	public void removeExpiredCoupons() {
		try {
			// Find current date without time portion
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date todayWithZeroTime;
			todayWithZeroTime = formatter.parse(formatter.format(new Date()));

			// Find coupons expired
			List<Coupon> coupons = couponRep.findByEndDateBefore(todayWithZeroTime);

			for (Coupon coupon : coupons) {

				// Remove coupon from company
				Company company = coupon.getCompany();
				company.removeCoupon(coupon);
				companyRep.save(company);

				// Remove coupon from customers who purchased it
				Collection<Customer> customers = coupon.getCustomers();
				for (Customer customer : customers) {
					customer.removeCoupon(coupon);
					customerRep.save(customer);
				}

				// Remove coupon
				couponRep.deleteById(coupon.getCouponId());
			}
		} catch (ParseException e) {
			logger.error("Internal error while removing expired coupons, Invalid date");
		}
	}

}
