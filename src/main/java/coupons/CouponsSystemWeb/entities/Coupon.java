package coupons.CouponsSystemWeb.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import coupons.CouponsSystemWeb.customValidations.FutureOrPresentDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Represents a coupon
 *
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long couponId;

	@NotNull(message = "Title cannot be null")
	@Size(min = 6, max = 40, message = "Title must be between 6 and 40 characters")
	@Column(nullable = false, unique = true, length = 40)
	private String title;

	@Temporal(TemporalType.DATE)
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date startDate;

	@FutureOrPresentDate
	@Temporal(TemporalType.DATE)
	@NotNull(message = "End date cannot be null")
	@Column(nullable = false)
	private Date endDate;

	@PositiveOrZero(message = "Amount must be positive or zero")
	@Column(nullable = false)
	private int amount;

	@NotNull(message = "Coupon type cannot be null")
	@Column(nullable = false, length = 40)
	@Enumerated(EnumType.STRING)
	private CouponType couponType;

	@NotNull(message = "Message cannot be null")
	@Size(min = 10, max = 1000, message = "Message must be between 10 and 1000 characters")
	@Column(nullable = false, length = 1000)
	private String message;

	@Positive(message = "Price must be positive")
	@Column(nullable = false)
	private double price;

	@NotNull(message = "Image cannot be null")
	@Size(min = 5, max = 40, message = "Image name must be between 5 and 40 characters")
	@Column(nullable = false, length = 40)
	private String image;

	@Valid
	@ManyToOne
	@JsonIgnore
	private Company company;

	@Valid
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coupons")
	@JsonIgnore
	private Collection<Customer> customers;

	/**
	 * Constructor for Coupon
	 * 
	 * @param title      The title of the coupon
	 * @param message    The message in the coupon
	 * @param image      The image of the coupon
	 * @param endDate    The expiration date of the coupon
	 * @param amount     The amount of coupons in stock
	 * @param couponType The couponType of the coupon
	 * @param price      The price of the coupon for customer
	 */
	public Coupon(String title, String message, String image, Date endDate, int amount, CouponType couponType,
			double price) {
		this.title = title;
		this.message = message;
		this.image = image;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = couponType;
		this.price = price;
	}

	/**
	 * Adds a customer to list of customers who purchased the coupon
	 * 
	 * @param customer Customer who purchased the coupon
	 */
	public void addCustomer(Customer customer) {
		customers.add(customer);
	}

	/**
	 * Removes a customer from list of customers who purchased the coupon
	 * 
	 * @param customer Customer to be removed from list
	 */
	public void removeCustomer(Customer customer) {
		customers.remove(customer);
	}

	@Override
	public String toString() {
		return "Coupon [couponId=" + couponId + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", couponType=" + couponType + ", message=" + message + ", price=" + price
				+ ", image=" + image + "]";
	}

}
