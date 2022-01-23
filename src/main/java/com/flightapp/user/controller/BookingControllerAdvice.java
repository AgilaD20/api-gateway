package com.flightapp.user.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flightapp.user.exception.EntityNotPresentException;
import com.flightapp.user.exception.SeatsNotUpdatedException;
import com.flightapp.user.exception.TicketCannotBeCancelledException;
import com.flightapp.user.exception.TicketNotPresentException;
import com.flightapp.user.ui.ErrorResponse;

@RestControllerAdvice
public class BookingControllerAdvice {
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleTicketNotFoundException(TicketNotPresentException ex)
	{
		ErrorResponse response = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleTicketCannotBeCancelledException(TicketCannotBeCancelledException ex)
	{
		ErrorResponse response = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_MODIFIED.value(),LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(response);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleSeatNotUpdatedException(SeatsNotUpdatedException ex)
	{
		ErrorResponse response = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_ACCEPTABLE.value(),LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleEntityNotPresentException(EntityNotPresentException ex)
	{
		ErrorResponse response = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	
	

}
