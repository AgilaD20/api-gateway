package com.flightapp.user.ui;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
	

	private String flightName;
	
	private Integer flightID;
	
	private Integer availableSeats;
	
	private String fromLocation;
	
	private String destination;
	
	private Double price;
	
	private TripType tripType;
	
	private LocalDate departureDate;
	
	private String airlineName;
}
