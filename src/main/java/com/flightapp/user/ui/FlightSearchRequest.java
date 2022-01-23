package com.flightapp.user.ui;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightSearchRequest {
	
	private String fromLocation;
	private String destination;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	

}
