package com.flightapp.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flightapp.user.model.Userentity;




//import com.flightapp.user.model.Userentity;

@Repository
public interface UserRepository extends JpaRepository<Userentity,Integer> {

	@Query
	public Userentity findByEmail(String email);
	
	@Query(value="SELECT role_id FROM user_roles WHERE user_id=:userid",nativeQuery=true)
	public List<Integer> getRolesByEmail (@Param("userid")Integer userid);

	@Modifying
	@Query(value="insert into user_roles(user_id, role_id) values(:userId,:roleId)",nativeQuery=true)
	public void updateRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
	
	
	
}
