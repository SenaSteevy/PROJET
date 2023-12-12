package com.sivo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivo.entity.ImageModel;
import com.sivo.entity.RegisterRequest;
import com.sivo.entity.Role;
import com.sivo.entity.User;
import com.sivo.repository.ImageRepository;
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
	ImageRepository imageRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RegisterRequestRepository registerRequestRepository;
	
	
	public UserResponse createUser(UserRequest userRequest) {
		
		//aucun nouvel user ne doit etre admin 
		Role role = roleRepository.findByRoleName("User").get();
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		userRequest.setRoles(roles);
		Optional<User> userList = userRepository.findByEmail(userRequest.getEmail());
		
		if(!userList.isEmpty())
			return new UserResponse();
		
		User user = new User(userRequest);
		user.setPassword(getEncryptedPassword(userRequest.getPassword()));
		user = userRepository.save(user);

		return new UserResponse(user);
	}
	
	@Transactional
	public void initUsersAndRoles() {
	    imageRepository.deleteAll();
	    userRepository.deleteAll();
	    roleRepository.deleteAll();

	    Role admin = new Role("Admin", "All access granted");
	    Role user = new Role("User", "Have access to some features");

	    admin = roleRepository.save(admin);
	    user = roleRepository.save(user);

	    Set<Role> adminRoles = new HashSet<>();
	    adminRoles.add(admin);
	    User user1 = new User("anesyveets@gmail.com", "Male", "admin", "admin", passwordEncoder.encode("admin123"), "Admin", adminRoles);
	    user1 = userRepository.save(user1);

	    Set<Role> userRoles = new HashSet<>();
	    userRoles.add(user);
	    User user2 = new User("user1@gmail.com", "Male", "sena", "steevy", passwordEncoder.encode("user123"), "user", userRoles);
	    user2 = userRepository.save(user2);
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

	public ResponseEntity<?> findById(Long id) {
		
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(user.get());
	}

	public ResponseEntity<?> deleteUserById(Long id) {
		
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			return ResponseEntity.notFound().build();
		Optional<ImageModel> image = imageRepository.findByUser(user.get());
		
		if(image.isPresent())
			imageRepository.delete(image.get());
		userRepository.delete(user.get());
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<User> updateUser(Long id, UserRequest userRequest) {
		
		Optional<User> optionalUser  = userRepository.findById(id);
		if(optionalUser.isEmpty())
			return ResponseEntity.notFound().build();
		
		User updatedUser = new User(userRequest);
		updatedUser.setId(id);
		updatedUser =  userRepository.save(updatedUser);
		return ResponseEntity.ok(updatedUser);
	}
}
	
	

