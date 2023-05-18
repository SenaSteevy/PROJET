package com.sivo.request;

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

public class RegisterRequestRequest {

	private String firstName;
	
    private String lastName;
	
    private String email;
	
    private String gender;
	
    private String poste;
    
   
}

