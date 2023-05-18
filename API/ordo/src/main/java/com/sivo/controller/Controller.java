package com.sivo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.request.JobRequest;
import com.sivo.resources.TaskDescription;
import com.sivo.service.JobPhaseService;
import com.sivo.service.SchedulerService;
import com.sivo.service.TaskDescriptionService;

@RestController
@RequestMapping("/api/geneticScheduler")

public class Controller {
	
	@Autowired
	SchedulerService service;
	
	@Autowired
	TaskDescriptionService taskDescriptionService;
	
	@Autowired
	JobPhaseService jobPhaseService;
	
	@PostMapping("/createJob")
	public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest){
		return service.createJob(jobRequest) ;
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		
		return service.getById(id) ;
	}

	@PostMapping("/updateJob/{id}")
	public ResponseEntity<?> updateJob(@PathVariable Integer id, @RequestBody JobRequest jobRequest){
		
		return service.updateJob(id, jobRequest) ;
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		
		return service.deleteById(id) ;
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(){
		
		return service.getAll() ;
	}
	
	@GetMapping("/getAllTasks")
	public ResponseEntity<?> getAllTasks(){
		
		return service.getAllTasks() ;
	}
	
	@PostMapping("/updateJobStatus/{id}")
	public ResponseEntity<?> updateJobStatus(@PathVariable Integer id, @RequestParam String status ){
		
		return service.updateJobStatus(id, status) ;
	}
	
	
	@PostMapping("/createTaskDescription")
	public ResponseEntity<?> createTaskDescription(@RequestBody TaskDescription taskDescription){
		return taskDescriptionService.createTaskDescription(taskDescription);
	}
	
	@GetMapping("/getTaskDescriptionById/{id}")
	public ResponseEntity<?> getTaskDescriptionById(@PathVariable Long id){
		return taskDescriptionService.getById(id);
	}
	
	@PostMapping("/updateTaskDescription/{id}")
	public ResponseEntity<?> updateTaskDescription(@PathVariable Long id , @RequestBody TaskDescription taskDescription){
		return taskDescriptionService.updateTaskDescription(id, taskDescription);
	}
	
	@PostMapping("/deleteTaskDescriptionById/{id}")
	public ResponseEntity<?> deleteDescriptionTask(@PathVariable Long id){
		return taskDescriptionService.deleteById(id);
	}
	
	@GetMapping("/getAllTaskDescription")
	public ResponseEntity<?> getAllTaskDescription(){
		return taskDescriptionService.getAll();
	}
	
	@PostMapping("/refreshTaskDescription")
	public ResponseEntity<?> refreshTaskDescription(){
		try {
			return taskDescriptionService.refreshDB() ;
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
		}
	}
	
	@PostMapping("/refresh-dataBase")
	public  ResponseEntity<?> refreshDB(){
		
		try {
			return service.refreshDB() ;
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
		}
	}
	
	
	@PostMapping("/solve")
	public ResponseEntity<?> solve() {
		
		return service.solve();
		
	}
	
	

}
