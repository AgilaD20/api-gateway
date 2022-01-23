package com.flightapp.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketNotPresentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074299979404171671L;
	private String message;
	

}
