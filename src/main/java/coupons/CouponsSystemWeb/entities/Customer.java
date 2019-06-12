package coupons.CouponsSystemWeb.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Represents a customer
 *
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerId;

	@NotNull(message = "Customer name cannot be null")
	@Size(min = 6, max = 40, message = "Customer name must be between 6 and 40 characters")
	@Column(nullable = false, unique = true, length = 40)
	private String custName;

	@NotNull(message = "Password cannot be null")
	@Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
	@Column(nullable = false, length = 40)
	private String password;

	@Valid
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Coupon> coupons;

	/**
	 * Constructor for Customer
	 * 
	 * @param custName The name of the customer
	 * @param password Customer user password
	 */
	public Customer(String custName, String password) {
		this.custName = custName;
		this.password = password;
		this.coupons = new ArrayList<>();
	}

	/**
	 * Add a purchased coupon to customer
	 * 
	 * @param coupon Coupon purchased by customer
	 */
	public void addCoupon(Coupon coupon) {
		this.coupons.add(coupon);
	}

	/**
	 * Removes a purchased coupon from customer
	 * 
	 * @param coupon Coupon removed from customer
	 */
	public void removeCoupon(Coupon coupon) {
		this.coupons.remove(coupon);
	}

	/**
	 * Checks if a coupon was purchased by customer
	 * 
	 * @param coupon Coupon to check if exists
	 * @return True if coupon was purchased by customer, otherwise return false
	 */
	public boolean couponExists(Coupon coupon) {
		return this.coupons.contains(coupon);
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", custName=" + custName + ", password=" + password + "]";
	}

}
