package com.flightapp.user.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BookingConfig {
	
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
	
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
	
	/*
	 * @Bean public ObjectMapper mapper() { ObjectMapper mapper = new
	 * ObjectMapper();
	 * mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); return
	 * mapper; }
	 */
	

	

}
