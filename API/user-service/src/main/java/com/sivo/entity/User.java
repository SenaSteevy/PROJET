package com.sivo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sivo.request.UserRequest;

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
@Table(name = "user")
public class User {

	@Id
	@Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id ;
	
	@Column(name = "email")
	private String email;
	
	
	@Column( name = "gender")
	private String gender;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "password")
	private String password;
	
	@Column(name = "post")
	private String post;
	
	

	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "role")
	private Role role;

	public User(UserRequest userRequest) {
		
		this.gender = userRequest.getGender();
		this.firstName = userRequest.getFirstName();
		this.lastName = userRequest.getLastName();
		this.email = userRequest.getEmail();
		this.password = userRequest.getPassword();
		this.role = userRequest.getRole();
		this.post = userRequest.getPost();
	}

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