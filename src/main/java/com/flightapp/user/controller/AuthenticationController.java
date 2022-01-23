package com.flightapp.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.user.config.JWTutil;
import com.flightapp.user.service.FlightUserDetails;
import com.flightapp.user.service.UserService;
import com.flightapp.user.ui.AuthenticationRequest;
import com.flightapp.user.ui.AuthenticationResponse;
import com.flightapp.user.ui.CreateUserResponseModel;
import com.flightapp.user.ui.CreateusrRequestModel;

/*
import com.flightapp.user.config.JWTutil;
import com.flightapp.user.service.FlightUserDetails;
import com.flightapp.user.ui.AuthenticationRequest;
import com.flightapp.user.ui.AuthenticationResponse;
*/

@RestController
@RequestMapping("/api/v1.0/user")
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;

	private final JWTutil jwtTokenUtil;

	private final FlightUserDetails userDetailsService;
	
	private final UserService userService;

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, JWTutil jwtTokenUtil,
			FlightUserDetails userDetailsService, UserService userService ) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
	}

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authrequest) throws Exception {
		try {

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authrequest.getUserEmail(), authrequest.getPassword()));

		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authrequest.getUserEmail());
		AuthenticationResponse authresponse = new AuthenticationResponse(jwtTokenUtil.generateToken(userDetails));

		return ResponseEntity.ok(authresponse);
	}
	
	@PostMapping("/register")
    public ResponseEntity<CreateUserResponseModel> createUser(@Validated @RequestBody CreateusrRequestModel userDetails)
    {
		CreateUserResponseModel userResponseModel = userService.creatuser(userDetails);
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponseModel);
		

    }

}

