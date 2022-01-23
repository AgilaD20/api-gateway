package com.flightapp.user.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flightapp.user.model.Userentity;
import com.flightapp.user.repository.RoleRepository;
import com.flightapp.user.repository.UserRepository;
import com.flightapp.user.ui.CreateUserResponseModel;
import com.flightapp.user.ui.CreateusrRequestModel;

/*
import com.flightapp.user.Repository.RoleRepository;
import com.flightapp.user.Repository.UserRepository;
import com.flightapp.user.model.Userentity;
import com.flightapp.user.ui.CreateUserResponseModel;
import com.flightapp.user.ui.CreateusrRequestModel;
*/
@Service
public class UserService {
	
	private final UserRepository userrepository;
	
	private final RoleRepository rolerepository;
	
	private final ModelMapper modelMapper;
	
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userrepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.userrepository = userrepository;
		this.modelMapper= modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.rolerepository = roleRepository;
	}
	
	@Transactional
	public CreateUserResponseModel creatuser(CreateusrRequestModel request)
	{
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Userentity user = modelMapper.map(request, Userentity.class);
		user.setEncryptedpassword(passwordEncoder.encode(request.getPassword()));
		Userentity  usr = userrepository.save(user);
		userrepository.updateRole(usr.getId(),rolerepository.findByRoleName("USER").getRoleId());
		return modelMapper.map(user, CreateUserResponseModel.class);
		
	}
	
	

}
