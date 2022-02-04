package com.flightapp.user.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flightapp.user.model.mealpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketDTO {

	private Integer PNR;

	private Integer flightId;

	private mealpreference mealPreference;

	private Double price;

	private Integer passengerCount;

	private String passengerdetails;

	private String userName;

	private String userEmail;
	
	private String seatNumbers;
	
	private Boolean isCancelled;
	
	private LocalDateTime createdTimeStamp;
	
	private LocalDate departureDate;

}
