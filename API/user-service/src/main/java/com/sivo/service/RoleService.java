package com.sivo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sivo.entity.Permission;
import com.sivo.entity.Role;
import com.sivo.repository.PermissionRepository;
import com.sivo.repository.RoleRepository;
import com.sivo.request.RoleRequest;
import com.sivo.response.RoleResponse;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PermissionRepository permissionRepository;
	
	public RoleResponse createRole(RoleRequest roleRequest) {
		
		Role role = roleRepository.save( new Role(roleRequest) );
		
		return new RoleResponse(role);
	}

	public ResponseEntity<List<Role>> getAll() {

		List<Role> roles = roleRepository.findAll();
		return ResponseEntity.ok(roles);
	}

	public void initPermissions() {
		
		List<Permission> permissionList = permissionRepository.findAll();
		if(permissionList.isEmpty()) {
			
			permissionList.add( new Permission("MANAGE USERS"));
			permissionList.add( new Permission("MANAGE ROLES"));
			permissionList.add( new Permission("MANAGE USER REQUESTS"));
			permissionList.add( new Permission("MANAGE PLANNINGS"));
			permissionList.add( new Permission("MANAGE AUTOPLANNING"));
			permissionList.add( new Permission("MANAGE CLIENTS"));
			permissionList.add( new Permission("MANAGE PHASES"));
			permissionList.add( new Permission("MANAGE TREATMENTS"));
			permissionList.add( new Permission("MANAGE RESOURCES"));
			permissionList.add( new Permission("MANAGE ORDERS"));
			permissionList.add( new Permission("MANAGE SCHEDULER"));
			
			permissionRepository.saveAll(permissionList);
			
			
		}
		
	}

	public ResponseEntity<List<Permission>> getAllPermissions() {
		
		List<Permission> permissionList = permissionRepository.findAll();
		return ResponseEntity.ok(permissionList);
	}
	

}
