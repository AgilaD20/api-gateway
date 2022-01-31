package com.flightapp.user.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.user.exception.SeatsNotFoundException;
import com.flightapp.user.exception.TicketCannotBeCancelledException;
import com.flightapp.user.exception.TicketNotPresentException;
import com.flightapp.user.model.Ticket;
import com.flightapp.user.service.BookingService;
import com.flightapp.user.ui.ApiResponse;
import com.flightapp.user.ui.BookTicketDTO;
import com.flightapp.user.ui.Booking;
import com.flightapp.user.ui.FlightDTO;
import com.flightapp.user.ui.FlightSearchRequest;
import com.flightapp.user.ui.TicketDTO;

@RestController
@RequestMapping("/api/v1.0/user")
public class BookingController {

	private final BookingService bookingService;

	private final ModelMapper modelMapper;

	public BookingController(BookingService bookingService, ModelMapper modelMapper) {
		this.bookingService = bookingService;
		this.modelMapper = modelMapper;
	}

	@PostMapping("/flight/booking/{flightid}")
	public ResponseEntity<TicketDTO> bookFlight(@PathVariable("flightid") Integer flightid,
			@RequestBody BookTicketDTO bookrequest) {
		Ticket ticket = bookingService.bookticket(flightid, bookrequest);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
		ticketDTO.setUserEmail(bookrequest.getUserEmail());
		//ticketDTO.setUserName(bookrequest.getUserName());
		
		return ResponseEntity.ok(ticketDTO);
	}

	@GetMapping("/flight/ticket/{pnr}")
	public ResponseEntity<TicketDTO> getTicketByPNR(@PathVariable("pnr") Integer PNR) throws TicketNotPresentException {
		Optional<Ticket> ticket = bookingService.getTicketByPNR(PNR);

		if (ticket.isEmpty()) {
			throw new TicketNotPresentException("PNR number is not valid");
		}
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		TicketDTO ticketDTO = modelMapper.map(ticket.get(), TicketDTO.class);
		ticketDTO.setUserEmail(ticket.get().getUser().getEmail());
		return ResponseEntity.ok(ticketDTO);
	}

	@GetMapping("/flight/booking/history/{emailId}")
	public ResponseEntity<List<Booking>> getBookingsByEmail(@PathVariable("emailId") String emailId)
			throws TicketNotPresentException {
		List<Booking> ticketList = bookingService.getBookingByEmail(emailId);
		if (ticketList.isEmpty()) {
			throw new TicketNotPresentException("No Booking found for the user email");
		}

		return ResponseEntity.ok(ticketList);
	}

	@DeleteMapping("/flight/booking/cancel/{pnr}")
	public ResponseEntity<ApiResponse> deleteBookingsBypnr(@PathVariable("pnr") Integer PNR)
			throws TicketNotPresentException, TicketCannotBeCancelledException {
		bookingService.deleteBookingByEmail(PNR);

		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Ticket Cancelled successfully"));
	}

	@PostMapping("/flight/search")
	public ResponseEntity<List<FlightDTO>> getAllFlights(@RequestBody FlightSearchRequest flightSearchRequest) {

		List<FlightDTO> bookingList = bookingService.getAllFlights(flightSearchRequest);
		return ResponseEntity.status(HttpStatus.OK).body(bookingList);
	}
	
	@GetMapping("/flight/availableseats/{flightid}")
	public ResponseEntity<List<String>> getAllSeats(@PathVariable Integer flightid) throws SeatsNotFoundException{
		
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.getSeatsByFlightId(flightid));
		
	}

}
