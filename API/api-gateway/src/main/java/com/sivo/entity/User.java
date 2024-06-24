package com.sivo.entity;

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


public class User {

	
	private Long id ;
	
	private String email;
	
	
	private String gender;

	private String firstName;

	private String lastName;

	private String password;
	
	private String post;
	
	

	
	private Role role;

	

	public User(String email, String gender, String firstName, String lastName, String password, String post,
			Role role) {
		this.email = email;
		this.gender = gender;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.post = post;
		this.role = role;
	}
	


}