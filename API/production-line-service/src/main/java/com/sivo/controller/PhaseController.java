package com.sivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.request.PhaseRequest;
import com.sivo.request.TimeslotRequest;
import com.sivo.response.PhaseResponse;
import com.sivo.response.TimeslotResponse;
import com.sivo.service.PhaseService;

@RestController
@RequestMapping("/api/phase")
public class PhaseController {
	
	@Autowired
	PhaseService phaseService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createPhase(@RequestBody PhaseRequest phaseRequest) {
		return phaseService.createPhase(phaseRequest);
	}
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deletePhaseById(@PathVariable("id") long id) {
		return phaseService.deletePhaseById(id);
	}
	
	@PostMapping("/update/{id}")
	public ResponseEntity<?> updatePhase(@PathVariable("id") long id, @RequestBody PhaseRequest phaseRequest) {
		return phaseService.updatePhaseById( id, phaseRequest);
	}
	
	@GetMapping("/getById/{id}")
	public PhaseResponse getPhaseById(@PathVariable("id") long id) {
		return phaseService.getPhaseByid(id);
	}
	


	
	@PostMapping("/{id}/createTimeslot")
	public ResponseEntity<?> createTimeslot(@RequestBody TimeslotRequest timeslotRequest, @PathVariable("id") long id) {
		return phaseService.createTimeslot(id, timeslotRequest);
	}
	
	@PostMapping("/{id}/updateTimeslot/{timeslot_id}")
	public ResponseEntity<?>updateTimeslotById(@PathVariable("id") long id
			,@PathVariable("timeslot_id") long timeslot_id, @RequestBody TimeslotRequest timeslotRequest)
	{
		return phaseService.updateTimeslotById(id,timeslot_id, timeslotRequest);
	}
	
	@PostMapping("/deleteTimeslot/{timeslot_id}")
	public ResponseEntity<?> deleteTimeslotById(@PathVariable("timeslot_id") long timeslot_id) {
		return phaseService.deleteTimeslotById(timeslot_id);
	}
	
	@GetMapping("/getTimeslot/{timeslot_id}")
	public ResponseEntity<?> getTimeslotById(@PathVariable("timeslot_id") long timeslot_id) {
		return phaseService.getTimeslotById(timeslot_id);
	}
	
	@GetMapping("/getAll")
	public List<PhaseResponse> getAllPhase() {
		return phaseService.getAllPhase();
	}
	
	@GetMapping("/{id}/getTimeslotList")
	public List<TimeslotResponse> getAllTimeslotByPhaseId(@PathVariable("id") long id) {
		return phaseService.getAllTimeslotByPhaseId(id);
	}
}
