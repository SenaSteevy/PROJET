package com.sivo.response;

import java.time.Duration;
import java.time.LocalDateTime;

import com.sivo.resources.TaskDescription;

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

public class JobResponse {
	
	private int numOrder;
	
	private TaskDescription taskDescription;
	
	private String type;
	
	private LocalDateTime dueDate;
	
	private LocalDateTime startDate;

	private Duration leadTime;

	private int priority;

	private String status;

}
