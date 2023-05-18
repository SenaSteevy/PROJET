package com.sivo.response;


import java.time.Duration;

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

public class PhaseResponse {
	
	

	private long id;
	
	private String name;
		
	private int capacity;
	
	private Duration duration;
	
	
}
