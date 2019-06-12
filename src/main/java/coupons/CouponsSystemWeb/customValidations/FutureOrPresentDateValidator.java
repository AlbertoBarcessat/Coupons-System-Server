package coupons.CouponsSystemWeb.customValidations;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a date is valid for validator FutureOrPresentDate
 */
public class FutureOrPresentDateValidator implements ConstraintValidator<FutureOrPresentDate, Date> {

	/**
	 * Checks that a date is a future or present date without the time portion, for
	 * validator FutureOrPresentDate
	 */
	@Override
	public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date todayWithZeroTime = formatter.parse(formatter.format(new Date()));
			if (date == null)
				return true; // null is valid
			return (date.compareTo(todayWithZeroTime) >= 0);
		} catch (ParseException e) {
			return false;
		}

	}

}
