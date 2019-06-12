package coupons.CouponsSystemWeb.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Web application interface for errors, including text and messages
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebApiError {

	private String errorText;
	private List<String> errorMessages;

}
