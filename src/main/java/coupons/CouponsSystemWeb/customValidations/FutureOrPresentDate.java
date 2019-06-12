package coupons.CouponsSystemWeb.customValidations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * Annotation for checking that date is future or present using
 * FutureOrPresentDateValidator
 *
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureOrPresentDateValidator.class)
public @interface FutureOrPresentDate {

	String message() default "Must be a future or present date";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
