package com.sivo.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.entity.Permission;
import com.sivo.entity.Role;
import com.sivo.request.RoleRequest;
import com.sivo.response.RoleResponse;
import com.sivo.service.RoleService;


@RestController
@RequestMapping("/roles")

public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	
	@PostConstruct
	public void initPermissions() {
		
		roleService.initPermissions();
	}
	
	
	@PostMapping("new")
	public RoleResponse createRole(@RequestBody RoleRequest roleRequest) {
		
		return roleService.createRole(roleRequest);
		
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Role>> getAllRoles() {
		return roleService.getAll();
	}
	
	@GetMapping("/permissions/getAll")
	public ResponseEntity<List<Permission>> getAllPermissions() {
		return roleService.getAllPermissions();
	}
}
