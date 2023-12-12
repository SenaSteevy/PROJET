package com.sivo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.REMOVE)
	@JoinColumn(name = "job_id")
	@JsonIgnore
	private Job job;
	
	@ManyToOne
	@JoinColumn(name = "treatment")
	private Treatment treatment;
	
	@Column(name = "status")
	private String status;
	
	@Column( name ="startTime")
	@PlanningVariable(valueRangeProviderRefs = {"startTimeRange"})
	private LocalDateTime  startTime ;
	
	@Column( name ="realStartTime")
	private LocalDateTime  realStartTime ;
	
	@Column(name = "timeRangeStartTime")
	private LocalDateTime timeRangeStartTime;
	
	 @ValueRangeProvider(id = "startTimeRange")
	 public  List<LocalDateTime> DateTimeRange() {
		 
	        List<LocalDateTime> dateTimeList = new ArrayList<>();
	        
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        
	        if(this.timeRangeStartTime != null) {
	        	
	        	currentDateTime = this.timeRangeStartTime;
	        }else 
	        	if(this.getJob().getDueDate().isBefore(currentDateTime)) {
	        		currentDateTime = this.getJob().getCreatedAt();
	        	}

	        
	        while (currentDateTime.isBefore(this.getJob().getDueDate())) {
	            dateTimeList.add(currentDateTime);
	            currentDateTime = currentDateTime.plus(this.treatment.getPhase().getDuration());
	        }
	        
	        if(dateTimeList.isEmpty()) {
	        
	        	while (currentDateTime.isBefore(this.job.getCreatedAt().plusDays(2))) {
		            dateTimeList.add(currentDateTime);
		            currentDateTime = currentDateTime.plus(this.treatment.getPhase().getDuration());
		        }
	        }
	        return dateTimeList;
	    }
	 
	 public int getPhaseId() {
		return  (int) this.treatment.getPhase().getId();
	 }


	 public Phase getPhase() {
		 return this.treatment.getPhase();
	 }

	public Task(Treatment treatment2) {
		this.treatment = treatment2;
		this.status = "UNDONE";
		this.timeRangeStartTime = LocalDateTime.of(2023, 1, 1, 18, 20);
	}
	
	 

	  


	
}
