package com.sivo.entity;

import java.io.Serializable;
import java.time.Duration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sivo.request.PhaseRequest;

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
@Table(name = "phase")
public class Phase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Include
	@Column(name="name")
	private String name;
	
	@Column(name = "capacity")
	private int capacity;
	
	@Column( name = "duration")
	private Duration duration;
	
	
	
	public Phase(PhaseRequest phaseRequest) {
		this.name = phaseRequest.getName();
		this.capacity = phaseRequest.getCapacity();
		this.duration = phaseRequest.getDuration();
	}
	
}
