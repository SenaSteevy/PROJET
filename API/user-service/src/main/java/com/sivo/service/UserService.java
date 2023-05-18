package com.sivo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivo.entity.RegisterRequest;
import com.sivo.entity.Role;
import com.sivo.entity.User;
import com.sivo.repository.RegisterRequestRepository;
import com.sivo.repository.RoleRepository;
import com.sivo.repository.UserRepository;
import com.sivo.request.RegisterRequestRequest;
import com.sivo.request.UserRequest;
import com.sivo.response.UserResponse;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RegisterRequestRepository registerRequestRepository;
	
	
	public UserResponse createUser(UserRequest userRequest) {
		
		//aucun nouvel user ne doit etre admin 
		Role role = roleRepository.findById("User").get();
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		userRequest.setRoles(roles);
		
		User user = new User(userRequest);
		user.setPassword(getEncryptedPassword(userRequest.getPassword()));
		userRepository.save(user);

		return new UserResponse(user);
	}
	
	public void initUsersAndRoles() {
		
		userRepository.deleteAll();
		roleRepository.deleteAll();

		Role admin = new Role("Admin", "All access granted");
		roleRepository.save(admin);
		
		Role user = new Role("User", "Have access to some features");
		roleRepository.save(user);
		
		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(admin);
		User user1 = new User("anesyveets@gmail.com", "admin", "admin", passwordEncoder.encode("admin123"), adminRoles);
		
		userRepository.save(user1);
		
		Set<Role> userRoles = 	new HashSet<>();
		userRoles.add(user);
		User user2 = new User("user1@gmail.com", "sena", "steevy", passwordEncoder.encode("user123"), userRoles);
		userRepository.save(user2);

		
	}


	public String getEncryptedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	
	public ResponseEntity<?> addRegisterRequest(RegisterRequestRequest registerRequest) {
		
		RegisterRequest register =  new RegisterRequest(registerRequest );
			register =	registerRequestRepository.save(register);
			System.out.println("registerRequest saved");
		return ResponseEntity.ok(register);
	}

}
