package coupons.CouponsSystemWeb.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Cross-Origin Resource Sharing (CORS) Filter
 */
@WebFilter("/*")
public class CORSFilter implements Filter {

	/**
	 * Cross-Origin Resource Sharing (CORS) Filter
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		// Allow requests from http://localhost:4200
		res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		// Allow Credentials
		res.setHeader("Access-Control-Allow-Credentials", "true");
		// As a part of the response to a request, which HTTP methods can be used during
		// the actual request.
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		// As part of the response to a request, which HTTP headers can be used during
		// the actual request.
		res.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-requested-with");

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
