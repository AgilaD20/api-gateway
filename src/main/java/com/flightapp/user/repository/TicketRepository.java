package com.flightapp.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flightapp.user.model.Ticket;
import com.flightapp.user.model.Userentity;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Integer> {

	@Query
	List<Ticket> findAllByUser(Userentity user);

}
