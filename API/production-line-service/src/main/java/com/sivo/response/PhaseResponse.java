package com.sivo.response;


import java.time.Duration;

import com.sivo.entity.Phase;

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
	
	public PhaseResponse(Phase phase) {
		this.id = phase.getId();
		this.name = phase.getName();
		this.capacity = phase.getCapacity();
		this.duration = phase.getDuration();
	}

	private long id;
	
	private String name;
	
	private int capacity;
	
	private Duration duration;
	
	
	
}
