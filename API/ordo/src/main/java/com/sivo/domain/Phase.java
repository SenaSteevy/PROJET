package com.sivo.domain;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sivo.response.PhaseResponse;

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
	@Column(name = "id")
	private long id;
	
	@Include
	@Column(name="name")
	private String name;
	
	@Column(name = "capacity")
	private int capacity;
	
	@Column( name = "duration")
	private Duration duration;
	
	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "phase", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Task> taskList;
	
	public Phase(PhaseResponse phaseResponse) {

		this.id = phaseResponse.getId();
		this.name = phaseResponse.getName();
		this.capacity = phaseResponse.getCapacity();
		this.duration = phaseResponse.getDuration();

	}


	

}
