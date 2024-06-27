package com.sivo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
	


	public Role(String roleName, String roleDescription, List<Permission> permissionList) {
		this.roleDescription = roleDescription;
		this.roleName = roleName;
		this.permissionList = permissionList;
	}

}
