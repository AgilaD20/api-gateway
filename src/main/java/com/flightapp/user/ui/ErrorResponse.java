package com.flightapp.user.ui;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	
	private String message;
	private int errorCode;
	private LocalDateTime errortimestamp;

}
