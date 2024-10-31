package com.sivo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivo.entity.ImageModel;
import com.sivo.entity.Permission;
import com.sivo.entity.RegisterRequest;
import com.sivo.entity.Role;
import com.sivo.entity.User;
import com.sivo.repository.ImageRepository;
import com.sivo.repository.PermissionRepository;
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
	private PermissionRepository permissionReposotory; 
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	

	@Autowired
	private RegisterRequestRepository registerRequestRepository;
	
	
	 
	
	
	public UserResponse createUser(UserRequest userRequest) {
		
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

	    List<Permission> permissionList  = permissionReposotory.findAll();
	    
	    //Admin Permissions
	    Role admin = new Role("Admin", "All access granted",permissionList);
	    
	    //User Permissions
	   permissionList =  permissionList.stream()
	    .filter(permission -> !permission.getDescription().equalsIgnoreCase("MANAGE USERS"))
	    .filter(permission -> !permission.getDescription().equalsIgnoreCase("MANAGE ROLES"))
	    .collect(Collectors.toList());
	    
	    Role user = new Role("User", "Have access to some features", permissionList);

	    admin = roleRepository.save(admin);
	    user = roleRepository.save(user);

	   
	    User user1 = new User("admin@gmail.com", "Male", "admin", "admin", passwordEncoder.encode("admin123"), "Admin", admin);
	    user1 = userRepository.save(user1);

	   
	   /* User user2 = new User("user1@gmail.com", "Male", "user", "steevy", passwordEncoder.encode("user123"), "user", user);
	    user2 = userRepository.save(user2);*/
	}




	public String getEncryptedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public List<User> getAll() {
		return  userRepository.findAll();
		
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
		updatedUser.setPassword(getEncryptedPassword(userRequest.getPassword()));
		updatedUser.setId(id);
		updatedUser =  userRepository.save(updatedUser);
		return ResponseEntity.ok(updatedUser);
	}

	public ResponseEntity<?> findByEmail(String username) {
		Optional<User> user = userRepository.findByEmail(username);
		if(user.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user.get());
	}
}
	
	

