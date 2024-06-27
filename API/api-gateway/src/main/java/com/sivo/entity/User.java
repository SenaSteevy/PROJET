
package com.sivo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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