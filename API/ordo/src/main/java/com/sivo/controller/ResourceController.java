package com.sivo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivo.request.ResourceRequest;
import com.sivo.resource.Resource;
import com.sivo.service.ResourceService;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin
public class ResourceController {

	@Autowired
	ResourceService resourceService;

	@GetMapping("/getAllResources")
	public ResponseEntity<List<Resource>> getAllResources() {
		
		return resourceService.getAllResources();
	}
	
	@GetMapping("/getResourceById/{id}")
	public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
		
		return resourceService.getResourceById(id);
	}
	
	@PostMapping("/saveResource")
	public  ResponseEntity<Resource> saveResource(@RequestBody ResourceRequest resourceRequest ) {
		
		return resourceService.saveResource(resourceRequest);
	}
	
	@PostMapping("/deleteResourceById/{id}")
	public ResponseEntity<Resource> deleteResourceById(@PathVariable Long id){
		
		return resourceService.deleteResourceById(id);
	}
	
	@PostMapping("/updateResourceById/{id}")
	public ResponseEntity<Resource> updateResourceById(@PathVariable Long id,@RequestBody ResourceRequest resourceRequest ){
		
		return resourceService.updateResourceById(id,resourceRequest);
	}
	
	
	
}
