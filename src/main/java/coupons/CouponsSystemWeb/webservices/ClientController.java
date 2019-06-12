package coupons.CouponsSystemWeb.webservices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupons.CouponsSystemWeb.entities.ClientType;
import coupons.CouponsSystemWeb.entities.User;
import coupons.CouponsSystemWeb.exceptions.InvalidLoginException;
import coupons.CouponsSystemWeb.services.AdminService;
import coupons.CouponsSystemWeb.services.CompanyService;
import coupons.CouponsSystemWeb.services.CustomerService;

/**
 * Rest Controller for login and logout of all clients
 */
@RestController
@RequestMapping("/user")
public class ClientController {

	@Autowired
	AdminService adminService;

	@Autowired
	CompanyService companyService;

	@Autowired
	CustomerService customerService;

	/**
	 * Login into system
	 * 
	 * @param user    User who tries to login
	 * @param request HttpServletRequest
	 * @return Id of user
	 * @throws InvalidLoginException When credentials are invalid for user
	 */
	@PostMapping(path = "login")
	public long login(@RequestBody User user, HttpServletRequest request) throws InvalidLoginException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalidates previous session, if existed
		}
		session = request.getSession(true); // Creates a new session
		long userId;
		String name = user.getName();
		String password = user.getPassword();
		ClientType clientType = user.getClientType();
		try {
			switch (clientType) {
			case ADMINISTRATOR:
				userId = adminService.login(name, password, clientType);
				break;
			case COMPANY:
				userId = companyService.login(name, password, clientType);
				break;
			case CUSTOMER:
				userId = customerService.login(name, password, clientType);
				break;
			default:
				throw new InvalidLoginException("Fail to login with invalid client type");
			}
		} catch (InvalidLoginException e) {
			throw new InvalidLoginException(e.getMessage());
		}
		session.setAttribute("clientType", clientType); // save clientType in session attribute
		session.setAttribute("userId", userId); // save user Id in session attribute
		return userId;
	}

	/**
	 * Logs out from system by invalidating session
	 * 
	 * @param request HttpServletRequest
	 */
	@GetMapping(path = "logout")
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

}
