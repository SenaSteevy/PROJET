package com.sivo.entity;

import java.util.List;

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


public class Role {

	
	private Long id;

	private String roleName;

	private String roleDescription;
	
	

	private List<Permission> permissionList;
	



	public Role(String roleName, String roleDescription, List<Permission> permissionList) {
		this.roleDescription = roleDescription;
		this.roleName = roleName;
		this.permissionList = permissionList;
	}

}
