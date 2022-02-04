package com.flightapp.user.ui;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	//private String userName;
	
	private String userEmail;
	
	//@JsonDeserialize(using = CustomStringDeserializer.class)
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<Passenger> passengersList;
	
	private  Double price;
	
	private List<String> seatNumbers;
	
	private LocalDate departureDate;
	
	

}
