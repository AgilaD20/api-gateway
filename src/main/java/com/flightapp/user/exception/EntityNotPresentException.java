package com.flightapp.user.exception;


public class EntityNotPresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public EntityNotPresentException(String message) {
		this.message = message;
	}
	

}
