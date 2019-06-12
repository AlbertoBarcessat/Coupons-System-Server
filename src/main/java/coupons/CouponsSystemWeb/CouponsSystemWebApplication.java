package coupons.CouponsSystemWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import coupons.CouponsSystemWeb.tasks.CouponExpirationDailyJob;
import coupons.CouponsSystemWeb.tests.Test;

/**
 * Coupons System 
 * @author Alberto Barcessat
 *
 */
@SpringBootApplication
@ServletComponentScan // Enable filters
public class CouponsSystemWebApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CouponsSystemWebApplication.class, args);

		// Run coupon expiration daily job
		CouponExpirationDailyJob couponExpirationDailyJob = (CouponExpirationDailyJob) context
				.getBean("couponExpirationDailyJob");
		Thread dailyThread = new Thread(couponExpirationDailyJob, "couponExpirationDailyJob");
		dailyThread.start();

		// Run tests
		Test test = (Test) context.getBean("test");
		test.tests();

		// Stop daily job
		couponExpirationDailyJob.stopTask(dailyThread);
	}

}
