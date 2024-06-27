	package com.sivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.entity.User;
import com.sivo.request.UserRequest;
import com.sivo.service.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
	
	@Autowired
	UserService userService;
	
	
	@PostMapping(value = {"/updateById/{id}"})
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest){
			return userService.updateUser(id, userRequest);		
	}
	
	@GetMapping("getById/{id}")
	public ResponseEntity<?> findUserById (@PathVariable("id") Long id) {
		return userService.findById(id);
	}
	

	
	@DeleteMapping("/deleteUserById/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){
		return userService.deleteUserById(id);
	}

	
	
}
