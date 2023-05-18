package com.sivo.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sivo.request.JobRequest;
import com.sivo.resources.TaskDescription;
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
@Table(name = "job")
public class Job {

	@Id
	@Include
	@Column(name = "num_order")
	private Integer numOrder;

	@ManyToOne
	@JoinColumn(name = "task_description", nullable = false)
	private TaskDescription taskDescription;

	@Column(name = "type")
	private String type;

	@Column(name = "due_date")
	private LocalDateTime dueDate;

	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Task> taskList;

	// la valeur sera décidée à partir de l'ordre de chaque solution
	@Column(name = "startDateTime")
	private LocalDateTime startDateTime;

	@Column(name = "leadTime")
	private Duration leadTime;

	@Column(name = "priority")
	private int priority;

	@Column(name = "status")
	private String status;

	public Job(JobRequest jobRequest) {

		this.numOrder = jobRequest.getNumOrder();
		this.taskDescription = jobRequest.getTaskDescription();
		this.type = jobRequest.getType();
		this.dueDate = jobRequest.getDueDate();
		this.startDateTime = null;
		this.leadTime = jobRequest.getLeadTime();
		this.priority = jobRequest.getPriority();
		this.status = jobRequest.getStatus();
	}

}
