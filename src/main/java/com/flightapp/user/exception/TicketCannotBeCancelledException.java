package com.flightapp.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketCannotBeCancelledException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 714927599780593541L;
	private String message;

}
