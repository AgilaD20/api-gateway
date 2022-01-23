package com.flightapp.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flightapp.user.model.Privilege;
import com.flightapp.user.model.Role;
import com.flightapp.user.model.Userentity;
import com.flightapp.user.repository.RoleRepository;
import com.flightapp.user.repository.UserRepository;

/*
import com.flightapp.user.Repository.RoleRepository;
import com.flightapp.user.Repository.UserRepository;
import com.flightapp.user.model.Privilege;
import com.flightapp.user.model.Role;
import com.flightapp.user.model.Userentity;
*/

@Service
public class FlightUserDetails implements UserDetailsService {

	

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	

	public FlightUserDetails(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Userentity user = userRepository.findByEmail(username);
		List<Integer> roleIds = userRepository.getRolesByEmail(user.getId());
		List<Role> roles = roleIds.stream().map(s -> roleRepository.findById(s).get()).collect(Collectors.toList());

		return new User(user.getEmail(), user.getEncryptedpassword(), getAuthoritesfromRole(roles));

	}

	private List<GrantedAuthority> getAuthorities(List<Privilege> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		privileges.stream().forEach(s -> authorities.add(new SimpleGrantedAuthority(s.getPrivilegeName())));

		return authorities;

	}

	private List<GrantedAuthority> getAuthoritesfromRole(List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		roles.stream().forEach(s -> authorities.addAll(getAuthorities(s.getPrivileges())));

		return authorities;

	}

}
