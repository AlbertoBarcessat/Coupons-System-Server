package coupons.CouponsSystemWeb.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Represents a company
 *
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long companyId;

	@NotNull(message = "Company name cannot be null")
	@Size(min = 6, max = 40, message = "Company name must be between 6 and 40 characters")
	@Column(nullable = false, unique = true, length = 40)
	private String companyName;

	@NotNull(message = "Password name cannot be null")
	@Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
	@Column(nullable = false, length = 40)
	private String password;

	@NotNull(message = "Email cannot be null")
	@Size(max = 40, message = "Email must be up to 40 characters")
	@Email(message = "Invalid email format")
	@Column(nullable = false, length = 40)
	private String email;

	@Valid
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.REMOVE)
	private Collection<Coupon> coupons;

	/**
	 * Constructor for Company
	 * 
	 * @param companyName The name of the company
	 * @param password    The password for login of company user
	 * @param email       The email of the company
	 */
	public Company(String companyName, String password, String email) {
		this.companyName = companyName;
		this.password = password;
		this.email = email;
		this.coupons = new ArrayList<>();
	}

	/**
	 * Adds a coupon to company
	 * 
	 * @param coupon Coupon to be added
	 */
	public void addCoupon(Coupon coupon) {
		coupons.add(coupon);
	}

	/**
	 * Removes a coupon from company
	 * 
	 * @param coupon Coupon to be removed
	 */
	public void removeCoupon(Coupon coupon) {
		coupons.remove(coupon);
	}

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", companyName=" + companyName + ", password=" + password
				+ ", email=" + email + "]";
	}

}
