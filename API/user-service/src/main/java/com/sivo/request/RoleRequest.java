package com.sivo.request;

import java.util.List;

import com.sivo.entity.Permission;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

public class RoleRequest {
	
	private String roleName;
	
	private String RoleDescription;
	
	private List<Permission> permissionList;

}
