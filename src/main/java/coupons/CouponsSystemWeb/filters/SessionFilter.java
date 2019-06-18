package coupons.CouponsSystemWeb.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import coupons.CouponsSystemWeb.entities.ClientType;

@WebFilter("/rest/*")
public class SessionFilter implements Filter {
	
	@Autowired
	Logger logger;

	/**
	 * Filters all user HTTP requests by checking that: 1. session is not expired,
	 * 2. user is logged in, 3. user client type is correct for operation. Returns
	 * error if one of these conditions is not met
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Checks if there is a valid (not expired) session, if not: returns error -
		// session expired
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession(false);
		if (session == null) { // session expired
			try {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_REQUEST_TIMEOUT,
						"Please login in order to continue");
				return;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		// Checks if user is logged in by checking that clientType is not null (value is
		// set when user logs in)
		// If not: returns error - user must login
		ClientType clientType = (ClientType) session.getAttribute("clientType");
		if (clientType == null) { // not logged in
			try {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION,
						"Please login before entering the system");
				return;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		// Checks if client type matches url path for specific client type, if not:
		// returns error - user not authorized.
		// Shared url is ok for all client types
		String URI = httpServletRequest.getRequestURI();
		if (((clientType == ClientType.ADMINISTRATOR && !URI.contains("/rest/admin"))
				|| (clientType == ClientType.COMPANY && !URI.contains("/rest/company"))
				|| (clientType == ClientType.CUSTOMER && !URI.contains("/rest/customer")))
				&& !URI.contains("/rest/shared")) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authorized");
			return;
		}

		chain.doFilter(request, response);
	}

}
