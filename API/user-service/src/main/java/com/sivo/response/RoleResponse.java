package com.sivo.response;

import com.sivo.entity.Role;

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


public class RoleResponse {

	private String roleName;
	
	private String roleDescription;
	
	public RoleResponse( Role role) {
		this.roleName = role.getRoleName();
		this.roleDescription = role.getRoleDescription();
	}
	
}
