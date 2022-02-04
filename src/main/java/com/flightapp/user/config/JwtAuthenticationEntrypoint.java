package com.flightapp.user.config;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5206191365885356996L;


	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		//response.set
        response.setHeader("message", "Please login again with correct credentials or sign up for new user");
        if(response.containsHeader("message")){
        	System.out.println("message is already there");
        	response.getHeader("message");
        }
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}

}
