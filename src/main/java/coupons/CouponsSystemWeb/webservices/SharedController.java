package coupons.CouponsSystemWeb.webservices;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupons.CouponsSystemWeb.entities.CouponType;
import coupons.CouponsSystemWeb.services.SharedServiceImplement;

/**
 * Rest Controller with shared operations for all clients
 */
@RestController
@RequestMapping("/rest/shared")
public class SharedController {

	@Autowired
	SharedServiceImplement sharedService;

	/**
	 * Gets all coupon types in system
	 * 
	 * @return coupon types in system
	 */
	@GetMapping(path = "couponTypes")
	public List<CouponType> getCouponTypes() {
		return sharedService.getCouponTypes();
	}

	/**
	 * Gets Id of user from session attribute
	 * 
	 * @param session HttpSession
	 * @return Id of user
	 */
	public long getUserId(HttpSession session) {
		return (long) session.getAttribute("userId");
	}

}
