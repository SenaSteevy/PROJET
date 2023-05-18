package com.sivo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sivo.proxy.PhaseProxy;
import com.sivo.repository.JobRepository;
import com.sivo.repository.TaskDescriptionRepository;
import com.sivo.repository.TaskRepository;
import com.sivo.resources.TaskDescription;

@Service
public class TaskDescriptionService {

	@Autowired
	TaskDescriptionRepository taskDescriptionRepository;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	PhaseProxy phaseProxy ;

	public ResponseEntity<?> getById(Long id) {

		Optional<TaskDescription> taskDescription = taskDescriptionRepository.findById(id);
		if (taskDescription.isPresent()) {
			return ResponseEntity.ok().body(taskDescription.get());
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> createTaskDescription(TaskDescription taskDescription) {

		Optional<TaskDescription> optionalTaskDescription = taskDescriptionRepository.findById(taskDescription.getId());
		if (optionalTaskDescription.isPresent())
			return ResponseEntity.ok("existe deja !");
		
		TaskDescription newTaskDescription = taskDescriptionRepository.save(taskDescription);
		return ResponseEntity.ok().body(newTaskDescription);
	}

	public ResponseEntity<?> updateTaskDescription(Long id, TaskDescription taskDescription) {

		Optional<TaskDescription> OptionalTaskDescription = taskDescriptionRepository.findById(id);
		if (OptionalTaskDescription.isPresent()) {
			TaskDescription updatedTaskDescription = taskDescriptionRepository.save(taskDescription);
			return ResponseEntity.ok().body(updatedTaskDescription);
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> deleteById(Long id) {

		Optional<TaskDescription> OptionalTaskDescription = taskDescriptionRepository.findById(id);
		if (OptionalTaskDescription.isPresent()) {
			taskDescriptionRepository.deleteById(id);
			return ResponseEntity.ok().body("La description de tâches d'ID " + id + " a bien été supprimée.");
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok().body(taskDescriptionRepository.findAll());
	}

	public ResponseEntity<?> refreshDB() throws IOException {

		InputStream file = getClass().getClassLoader().getResourceAsStream("data/sivo.xlsx");

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheet("Sheet 1");
		
		taskRepository.deleteAll();

		jobRepository.deleteAll();

		taskDescriptionRepository.deleteAll();

		int maxRows = 500;
		for (int i = 1; i < maxRows; i++) {
			Row row = sheet.getRow(i);
			TaskDescription taskDescription = new TaskDescription();

			//id
			taskDescription.setId( Long.parseLong(row.getCell(1).getStringCellValue()));
			
			// codeOrder
			taskDescription.setCodeOrder(row.getCell(4).getStringCellValue());

			// libellé
			taskDescription.setDescription(row.getCell(5).getStringCellValue());

			// supplément
			taskDescription.setSupplement(row.getCell(7).getStringCellValue());

		

			taskDescriptionRepository.save(taskDescription);

			System.out.println("TaskDescription " + (i+1) + " créé.");
		}

		workbook.close();
		file.close();
		return ResponseEntity.ok("Les descriptions de âches ont été reinitialisées !");
	}

	
}
