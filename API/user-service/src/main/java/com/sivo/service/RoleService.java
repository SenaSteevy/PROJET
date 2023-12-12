package com.sivo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sivo.entity.Role;
import com.sivo.repository.RoleRepository;
import com.sivo.request.RoleRequest;
import com.sivo.response.RoleResponse;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	public RoleResponse createRole(RoleRequest roleRequest) {
		
		Role role = roleRepository.save( new Role(roleRequest) );
		
		return new RoleResponse(role);
	}

	public ResponseEntity<List<Role>> getAll() {

		List<Role> roles = roleRepository.findAll();
		return ResponseEntity.ok(roles);
	}
	

}
