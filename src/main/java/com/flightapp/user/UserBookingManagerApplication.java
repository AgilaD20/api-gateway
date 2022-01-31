package com.flightapp.user;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserBookingManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserBookingManagerApplication.class, args);
	}

	
}
