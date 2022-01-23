package com.flightapp.user.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.flightapp.user.service.FlightUserDetails;

//import com.flightapp.user.service.FlightUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	
	private final JWTutil jwtutil;
	
	private final FlightUserDetails userDetailsService;
	
	
@Autowired
	public AuthenticationFilter(JWTutil jwtutil, FlightUserDetails userDetailsService) {
		this.jwtutil = jwtutil;
		this.userDetailsService = userDetailsService;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tokenHeader = request.getHeader("Authorization");
		String jwttoken = null;
		String userEmail = null;
		
		if(tokenHeader !=null && tokenHeader.startsWith("Bearer "))
		{
			jwttoken = tokenHeader.substring(7);
			try
			{
			userEmail = jwtutil.getUserNamefromToken(jwttoken);
			}
			catch(IllegalArgumentException e)
			{
				log.error("Unable to get JWT Token");
			}
			catch(ExpiredJwtException e)
			{
				log.error("JWT Token has expired");
			}
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
			if(jwtutil.isValidToken(jwttoken, userDetails))
			{
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
				.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
