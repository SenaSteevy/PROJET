package com.sivo.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sivo.response.PhaseResponse;
import com.sivo.response.TimeslotResponse;

@FeignClient(value = "production-line-service", url = "localhost:8081")
public interface PhaseProxy {
    
	
	@GetMapping("/api/phase/getAll")
	List<PhaseResponse> getPhaseList();
	
	@GetMapping("/api/phase/{id}/getTimeslotList")
	List<TimeslotResponse> getAllTimeslotByPhaseId(@PathVariable("id") long id);
	
	@GetMapping("api/phase/getById/{id}")
	PhaseResponse getPhaseById(@PathVariable("id") long id);
}
