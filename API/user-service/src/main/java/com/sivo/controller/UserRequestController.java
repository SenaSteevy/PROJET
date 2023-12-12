package com.sivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.entity.RegisterRequest;
import com.sivo.request.RegisterRequestRequest;
import com.sivo.service.RegisterRequestService;


@RestController
@CrossOrigin
@RequestMapping("/api/user")

public class UserRequestController {
	
	@Autowired
	RegisterRequestService userRequestService;
	
	@PostMapping("createUserRequest")
	public ResponseEntity<?> createUserRequest(@RequestBody RegisterRequestRequest userRequestRequest) {
		
		return userRequestService.createUserRequest(userRequestRequest);
		
	}
	
	@GetMapping("/getAllUserRequests")
	public ResponseEntity<List<RegisterRequest>> getAllUserRequests() {
		return userRequestService.getAll();
	}

	@PostMapping("/deleteUserRequest/{id}")
	public ResponseEntity<?> deleteUserRequest(@PathVariable("id") Long id){
		return userRequestService.deleteById(id);
	}
	
	@PostMapping("/updateUserRequest/{id}")
	public ResponseEntity<?> updateUserRequest(@PathVariable("id") Long id, @RequestBody RegisterRequestRequest userRequestRequest){
		return userRequestService.updateById(id, userRequestRequest);
	}
}
