package coupons.CouponsSystemWeb.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import coupons.CouponsSystemWeb.CouponsSystemWebApplication;

/**
 * 
 * Logger configuration
 *
 */
@Configuration
public class LoggerConfiguration {

	/**
	 * Gets Logger
	 * 
	 * @return Logger for application
	 */
	@Bean
	public Logger logger() {
		return LoggerFactory.getLogger(CouponsSystemWebApplication.class);
	}

}
