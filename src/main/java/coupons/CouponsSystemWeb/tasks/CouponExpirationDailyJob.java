package coupons.CouponsSystemWeb.tasks;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coupons.CouponsSystemWeb.services.AdminService;

/**
 * 
 * Job for deleting expired coupons in Coupon System
 *
 */
@Component
public class CouponExpirationDailyJob implements Runnable {

	@Autowired
	AdminService adminService;

	@Autowired
	Logger logger;

	private boolean quit; // If set to 'true', thread must stop

	/**
	 * Constructor for daily tasks. Initiates quit variable to false.
	 */
	public CouponExpirationDailyJob() {
		quit = false;
	}

	/**
	 * Runs daily tasks thread. Removes expired coupons once a day until quit
	 * variable is to to 'true'. Sleep time for one day may be interrupted to stop
	 * running.
	 */
	@Override
	public void run() {
		while (!quit) {
			logger.info("Deleting expired coupons...");
			adminService.removeExpiredCoupons();
			try {
				TimeUnit.DAYS.sleep(1); // Sleep for one day
			} catch (InterruptedException e) {
				logger.info("Stopped removing expired coupons");
				// Stops removing expired coupons
			}
		}
	}

	/**
	 * Stops running daily task by setting quit variable to 'true' and interrupting
	 * sleeping thread.
	 * @param thread dailyThread for deleting expired coupons
	 */
	public void stopTask(Thread thread) {
		quit = true;
		thread.interrupt();
	}

}
