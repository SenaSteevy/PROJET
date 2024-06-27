package com.sivo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.request.AuthRequest;
import com.sivo.response.AuthResponse;
import com.sivo.service.CustomUserDetailsService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthController {

   
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
       
    	return userDetailsService.authenticate(authRequest);
    }
    
  
}


