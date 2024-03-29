package coupons.CouponsSystemWeb.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import coupons.CouponsSystemWeb.entities.WebApiError;

/**
 * Handles exceptions in system
 *
 */
@ControllerAdvice
public class CustomExceptionHandler {

	@Autowired
	Logger logger;

	/**
	 * Handles validation exceptions
	 * 
	 * @param e Exception to be handled
	 * @return ResponseEntity ResponseEntity containing error information
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {

		List<String> errorMessages = new ArrayList<>();
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			errorMessages.add("Invalid value " + violation.getInvalidValue() + " for property "
					+ violation.getPropertyPath() + " : " + violation.getMessage());
		}
		WebApiError apiError = new WebApiError(errorMessages.size() + " violation errors occurred. ", errorMessages);
		return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles general exceptions
	 * 
	 * @param t Throwable to be handled
	 * @return ResponseEntity ResponseEntity containing error information
	 */
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleThrowable(Throwable t) {
		logger.error(t.getMessage());
		WebApiError apiError = new WebApiError("Something wrong happened... Please contact the admin. ", null);
		return new ResponseEntity<Object>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles application exceptions generated by Coupon system
	 * 
	 * @param e CouponsSystemException to be handled
	 * @return ResponseEntity ResponseEntity containing error information
	 */
	@ExceptionHandler(CouponsSystemException.class)
	public ResponseEntity<Object> handleCouponSystemException(CouponsSystemException e) {
		WebApiError apiError = new WebApiError(e.getMessage(), null);
		return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles application invalid login exception
	 * 
	 * @param e InvalidLoginException to be handled
	 * @return ResponseEntity ResponseEntity containing error information
	 */
	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<Object> handleCouponSystemException(InvalidLoginException e) {
		WebApiError apiError = new WebApiError(e.getMessage(), null);
		return new ResponseEntity<Object>(apiError, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles exceptions when no other handler was found
	 * 
	 * @param notFoundException NoHandlerFoundException to be handled
	 * @return ResponseEntity ResponseEntity containing error information
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handleResourceNotFound(NoHandlerFoundException notFoundException) {
		logger.error(notFoundException.getMessage());
		WebApiError apiError = new WebApiError(
				"Could not found " + notFoundException.getRequestURL() + " (" + notFoundException.getHttpMethod() + ")",
				null);
		return new ResponseEntity<Object>(apiError, HttpStatus.NOT_FOUND);
	}
}
