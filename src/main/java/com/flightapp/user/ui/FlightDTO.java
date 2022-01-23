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
public class FlightDTO {
	
	
	
	@Override
	public String toString() {
		return "FlightDTO [flightName=" + flightName + ", availableSeats=" + availableSeats + ", fromLocation="
				+ fromLocation + ", destination=" + destination + ", price=" + price + ", tripType=" + tripType
				+ ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + "]";
	}

	private String flightName;
	
	private Integer availableSeats;
	
	private String fromLocation;
	
	private String destination;
	
	private Double price;
	
	private TripType tripType;
	
	private LocalDateTime departureTime;
	
	private LocalDateTime arrivalTime;

}
