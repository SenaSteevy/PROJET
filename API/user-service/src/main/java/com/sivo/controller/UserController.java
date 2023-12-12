package com.sivo.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.entity.User;
import com.sivo.request.RegisterRequestRequest;
import com.sivo.request.UserRequest;
import com.sivo.response.UserResponse;
import com.sivo.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(value = {"/registerNewUser"})
	@PreAuthorize("hasRole('Admin')")
	public UserResponse createUser (@RequestBody UserRequest userRequest){
			return userService.createUser(userRequest);
	
	}
	
	
	
	@PostConstruct
	public void initializeRolesAndUsers() {
		userService.initUsersAndRoles();
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PostMapping("/newRegisterRequest")
	public ResponseEntity<?> addRegisterRequest(@RequestBody RegisterRequestRequest registerRequest){
		return userService.addRegisterRequest(registerRequest);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PostMapping(value = {"/updateUserById/{id}"})
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest){
			return userService.updateUser(id, userRequest);		
	}
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("getById/{id}")
	public ResponseEntity<?> findUserById (@PathVariable("id") Long id) {
		return userService.findById(id);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@DeleteMapping("/deleteUserById/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){
		return userService.deleteUserById(id);
	}

	
	@GetMapping("getAll")
	@PreAuthorize("hasRole('Admin')")
	@ResponseBody
	public List<User> getAll () {
		return userService.getAll();
	}
}
