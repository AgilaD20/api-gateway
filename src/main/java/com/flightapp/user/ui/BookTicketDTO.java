package com.flightapp.user.ui;

import java.util.List;

import javax.persistence.ManyToOne;

import com.flightapp.user.model.Userentity;
import com.flightapp.user.model.mealpreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookTicketDTO {
	
	private mealpreference mealPreference;
	
	private String userName;
	
	private String userEmail;
	
	private List<Passenger> passengersList;
	
	private  Double price;
	
	private List<String> seatNumbers;
	
	

}
