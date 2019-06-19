package coupons.CouponsSystemWeb.services;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.Company;
import coupons.CouponsSystemWeb.entities.Customer;
import coupons.CouponsSystemWeb.exceptions.InvalidIdException;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.exceptions.UniqueValueException;
import coupons.CouponsSystemWeb.exceptions.ValueNotFoundException;

/**
 * 
 * Service for implementing administrator operations
 *
 */
@Validated
public interface AdminService {

	public long login(@NotNull String name, @NotNull String password, @NotNull ClientType clientType)
			throws InvalidLoginException;

	public Company createCompany(@Valid Company company) throws UniqueValueException;

	public void removeCompany(@Positive long companyId) throws ValueNotFoundException;

	public void updateCompany(@Valid Company company, @Positive long companyId) throws ValueNotFoundException, InvalidIdException;

	public List<Company> getAllCompanies();

	public Company getCompany(@Positive long companyId) throws ValueNotFoundException;

	public Customer createCustomer(@Valid Customer customer) throws UniqueValueException;

	public void removeCustomer(@Positive long customerId) throws ValueNotFoundException;

	public void updateCustomer(@Valid Customer customer, @Positive long customerId) throws ValueNotFoundException, InvalidIdException;

	public List<Customer> getAllCustomers();

	public Customer getCustomer(@Positive long customerId) throws ValueNotFoundException;

	public void removeExpiredCoupons();

}
