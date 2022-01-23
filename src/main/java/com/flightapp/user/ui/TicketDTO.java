package com.flightapp.user.ui;

import java.util.List;

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
	
	private  Integer flightId;
	
	
	private  mealpreference mealPreference;
	
	
	private  Double price;
	
	
	private Integer passengerCount;
	
	
	private String passengerdetails;
	
private String userName;
	
	private String userEmail;
	
	

}
