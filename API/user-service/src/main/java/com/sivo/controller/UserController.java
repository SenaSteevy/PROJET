package com.sivo.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@PostMapping("/registerNewUser")
	@PreAuthorize("hasRole('Admin')")
	public UserResponse createUser (@RequestBody UserRequest createUserRequest) {
		return userService.createUser(createUserRequest);
	}
	
	@PostConstruct
	public void initializeRolesAndUsers() {
		userService.initUsersAndRoles();
	}
	
	@PostMapping("/newRegisterRequest")
	public ResponseEntity<?> addRegisterRequest(@RequestBody RegisterRequestRequest registerRequest){
		return userService.addRegisterRequest(registerRequest);
	}
	

	
	@GetMapping("getAll")
	@PreAuthorize("hasRole('Admin')")
	@ResponseBody
	public List<User> getAll () {
		return userService.getAll();
	}
}
