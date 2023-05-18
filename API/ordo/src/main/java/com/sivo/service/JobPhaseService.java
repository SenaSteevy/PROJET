package com.sivo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivo.domain.Job;
import com.sivo.proxy.PhaseProxy;
import com.sivo.repository.JobRepository;
import com.sivo.repository.TaskDescriptionRepository;

@Service
public class JobPhaseService {
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	TaskDescriptionRepository taskDescriptionRepository;
	
	@Autowired
	PhaseProxy phaseProxy ;

	public void createJobPhase(Job jobRequest) {
		// TODO Auto-generated method stub
		
	}
	
	

}
