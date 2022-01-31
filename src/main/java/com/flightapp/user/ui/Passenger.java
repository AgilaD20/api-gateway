package com.flightapp.user.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.flightapp.user.config.CustomStringDeserializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class Passenger {
	
	private String passengerName;
	
	private String gender;
	
	private int age;
	
	@Override
	public String toString()
	{
		return(passengerName+" "+gender+" "+age);
	}

}
