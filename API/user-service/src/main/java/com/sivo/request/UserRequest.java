package com.sivo.request;

import java.util.Set;

import com.sivo.entity.ImageModel;
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

public class UserRequest {
	
	private Long id;

	private String gender;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private Set<Role> roles;
	
	private String post;
	
	private ImageModel profile ;

}