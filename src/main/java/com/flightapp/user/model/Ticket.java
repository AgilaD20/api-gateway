package com.flightapp.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Ticket {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PNR")
	private Integer PNR;
	
	@Column(name="FLIGHTID")
	private  Integer flightId;
	
	@Column(name="MEALPREFERENCE")
	private  mealpreference mealPreference;
	
	@Column(name="PRICE")
	private  Double price;
	
	@Column(name="PCOUNT")
	private Integer passengerCount;
	
	@Column(name="PDetails")
	private String passengerdetails;
	
	@ManyToOne
	@JoinColumn(name="email")
	private  Userentity user; 
	
	@Column(name="SEATNUMBERS")
	private String seatNumbers;
	
}
