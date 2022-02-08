package com.flightapp.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
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
import com.flightapp.user.exception.SeatsNotFoundException;
import com.flightapp.user.exception.SeatsNotUpdatedException;
import com.flightapp.user.exception.TicketCannotBeCancelledException;
import com.flightapp.user.exception.TicketNotPresentException;
import com.flightapp.user.model.Ticket;
import com.flightapp.user.model.Userentity;
import com.flightapp.user.repository.TicketRepository;
import com.flightapp.user.repository.UserRepository;
import com.flightapp.user.ui.ApiResponse;
import com.flightapp.user.ui.BookTicketDTO;
import com.flightapp.user.ui.Booking;
import com.flightapp.user.ui.FlightDTO;
import com.flightapp.user.ui.FlightDTOList;
import com.flightapp.user.ui.FlightSearchRequest;
import com.flightapp.user.ui.SearchAvailableSeats;
import com.flightapp.user.ui.UpdateSeatDTO;

@Service
public class BookingService {

	private final TicketRepository ticketRepository;

	private final UserRepository userRepository;

	private final ModelMapper modelMapper;;;
	private final RestTemplate restTemplate;

	private final JWTutil jwtutil;

	@Value("${flight.url}")
	private String FLIGHT_URL;
	//"http://localhost:8082/api/v1.0/common/flight";
	

