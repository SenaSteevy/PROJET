package com.sivo.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/getRoleByName/{rolename}")
	public ResponseEntity<Role> getRoleByName(@PathVariable("rolename") String roleName) {
		return roleService.getRoleByName(roleName);
	}
	
	@PostMapping(value = {"/updateRoleById/{id}"})
	public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @RequestBody RoleRequest roleRequest){
			return roleService.updateRole(id, roleRequest);		
	}
	
	@GetMapping("getById/{id}")
	public ResponseEntity<?> findRoleById (@PathVariable("id") Long id) {
		return roleService.findById(id);
	}
	

	
	@DeleteMapping("/deleteRoleById/{id}")
	public ResponseEntity<?> deleteRoleById(@PathVariable("id") Long id){
		return roleService.deleteRoleById(id);
	}
}
