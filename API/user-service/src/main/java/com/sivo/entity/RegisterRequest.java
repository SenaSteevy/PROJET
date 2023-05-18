package com.sivo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sivo.request.RegisterRequestRequest;

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

@Entity
@Table(name= "RegisterRequest")
public class RegisterRequest {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long  id;
   
	@Column(name="firstName")
	private String firstName;
	
	@Column(name = "lastName")
    private String lastName;
	
	@Column(name ="email")
    private String email;
	
	@Column(name ="gender")
    private String gender;
	
	@Column(name ="poste")
    private String poste;
	
	 public RegisterRequest(RegisterRequestRequest registerRequest) {
	    	this.firstName = registerRequest.getFirstName();
	    	this.lastName = registerRequest.getLastName();
	    	this.email = registerRequest.getEmail();
	    	this.gender = registerRequest.getGender();
	    	this.poste = registerRequest.getPoste();
	    			
	    }
}
