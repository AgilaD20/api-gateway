package com.flightapp.user.ui;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FlightDTOList {
	
	private List<FlightDTO> flightList;
	
	public FlightDTOList(List<FlightDTO> flightList) {
		this.flightList = flightList;
	}
	
	public List<FlightDTO> getFlightList()
	{
		return this.flightList;
	}
	
	public void setFlightList(List<FlightDTO> flightList)
	{
		this.flightList = flightList;
	}

}
