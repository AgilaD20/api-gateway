package com.flightapp.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatsNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5345332493310312558L;
	private String message;

}