	public BookingService(TicketRepository ticketRepository, UserRepository userRepository, ModelMapper modelMapper,
			RestTemplate restTemplate, JWTutil jwtutil) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.restTemplate = restTemplate;
		this.jwtutil = jwtutil;
	}

	@Transactional
	public Ticket bookticket(Integer flightid, BookTicketDTO ticketDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Ticket ticket = new Ticket();
		ticket.setCreatedTimeStamp(LocalDateTime.now());
		ticket.setDepartureDate(ticketDTO.getDepartureDate());
		ticket.setFlightId(flightid);
		ticket.setMealPreference(ticketDTO.getMealPreference());
		ticket.setPassengerCount(ticketDTO.getPassengersList().size());
		ticket.setPassengerdetails(
				ticketDTO.getPassengersList().stream().map(s -> s.toString()).collect(Collectors.joining("|")));
		Userentity user = userRepository.findByEmail(ticketDTO.getUserEmail());
		System.out.println(user == null);
		ticket.setUser(user);

		HttpHeaders headers = getHeaders();
		HttpEntity<UpdateSeatDTO> request = new HttpEntity<>(new UpdateSeatDTO(ticketDTO.getPassengersList().size(),
				ticketDTO.getSeatNumbers(), flightid, ticketDTO.getDepartureDate()), headers);
		try {
			restTemplate.exchange(FLIGHT_URL + "/updateSeats", HttpMethod.POST, request, Object.class);
		} catch (HttpClientErrorException ex) {

			throw new SeatsNotUpdatedException("Flight or Seat is not available");
		}

		ticket.setSeatNumbers(
				ticketDTO.getSeatNumbers().stream().map(s -> s.toString()).collect(Collectors.joining("|")));
		HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
		ResponseEntity<FlightDTO> flightDTOResponse = restTemplate.exchange(FLIGHT_URL + "/{flightId}", HttpMethod.GET,
				jwtEntity, FlightDTO.class, flightid);
		ticket.setPrice(flightDTOResponse.getBody().getPrice() * ticketDTO.getPassengersList().size());

		return ticketRepository.save(ticket);

	}

	public Optional<Ticket> getTicketByPNR(Integer PNR) {
		return ticketRepository.findById(PNR);
	}

	public List<Booking> getBookingByEmail(String emailId) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Userentity user = userRepository.findByEmail(emailId);

		if (user == null) {
			throw new EntityNotPresentException("User email not present");
		}

		List<Ticket> ticketList = ticketRepository.findAllByUserOrderByCreatedTimeStampDesc(user);
		HttpHeaders headers = getHeaders();
		HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);

		List<Booking> bookingList = ticketList.stream().map(t -> {
			Booking book = modelMapper.map(t, Booking.class);
			ResponseEntity<FlightDTO> flightDTOResponse = restTemplate.exchange(FLIGHT_URL + "/{flightId}",
					HttpMethod.GET, jwtEntity, FlightDTO.class, t.getFlightId());
			FlightDTO dto = flightDTOResponse.getBody();
			book.setAirlineName(dto.getAirlineName());
			book.setFlightName(dto.getFlightName());
			book.setDestination(dto.getDestination());
			book.setFromLocation(dto.getFromLocation());
			return book;
		}).collect(Collectors.toList());

		return bookingList;
	}

	@Transactional
	public void deleteBookingByEmail(Integer pNR) throws TicketNotPresentException, TicketCannotBeCancelledException {

		LocalDate currentTime = LocalDate.now();

		Optional<Ticket> ticketOptional = ticketRepository.findById(pNR);
		if (!ticketOptional.isPresent()) {
			throw new TicketNotPresentException("Ticket is not found for the given PNR");
		}
		ticketOptional.get().getFlightId();
		Ticket ticket = ticketOptional.get();
		HttpHeaders headers = getHeaders();

		if (Period.between(currentTime, ticket.getDepartureDate()).getDays() < 1) {
			throw new TicketCannotBeCancelledException("Ticket cannot be cancelled before 24 hours of departure time");
		}

		List<String> seatNumbers = Arrays.asList(ticket.getSeatNumbers().split("\\|"));
		UpdateSeatDTO updateseatRequest = new UpdateSeatDTO(seatNumbers.size(), seatNumbers, ticket.getFlightId(),
				ticket.getDepartureDate());
		HttpEntity<UpdateSeatDTO> seatEntity = new HttpEntity<UpdateSeatDTO>(updateseatRequest, headers);
		restTemplate.exchange(FLIGHT_URL + "/addseatsback", HttpMethod.POST, seatEntity, ApiResponse.class);
		ticketRepository.updateCancelledById(pNR);

	}

	public List<FlightDTO> getAllFlights(FlightSearchRequest flightSearchRequest) {
		HttpHeaders headers = getHeaders();
		System.out.println(FLIGHT_URL);
		HttpEntity<FlightSearchRequest> jwtEntity = new HttpEntity<FlightSearchRequest>(flightSearchRequest, headers);
		try {
			ResponseEntity<FlightDTOList> flightList = restTemplate.exchange(FLIGHT_URL + "/flights", HttpMethod.POST,
					jwtEntity, FlightDTOList.class);
			return flightList.getBody().getFlightList();
		} catch (Exception ex) {
			throw new EntityNotPresentException("No Flight is present for the given criteria");
		}

	}

	/*
	 * public List<String> getSeatsByFlightId(Integer flightid) throws
	 * SeatsNotFoundException { HttpHeaders headers = getHeaders();
	 * HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
	 * 
	 * try { ResponseEntity<String> flightDTOResponse =
	 * restTemplate.exchange(FLIGHT_URL + "/availableseat/{flightId}",
	 * HttpMethod.GET, jwtEntity, String.class, flightid); List<String> seatNumbers
	 * = Arrays.asList(flightDTOResponse.getBody().split("\\|")); return
	 * seatNumbers; } catch (Exception ex) { throw new
	 * SeatsNotFoundException("An unexpected error occured"); }
	 * 
	 * }
	 */

	public List<String> getSeatsBydepartureDate(SearchAvailableSeats search) throws SeatsNotFoundException {

		HttpHeaders headers = getHeaders();
		HttpEntity<SearchAvailableSeats> jwtEntity = new HttpEntity<SearchAvailableSeats>(search, headers);
		try {
			ResponseEntity<String> flightDTOResponse = restTemplate.exchange(FLIGHT_URL + "/availableseats",
					HttpMethod.POST, jwtEntity, String.class);
			List<String> seatNumbers = Arrays.asList(flightDTOResponse.getBody().split("\\|"));
			return seatNumbers;
		} catch (Exception ex) {
			throw new SeatsNotFoundException("An unexpected error occured");
		}

	}

	public HttpHeaders getHeaders() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = new User(auth.getPrincipal().toString(), auth.getCredentials().toString(),
				auth.getAuthorities());
		String token = "Bearer " + jwtutil.generateToken(userDetails);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		return headers;

	}

}
