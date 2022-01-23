package com.flightapp.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flightapp.user.model.Role;

//import com.flightapp.user.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	@Query
	public Role findByRoleName(String rolename);

}
