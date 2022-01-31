package com.flightapp.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flightapp.user.model.Ticket;
import com.flightapp.user.model.Userentity;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Integer> {

	@Query
	List<Ticket> findAllByUserOrderByCreatedTimeStampDesc(Userentity user);

	@Modifying
	@Query(value="update ticket set iscancelled=1 where pnr=:pNR",nativeQuery=true)
	void updateCancelledById(Integer pNR);
	


}
