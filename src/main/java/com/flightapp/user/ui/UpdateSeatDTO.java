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
public class UpdateSeatDTO {
	
	private int seatCount;
	
	private List<String> seatNumbers;
	
	private Integer flighId;
	
	

}
