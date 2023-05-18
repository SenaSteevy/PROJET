package com.sivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.request.RoleRequest;
import com.sivo.response.RoleResponse;
import com.sivo.service.RoleService;


@RestController
@RequestMapping("/api/role")

public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@PostMapping("create")
	public RoleResponse createRole(@RequestBody RoleRequest roleRequest) {
		
		return roleService.createRole(roleRequest);
		
	}

}
