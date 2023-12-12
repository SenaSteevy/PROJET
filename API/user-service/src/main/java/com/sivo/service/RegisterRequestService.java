package com.sivo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sivo.entity.RegisterRequest;
import com.sivo.repository.RegisterRequestRepository;
import com.sivo.request.RegisterRequestRequest;

@Service
public class RegisterRequestService {

	@Autowired
	RegisterRequestRepository registerRequestRepository;
	
	public ResponseEntity<?> createUserRequest(RegisterRequestRequest registerRequestRequest) {
		
		RegisterRequest registerRequest = new RegisterRequest(registerRequestRequest);
		registerRequest = registerRequestRepository.save( registerRequest );
		return ResponseEntity.ok(registerRequest);
	}

	public ResponseEntity<List<RegisterRequest>> getAll() {

		List<RegisterRequest> registerRequests = registerRequestRepository.findAll();
		return ResponseEntity.ok(registerRequests);
	}

	public ResponseEntity<?> deleteById(Long id) {
		
		Optional<RegisterRequest> registerRequest = registerRequestRepository.findById(id);
		if(registerRequest.isEmpty())
			return ResponseEntity.notFound().build();
		
		registerRequestRepository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> updateById(Long id, RegisterRequestRequest userRequestRequest) {
		
		Optional<RegisterRequest> registerRequest = registerRequestRepository.findById(id);
		if(registerRequest.isEmpty())
			return ResponseEntity.notFound().build();
		
		RegisterRequest updatedRequest = new RegisterRequest(userRequestRequest);
		updatedRequest.setId(id);
		
		updatedRequest = registerRequestRepository.save(updatedRequest);
		return ResponseEntity.ok().build();
	}
	

}
