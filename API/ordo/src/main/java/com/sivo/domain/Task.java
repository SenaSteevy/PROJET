package com.sivo.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

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
@Table(name = "Task")
@PlanningEntity
public class Task implements Serializable {

	
	


	private static final long serialVersionUID = 1L;

	@PlanningId
	@Id
	@Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "job")
	private Job job;
	
	@ManyToOne
	@JoinColumn(name = "phase")
	private Phase phase;
	
	@Column(name ="type")
	private String type;
	
	@Column(name = "status")
	private String status;
	
	@Column( name ="startTime")
	@PlanningVariable(valueRangeProviderRefs = {"startTimeRange"})
	private LocalDateTime  startTime ;
	
	
	 @ValueRangeProvider(id = "startTimeRange")
	 public  List<LocalDateTime> DateTimeRange() {
		 
	        List<LocalDateTime> dateTimeList = new ArrayList<>();

//	        LocalDateTime currentDateTime = LocalDateTime.now();

	        LocalDateTime currentDateTime = LocalDateTime.of(2023, 1, 1, 18, 20);
	        
	        while (currentDateTime.isBefore(this.getJob().getDueDate())) {
	            dateTimeList.add(currentDateTime);
	            currentDateTime = currentDateTime.plus(this.getPhase().getDuration());
	        }

	        return dateTimeList;
	    }
	 
	 public int getPhaseId() {
		return  (int) this.phase.getId();
	 }

	public Task(Job job2, Phase phase2) {
		
		this.job = job2;
		this.phase = phase2;
		this.status = "UNDONE";

	}

	public Task(Job job2, Phase antiReflet, String taskType) {
		this.job = job2;
		this.phase = antiReflet;
		this.type = taskType;
		this.status = "UNDONE";
	}
	
}
