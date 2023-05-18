package com.sivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.request.JwtRequest;
import com.sivo.service.JwtService;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class JwtController {

	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		try{
			return jwtService.createJwtToken(jwtRequest);
		}catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
