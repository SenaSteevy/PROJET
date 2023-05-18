package com.sivo.response;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
public class TimeslotResponse {
	


	private long id;
	
	private DayOfWeek dayOfWeek;
	
    private LocalTime startTime;
   
	private LocalTime endTime;
	
	private PhaseResponse phaseResponse;
	


	
}
