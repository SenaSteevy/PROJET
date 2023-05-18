package com.sivo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sivo.request.RoleRequest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
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

@Entity
@Table(name = "role")
public class Role {

	
	@Id
	@Include
	private String roleName;
	
	private String roleDescription;
	
	public Role(RoleRequest roleRequest) {
		this.roleName = roleRequest.getRoleName();
		this.roleDescription = roleRequest.getRoleDescription();
	}

	
}
