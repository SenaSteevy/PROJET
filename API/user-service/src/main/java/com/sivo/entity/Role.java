package com.sivo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH )
	@JoinTable(name = "ROLES_PERMISSIONS", joinColumns = { @JoinColumn(name = "PERMISSION_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private List<Permission> permissionList;
	

	public Role(RoleRequest roleRequest) {
		this.roleName = roleRequest.getRoleName();
		this.roleDescription = roleRequest.getRoleDescription();
	}

	public Role(String roleName, String roleDescription, List<Permission> permissionList) {
		this.roleDescription = roleDescription;
		this.roleName = roleName;
		this.permissionList = permissionList;
	}

}
