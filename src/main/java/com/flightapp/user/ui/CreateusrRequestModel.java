package com.flightapp.user.ui;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateusrRequestModel {

	
    @NotNull(message="Firstname cannot be empty")
	private String firstName;
	
    @NotNull(message="Lastname cannot be empty")
	private String lastName;
	
    @NotNull(message="Email cannot be empty")
	private String email;
	
    @NotNull(message="Password cannot be empty")
	private String password;

}
