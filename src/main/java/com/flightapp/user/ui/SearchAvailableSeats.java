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
public class SearchAvailableSeats {
	
	
private Integer flightId;

private LocalDate departureDate;

}
