package com.sivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.entity.Role;
import com.sivo.request.RoleRequest;
import com.sivo.response.RoleResponse;
import com.sivo.service.RoleService;


@RestController
@CrossOrigin
@RequestMapping("/api/user")

public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@PostMapping("createRole")
	public RoleResponse createRole(@RequestBody RoleRequest roleRequest) {
		
		return roleService.createRole(roleRequest);
		
	}
	
	@GetMapping("/getAllRoles")
	public ResponseEntity<List<Role>> getAllRoles() {
		return roleService.getAll();
	}

}
