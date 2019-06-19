package coupons.CouponsSystemWeb.webservices;

import java.util.List;

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
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.CouponsSystemException;
import coupons.CouponsSystemWeb.services.AdminService;

/**
 * 
 * Rest Controller for administrator users
 */
@RestController
@RequestMapping("/rest/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	/**
	 * Creates a new company
	 * 
	 * @param company Company to be created
	 * @return Company Company created
	 * @throws CouponsSystemException If any error occurred
	 */
	@PostMapping(path = "company")
	public Company createCompany(@RequestBody Company company) throws CouponsSystemException {
		return adminService.createCompany(company);
	}

	/**
	 * Removes a company from system
	 * 
	 * @param companyId Id of company
	 * @throws CouponsSystemException If any error occurred
	 */
	@DeleteMapping(path = "company/{companyId}")
	public void removeCompany(@PathVariable long companyId) throws CouponsSystemException {
		adminService.removeCompany(companyId);
	}

	/**
	 * Updates a company
	 * 
	 * @param companyId Id of company to be updated
	 * @param company Company with data to update

	 * @throws CouponsSystemException If any error occurred
	 */
	@PutMapping(path = "company/{companyId}")
	public void updateCompany(@PathVariable long companyId, @RequestBody Company company)
			throws CouponsSystemException {
		adminService.updateCompany(company, companyId);
	}

	/**
	 * Gets all companies in system
	 * 
	 * @return List of companies
	 */
	@GetMapping(path = "company")
	public List<Company> getAllCompanies() {
		return adminService.getAllCompanies();
	}

	/**
	 * Gets a company
	 * 
	 * @param companyId Id of company
	 * @return Company Company requested
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "company/{companyId}")
	public Company getCompany(@PathVariable long companyId) throws CouponsSystemException {
		return adminService.getCompany(companyId);
	}

	/**
	 * Creates a new customer
	 * 
	 * @param customer Customer to be created
	 * @return Customer Customer created
	 * @throws CouponsSystemException If any error occurred
	 */
	@PostMapping(path = "customer")
	public Customer createCustomer(@RequestBody Customer customer) throws CouponsSystemException {
		return adminService.createCustomer(customer);
	}

	/**
	 * Removes a customer from system
	 * 
	 * @param customerId Id of customer
	 * @throws CouponsSystemException If any error occurred
	 */
	@DeleteMapping(path = "customer/{customerId}")
	public void removeCustomer(@PathVariable long customerId) throws CouponsSystemException {
		adminService.removeCustomer(customerId);
	}

	/**
	 * Updates a customer
	 * 
	 * @param customerId Id of customer
	 * @param customer   Customer containing data to be updated
	 * @throws CouponsSystemException If any error occurred
	 */
	@PutMapping(path = "customer/{customerId}")
	public void updateCustomer(@PathVariable long customerId, @RequestBody Customer customer)
			throws CouponsSystemException {
		adminService.updateCustomer(customer, customerId);
	}

	/**
	 * Gets all customers in system
	 * 
	 * @return List of customers
	 */
	@GetMapping(path = "customer")
	public List<Customer> getAllCustomers() {
		return adminService.getAllCustomers();
	}

	/**
	 * Gets a customer
	 * 
	 * @param customerId Id of customer
	 * @return Customer Customer retrieved
	 * @throws CouponsSystemException If any error occurred
	 */
	@GetMapping(path = "customer/{customerId}")
	public Customer getCustomer(@PathVariable long customerId) throws CouponsSystemException {
		return adminService.getCustomer(customerId);
	}

}
