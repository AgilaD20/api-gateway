package com.flightapp.user.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.RestTemplate;

import com.flightapp.user.config.JWTutil;
import com.flightapp.user.exception.EntityNotPresentException;
import com.flightapp.user.exception.SeatsNotUpdatedException;
import com.flightapp.user.exception.TicketCannotBeCancelledException;
import com.flightapp.user.exception.TicketNotPresentException;
import com.flightapp.user.model.Ticket;
import com.flightapp.user.model.Userentity;
import com.flightapp.user.repository.TicketRepository;
import com.flightapp.user.repository.UserRepository;
import com.flightapp.user.ui.BookTicketDTO;
import com.flightapp.user.ui.FlightDTO;
import com.flightapp.user.ui.FlightDTOList;
import com.flightapp.user.ui.FlightSearchRequest;
import com.flightapp.user.ui.TicketDTO;
import com.flightapp.user.ui.UpdateSeatDTO;

@Service
public class BookingService {

	private final TicketRepository ticketRepository;

	private final UserRepository userRepository;

	private final ModelMapper modelMapper;
	;;
	private final RestTemplate restTemplate;

	private final JWTutil jwtutil;
	
	private static final String FLIGHT_URL ="http://localhost:8082/api/v1.0/common/flight";

	public BookingService(TicketRepository ticketRepository, UserRepository userRepository, ModelMapper modelMapper, RestTemplate restTemplate, JWTutil jwtutil) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.restTemplate = restTemplate;
		this.jwtutil = jwtutil;
	}

	public Ticket bookticket(Integer flightid, BookTicketDTO ticketDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Ticket ticket = new Ticket();
		ticket.setFlightId(flightid);
		ticket.setMealPreference(ticketDTO.getMealPreference());
		ticket.setPassengerCount(ticketDTO.getPassengersList().size());
		ticket.setPassengerdetails(
				ticketDTO.getPassengersList().stream().map(s -> s.toString()).collect(Collectors.joining("|")));
		Userentity user = userRepository.findByEmail(ticketDTO.getUserEmail());
		System.out.println(user == null);
		ticket.setUser(user);
		
		HttpHeaders headers= getHeaders();
		HttpEntity<UpdateSeatDTO> request = new HttpEntity<>(new UpdateSeatDTO(ticketDTO.getPassengersList().size(),ticketDTO.getSeatNumbers(),flightid),headers);
		try
		{
			restTemplate
					  .exchange(FLIGHT_URL+"/udpateSeats", HttpMethod.POST, request,Object.class);
		}
		catch(HttpClientErrorException ex)
				{
			
			throw new SeatsNotUpdatedException("Flight or Seat is not available");
				}
		
		ticket.setSeatNumbers(
				ticketDTO.getSeatNumbers().stream().map(s -> s.toString()).collect(Collectors.joining("|")));
			HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
			ResponseEntity<FlightDTO> flightDTOResponse = restTemplate.exchange(FLIGHT_URL+"/{flightId}",HttpMethod.GET,jwtEntity,FlightDTO.class,flightid);
			ticket.setPrice(flightDTOResponse.getBody().getPrice() * ticketDTO.getPassengersList().size());
			
			return ticketRepository.save(ticket);
		
	}

	public Optional<Ticket> getTicketByPNR(Integer PNR) {
		return ticketRepository.findById(PNR);
	}

	public List<TicketDTO> getBookingByEmail(String emailId) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Userentity user = userRepository.findByEmail(emailId);
		
		if(user==null)
		{
			throw new EntityNotPresentException("User email not present");
		}
		
		List<Ticket> ticketList =  ticketRepository.findAllByUser(user);
		
		List<TicketDTO> ticketDTOList = ticketList.stream().map(t->
		{	
	    TicketDTO dto = modelMapper.map(t, TicketDTO.class);
	    dto.setUserName(user.getFirstName()+" "+user.getLastName());
	    return dto;
	    }).collect(Collectors.toList());

       return ticketDTOList;
	}

	public void deleteBookingByEmail(Integer pNR) throws TicketNotPresentException, TicketCannotBeCancelledException {
		
		LocalDateTime currentTime = LocalDateTime.now();

		Optional<Ticket> ticket = ticketRepository.findById(pNR);
		if (!ticket.isPresent()) {
			throw new TicketNotPresentException("Ticket is not found for the given PNR");
		}
		ticket.get().getFlightId();
		HttpHeaders headers= getHeaders();
		//HttpEntity<FlightSearchRequest> jwtEntity = new HttpEntity<FlightSearchRequest>(flightSearchRequest,headers);
		// Implement to get the flight departure date
		//FlightDTO flightDTO = restTemplate.getForObject("http://localhost:8082/api/v1.0/flight/departuredate/{flightId}",FlightDTO.class,pNR);
		HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
		ResponseEntity<FlightDTO> flightDTOResponse = restTemplate.exchange(FLIGHT_URL+"/{flightId}",HttpMethod.GET,jwtEntity,FlightDTO.class,ticket.get().getFlightId());
		 if(Duration.between(currentTime, flightDTOResponse.getBody().getDepartureTime()).toHours()<24) 
		 { throw new
		 TicketCannotBeCancelledException("Ticket cannot be cancelled before 24 hours of departure time"
		 ); }
		 
		ticketRepository.deleteById(pNR);

	}

	public List<FlightDTO> getAllFlights(FlightSearchRequest flightSearchRequest) {
		HttpHeaders headers= getHeaders();
		HttpEntity<FlightSearchRequest> jwtEntity = new HttpEntity<FlightSearchRequest>(flightSearchRequest,headers);
		try
		{
			ResponseEntity<FlightDTOList> flightList = restTemplate.exchange(FLIGHT_URL+"/flights", HttpMethod.POST, jwtEntity,FlightDTOList.class);
			return flightList.getBody().getFlightList();
		}
		catch(Exception ex )
		{
			throw new EntityNotPresentException("No Flight is present for the given criteria");
		}
		
		
	}
	
	public HttpHeaders getHeaders()
	{
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	User userDetails = new User(auth.getPrincipal().toString(),auth.getCredentials().toString(),auth.getAuthorities());
	String token = "Bearer " + jwtutil.generateToken(userDetails);
	HttpHeaders headers = new HttpHeaders();
	headers.set("Authorization", token);
	return headers;
		
	}

}
