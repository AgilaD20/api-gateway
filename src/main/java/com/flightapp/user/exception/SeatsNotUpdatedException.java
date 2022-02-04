package com.flightapp.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatsNotUpdatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5502577973735022697L;
	private String message;

}
