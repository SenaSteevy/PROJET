package com.sivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.entity.User;
import com.sivo.request.JwtRequest;
import com.sivo.service.JwtService;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class JwtController {

	@Autowired
	private JwtService jwtService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		try {
			return jwtService.createJwtToken(jwtRequest);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("loadUserByUsername/{username}")
	public Mono<User> loadUserByUsername(@PathVariable("username") String username) {
		return jwtService.loadUserByUsername(username);
	}

	@GetMapping("getUserNameFromToken/{jwtToken}")
	Mono<String> getUserNameFromtoken(@PathVariable("jwtToken") String jwtToken) {

		return jwtService.getUserNameFromToken(jwtToken);
	}

	@GetMapping("validateToken/{jwtToken}/{username}")
	Mono<Boolean> validateToken(@PathVariable("jwtToken") String jwtToken,
			@PathVariable("username") String username) {
		
		return jwtService.validateToken(jwtToken,username);

	}
}
