package com.sivo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String roleName;

	@Column(name = "description")
	private String roleDescription;

	public Role(RoleRequest roleRequest) {
		this.roleName = roleRequest.getRoleName();
		this.roleDescription = roleRequest.getRoleDescription();
	}

	public Role(String roleName, String roleDescription) {
		this.roleDescription = roleDescription;
		this.roleName = roleName;
	}

}
