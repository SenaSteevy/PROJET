package com.sivo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sivo.domain.Job;
import com.sivo.domain.Phase;
import com.sivo.domain.Planning;
import com.sivo.domain.Schedule;
import com.sivo.domain.Task;
import com.sivo.domain.Treatment;
import com.sivo.repository.ClientRepository;
import com.sivo.repository.JobRepository;
import com.sivo.repository.PhaseRepository;
import com.sivo.repository.PlanningRepository;
import com.sivo.repository.ResourceRepository;
import com.sivo.repository.TaskRepository;
import com.sivo.repository.TreatmentRepository;
import com.sivo.request.ClientRequest;
import com.sivo.request.JobRequest;
import com.sivo.resource.AutoPlanning;
import com.sivo.resource.Client;
import com.sivo.resource.Resource;
import com.sivo.resource.Timeslot;

@Service
@Transactional
public class SchedulerService {
	
	private AutoPlanning autoPlanning = new AutoPlanning("OFF");

	@Autowired
	JobRepository jobRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TreatmentRepository treatmentRepository;

	@Autowired
	PhaseRepository phaseRepository;

	@Autowired
	PlanningRepository planningRepository;

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	ResourceRepository resourceRepository;

	String[] colors = { "MONOCOLORE", "BICOLORE", "BRUN", "TEINTE", "GRIS", "BLACK", "SUN", "MARRON", "BURGUNDY",
			"SCOTOVUE", "QUARTZ", "LEMON", "ROSE", "ORANGE" };
	List<String> colorations = new ArrayList<String>(Arrays.asList(colors));

	String[] AR = { "PREVENCIA", "BLUE", "CRIZAL", "PREMIUM", "ALIZE", "SAPPHIRE", "MIRROR CX", "UV", "DRIVE",
			"OPTIFOG" };
	List<String> typeAR = new ArrayList<String>(Arrays.asList(AR));

	Phase impression;
	Phase surfacage;
	Phase coloration;
	Phase resys;
	Phase antiReflet;
	
	public ResponseEntity<?> setAutoPlanning(String value) {
		this.autoPlanning.setValue(value);
		return ResponseEntity.ok().build();
	}
	
	public ResponseEntity<?> getAutoPlanning() {
		return ResponseEntity.ok(this.autoPlanning);
	}

	public Schedule solve(Schedule problem) {

		// Submit the problem to start solving
		SolverConfig solverConfig = SolverConfig.createFromXmlResource("solverConfig.xml");
	//	solverConfig.withTerminationConfig(new TerminationConfig().withMinutesSpentLimit((long) 60));

		SolverFactory<Schedule> solverFactory = SolverFactory.create(solverConfig);
		Solver<Schedule> solver = solverFactory.buildSolver();

		Schedule solution;
		try {
			// Wait until the solving ends
			solution = solver.solve(problem);
		} catch (Exception e) {
			throw new IllegalStateException("Solving failed.", e);
		}
		return solution;
	}

	public ResponseEntity<?> solve() {

		List<Task> taskList = taskRepository.findByStatusIgnoreCaseLike("UNDONE");

		if (taskList.isEmpty())
			System.out.println("taskList empty");

		taskList.stream().forEach(task -> task.setStartTime(null));
		// solving
		Schedule bestSolution = solve(new Schedule(taskList));

		for (Task task : bestSolution.getTasks()) {
			task.getJob().updateTask(task);
			Job job = jobRepository.save(task.getJob());
		}

		List<Job> planning = getJobList(bestSolution);

		if (planning.isEmpty())
			System.out.println("jobList is empty");

		planning = jobRepository.saveAll(planning);
		Planning solution = new Planning(planning);
		solution = planningRepository.save(solution);
		System.out.println(" PLANNING DONE");
		return ResponseEntity.ok().body(solution);
	}

	public ResponseEntity<?> createJob(JobRequest jobRequest) {

		Optional<Job> optionalJob = jobRepository.findById(jobRequest.getNumOrder());
		if (optionalJob.isPresent()) {
			return ResponseEntity.ok().build();
		}
		Job job = new Job(jobRequest);
		job = jobRepository.save(job);

//		if (job.getTaskList() != null) {
//			for (Task task : job.getTaskList()) {
//
//				task.setJob(job);
//				task = taskRepository.save(task);
//			}
//		}

		return ResponseEntity.ok().body(job);
	}

	public ResponseEntity<?> updateJob(Integer id, JobRequest jobRequest) {
		Optional<Job> jobOptional = jobRepository.findById(id);
		if (jobOptional.isPresent()) {
			Job existingJob = jobOptional.get();
			Job updatedJob = new Job(jobRequest);

			// Update the properties of existingJob from updatedJob
			existingJob.setCodeOrder(updatedJob.getCodeOrder());
			existingJob.setClient(updatedJob.getClient());
			existingJob.setDescription(updatedJob.getDescription());
			existingJob.setSupplement(updatedJob.getSupplement());
			existingJob.setType(updatedJob.getType());
			existingJob.setDueDate(updatedJob.getDueDate());
			existingJob.setLeadTime(updatedJob.getLeadTime());
			existingJob.setPriority(updatedJob.getPriority());
			existingJob.setCreatedAt(updatedJob.getCreatedAt());
			existingJob.setDoneAt(updatedJob.getDoneAt());
			existingJob.setResource(updatedJob.getResource());
			existingJob.setStartDateTime(updatedJob.getStartDateTime());
			existingJob.setStatus(updatedJob.getStatus());

			// Update tasks in existingJob's taskList based on updatedJob's taskList
			for (Task updatedTask : updatedJob.getTaskList()) {
				Task existingTask = existingJob.getTaskList().stream()
						.filter(task -> task.getId() == updatedTask.getId()).findFirst().orElse(null);

				if (existingTask != null) {
					existingTask.setTreatment(updatedTask.getTreatment());
					existingTask.setStartTime(updatedTask.getStartTime());
					existingTask.setRealStartTime(updatedTask.getRealStartTime());
					existingTask.setStatus(updatedTask.getStatus());
					existingTask.setJob(updatedJob);
					// ... update other task properties
				} else {
					existingJob.getTaskList().add(updatedTask);
				}
			}

			// Save the updated job along with the updated tasks
			Job savedJob = jobRepository.save(existingJob);
			return ResponseEntity.ok().body(savedJob);
		}

		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> deleteById(Integer id) {

		Optional<Job> job = jobRepository.findById(id);
		if (job.isPresent()) {
			jobRepository.deleteById(id);
			taskRepository.deleteInBatch(job.get().getTaskList());
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok().body(jobRepository.findAll());
	}

	public ResponseEntity<?> refreshDB() throws IOException {

		InputStream file = getClass().getClassLoader().getResourceAsStream("data/sivo.xlsx");

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheet("Sheet 1");

		taskRepository.deleteAll();
		planningRepository.deleteAll();
		jobRepository.deleteAll();
		clientRepository.deleteAll();
		resourceRepository.deleteAll();

		resetPhases();

		// resource
		Resource resourceFini = new Resource("paire de verres", "simple", 1480);
		resourceFini = resourceRepository.save(resourceFini);

		Resource resourceSemiFini = new Resource("paire de verres", "semi-finie", 520);
		resourceSemiFini = resourceRepository.save(resourceSemiFini);

		// client
		ClientRequest clientRequest = new ClientRequest("sena steevy", "rue 7 novembre", "anesyveets@gmail.com",
				" +216 52000008");
		Client client = new Client(clientRequest);
		client = clientRepository.save(client);

		createAllTreatment();

		int maxRows = 2000;
		for (int i = 1; i < maxRows; i++) {
			Row row = sheet.getRow(i);
			JobRequest jobRequest = new JobRequest();

			// codeOrder
			jobRequest.setCodeOrder(row.getCell(4).getStringCellValue());

			// Client
			jobRequest.setClient(client);

			// description
			jobRequest.setDescription(row.getCell(5).getStringCellValue());

			// supplement
			jobRequest.setSupplement(row.getCell(7).getStringCellValue());

			// type
			jobRequest.setType(row.getCell(3).getStringCellValue());

			// Resource
			if (jobRequest.getType().equalsIgnoreCase("F")) {
				jobRequest.setResource(resourceFini);
			} else {
				jobRequest.setResource(resourceSemiFini);

			}

			// dueDate
			LocalDateTime dateTime = getLocalDateTime(row.getCell(19));
			jobRequest.setDueDate(dateTime);

			// il n'y a pas de priorité définie dans le fichier excel
			jobRequest.setPriority(1);

			// Status
			jobRequest.setStatus("UNDONE");

			// doneAt
			jobRequest.setDoneAt(null);

			// createdAt
			LocalDateTime createdAt = getLocalDateTime(row.getCell(8));
			jobRequest.setCreatedAt(createdAt);

			// taskList
			String supplement = row.getCell(7).getStringCellValue();
			List<Task> taskList = getTaskList(i, row, supplement);
			for (Task task : taskList)
				task = taskRepository.save(task);

			jobRequest.setTaskList(taskList);
			ResponseEntity<?> response = createJob(jobRequest);

			System.out.println("Job " + (i + 1) + " créé.");
		}

		workbook.close();
		file.close();
		return ResponseEntity.ok("La base de Donnéés a été reinitialisée !");
	}

	private void resetPhases() {
		impression = createPhase("impression", 2, Duration.ofMinutes(2));
		surfacage = createPhase("surfaçage", 160, Duration.ofHours(2));
		coloration = createPhase("coloration", 15, Duration.ofMinutes(30));
		resys = createPhase("resys", 120, Duration.ofHours(2));
		antiReflet = createPhase("anti-reflet", 165, Duration.ofHours(3));
	}

	private Phase createPhase(String name, int capacity, Duration duration) {
		List<Timeslot> timeslotList = generateTimeSlotList();
		Phase phase = new Phase(name, capacity, duration, timeslotList);
		return phaseRepository.save(phase);
	}

	private List<Job> getJobList(Schedule schedule) {
		// les tâches d'impression
		List<Job> planning = schedule.getTasks().stream()
				.filter(task -> task.getPhase().getName().equalsIgnoreCase("impression"))
				.sorted(Comparator.comparing(Task::getStartTime)).map(task -> task.getJob())
				.collect(Collectors.toList());
		

		// setStartDateTime
		LocalDateTime startDateTime;
		LocalDateTime endDateTime;

		for (Job job : planning) {
			// startDateTime
			List<Task> tasks = schedule.getTasks().stream().filter(task -> task.getJob().equals(job))
					.sorted(Comparator.comparingInt(Task::getPhaseId)).collect(Collectors.toList());
			startDateTime = tasks.get(0).getStartTime();
			
			job.setStartDateTime(startDateTime);
			job.setTaskList(tasks);
			
			// endDateTime
			OptionalInt MaxPhaseIdOfJob = schedule.getTasks().stream().filter(task -> task.getJob().equals(job))
					.mapToInt(Task::getPhaseId).max();

			Task TaskWithMaxPhaseId = schedule.getTasks().stream()
					.filter(task -> task.getJob().equals(job))
					.filter(task -> task.getStartTime()!=null )
					.filter(task -> task.getPhaseId() == MaxPhaseIdOfJob.getAsInt()).findFirst().get();
				
				endDateTime = TaskWithMaxPhaseId.getStartTime().plus(TaskWithMaxPhaseId.getPhase().getDuration());
				
				job.setLeadTime(Duration.between(startDateTime, endDateTime));
			

		}
		
		return planning;

	}

	private void createAllTreatment() {

		// impression
		Treatment treatment = new Treatment("Impression", impression);
		treatment = treatmentRepository.save(treatment);

		// surfacage
		treatment = new Treatment("G : 1.5mm D : 1.6mm", surfacage);
		treatment = treatmentRepository.save(treatment);

		// coloration
		for (String color : colorations) {
			treatment = new Treatment(color, coloration);
			treatment = treatmentRepository.save(treatment);
		}

		// resys
		treatment = new Treatment("Resys", resys);
		treatment = treatmentRepository.save(treatment);

		// anti-reflet
		for (String ar : typeAR) {
			treatment = new Treatment(ar, antiReflet);
			treatment = treatmentRepository.save(treatment);
		}

	}

	private List<Task> getTaskList(int numOrder, Row row, String supplement) {
		List<Task> taskList = new ArrayList<Task>();

		// impression
		Treatment treatment = treatmentRepository.findByPhaseName("impression").get(0);
		taskList.add(new Task(treatment));

		// surfacage
		if (row.getCell(3).getStringCellValue().contains("F")) {
			treatment = treatmentRepository.findByPhaseName("surfaçage").get(0);
			taskList.add(new Task(treatment));
		}

		List<Treatment> treatments = new ArrayList<Treatment>();
		// coloration
		for (String color : colorations)
			if (supplement.toUpperCase().contains(color)) {
				treatments = treatmentRepository.findByPhaseName("coloration");
				treatment = treatments.stream().filter(item -> item.getDescription().equalsIgnoreCase(color))
						.findFirst().get();
				taskList.add(new Task(treatment));
				break;
			}

		// resys
		if (supplement.toUpperCase().contains("SUPRA")) {

			treatment = treatmentRepository.findByPhaseName("resys").get(0);
			taskList.add(new Task(treatment));
		}

		// AR
		for (String type : typeAR)
			if (supplement.toUpperCase().contains(type)) {
				treatments = treatmentRepository.findByPhaseName("anti-reflet");
				treatment = treatments.stream().filter(item -> item.getDescription().equalsIgnoreCase(type)).findFirst()
						.get();
				taskList.add(new Task(treatment));
				break;
			}
		return taskList;
	}

	public LocalDateTime getLocalDateTime(Cell cell) {

	    // Try to parse the cell value as a LocalDateTime object.
	    try {
	        // If the cell value is a numeric cell, use the Apache POI `DateUtil` class to parse the cell value.
	        
	            Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
	            return LocalDateTime.ofInstant(javaDate.toInstant(), ZoneId.systemDefault());
	       
	    } catch (Exception e) {
	        // If the cell value cannot be parsed as a LocalDateTime object, return null.
	        return null;
	    }
	}



	public ResponseEntity<?> getAllTasks() {

		return ResponseEntity.ok(taskRepository.findAll());
	}

	public ResponseEntity<?> getJobById(Integer id) {

		Optional<Job> job = jobRepository.findById(id);
		if (job.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(job.get());
	}

	public ResponseEntity<?> updateJobList(List<Job> jobList) {

		try {
			for (Job job : jobList)
				updateJob(job.getNumOrder(), new JobRequest(job));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok().build();
	}

	private static List<Timeslot> generateTimeSlotList() {
		List<Timeslot> timeSlotList = new ArrayList<>();

		DayOfWeek startDay = DayOfWeek.MONDAY;
		DayOfWeek endDay = DayOfWeek.SATURDAY;
		LocalTime startTime = LocalTime.of(8, 30);
		LocalTime endTime = LocalTime.of(17, 30);

		DayOfWeek currentDay = startDay;
		while (currentDay != endDay) {
			Timeslot timeslot = new Timeslot();
			timeslot.setDayOfWeek(currentDay);
			timeslot.setStartTime(startTime);
			timeslot.setEndTime(endTime);
			timeSlotList.add(timeslot);

			currentDay = currentDay.plus(1);
		}

		return timeSlotList;
	}
	
	private  int taskId;

	public ResponseEntity<?> solveByExcelFile(MultipartFile file) throws IOException {

       System.out.println("Debut de l'extraction ...");
		
      InputStream  inputStream = file.getInputStream(); 
	    // Use a lightweight Excel library.
	    Workbook workbook = new XSSFWorkbook(inputStream);

	    // Get a reference to the first sheet in the workbook.
	    Sheet sheet = workbook.getSheetAt(0);

	    // Remove the header row.
	    sheet.removeRow(sheet.getRow(0));

	    // Create a list to store the jobs.
	    List<Job> jobs = new ArrayList<>();
	    List<Task> taskList = new ArrayList<>();
	    // Iterate over the rows in the sheet and create a job object for each row.
	    this.taskId = 0;
	    for (int i=1 ; i <= sheet.getPhysicalNumberOfRows(); i++  ) {
	    	Row row = sheet.getRow(i);
		    Job job = new Job();

	        // Set the job code, description, supplement, type, due date, priority, status, and created date.
		    job.setNumOrder(Integer.parseInt(row.getCell(1).getStringCellValue()));
	        job.setCodeOrder(row.getCell(4).getStringCellValue());
	        job.setDescription(row.getCell(5).getStringCellValue());
	        job.setSupplement(row.getCell(7).getStringCellValue());
	        job.setType(row.getCell(3).getStringCellValue());
	        job.setCreatedAt(getLocalDateTime(row.getCell(8)));
	        
	        LocalDateTime dueDate = getLocalDateTime(row.getCell(19));
	        if(dueDate==null) {	        	
	        	dueDate = job.getCreatedAt().plusDays(2);
	        }
	        job.setDueDate(dueDate);
	        job.setPriority(1);
	        job.setStatus("UNDONE");
	        
	        // Get the task list for the job.
	        List<Task> tasks = getTaskList(0, row, job.getSupplement());
	        tasks.stream().forEach(item ->{
	        	item.setJob(job);
	        	item.setId(this.taskId);
	        	this.taskId++;
	        });
	        taskList.addAll(tasks);
	        // Set the time range start time for each task in the job.
	        tasks.stream().forEach(item -> item.setTimeRangeStartTime(job.getCreatedAt()));

	        // Add the tasks to the job.
	        job.setTaskList(tasks);

	        // Add the job to the list of jobs.
	        jobs.add(job);
	        System.out.println("row "+i+" Done");
	       
	    }
	    
	 // Close the FileInputStream object.
        inputStream.close();

	    // Close the Excel file.
	    workbook.close();
	    
		System.out.println("Extraction des données : OK");
		System.out.println("total jobs extracted : "+jobs.size());
		System.out.println("job.duedate : "+jobs.get(0).getDueDate());
		System.out.println("Début du Calcul...");
				
		Schedule schedule = new Schedule(taskList);
		schedule = solve(schedule);
		System.out.println("Calcul : OK");
		System.out.println("Début de la création du fichier Excel");

		List<Job> planning = getJobList(schedule);
		try{
			return exportTaskList(planning, "response");
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public ResponseEntity<byte[]> exportTaskList(List<Job> jobList, String fileName) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Orders");

		// Créer un tableau pour contenir les données
		Row headerRow = sheet.createRow(0);

		// Ajouter les colonnes au tableau
		Cell cell = headerRow.createCell(0);
		cell.setCellValue("NUM ORDER");
		cell = headerRow.createCell(1);
		cell.setCellValue("CODE ORDER");
		cell = headerRow.createCell(2);
		cell.setCellValue("TYPE");
		cell = headerRow.createCell(3);
		cell.setCellValue("DESCRIPTION");
		cell = headerRow.createCell(4);
		cell.setCellValue("SUPPLEMENT");
		cell = headerRow.createCell(5);
		cell.setCellValue("DATE CREATION");
		cell = headerRow.createCell(6);
		cell.setCellValue("DUEDATE");
		cell = headerRow.createCell(7);
		cell.setCellValue("DEBUT IMPRESSION");
		cell = headerRow.createCell(8);
		cell.setCellValue("DEBUT SURFACAGE");
		cell = headerRow.createCell(9);
		cell.setCellValue("DEBUT COLORATION");
		cell = headerRow.createCell(10);
		cell.setCellValue("DEBUT RESYS");
		cell = headerRow.createCell(11);
		cell.setCellValue("DEBUT ANTI-REFLET");
		cell = headerRow.createCell(12);
		cell.setCellValue("DATE DE FIN");
		cell = headerRow.createCell(13);
		cell.setCellValue("DUREE ");

		// Ajouter les données de la liste de tâches au tableau
		int rowCount = 1;
		for (Job job : jobList) {
			
			Row row = sheet.createRow(rowCount);

			cell = row.createCell(0);
			cell.setCellValue(job.getNumOrder());
			cell = row.createCell(1);
			cell.setCellValue(job.getCodeOrder());
			cell = row.createCell(2);
			cell.setCellValue(job.getType());
			cell = row.createCell(3);
			cell.setCellValue(job.getDescription());
			cell = row.createCell(4);
			cell.setCellValue(job.getSupplement());
			cell = row.createCell(5);
			cell.setCellValue(parseLocalDateTime(job.getCreatedAt()));
			cell = row.createCell(6);
			cell.setCellValue(parseLocalDateTime(job.getDueDate()));
			
			List<Task> tasks = job.getTaskList().stream().sorted(Comparator.comparingInt(Task::getPhaseId)).collect(Collectors.toList());
			Optional<Task> task;
			
			//DebutImpression
			cell = row.createCell(7);
			cell.setCellValue(parseLocalDateTime(tasks.get(0).getStartTime()));

			//DebutSurfacage
			cell = row.createCell(8);
			task = tasks.stream().filter( item -> item.getTreatment().getPhase().getName().equalsIgnoreCase("surfaçage")).findAny();
			if(task.isPresent()) {				
				cell.setCellValue(parseLocalDateTime(task.get().getStartTime()));
			}
			
			//DebutColoration
			cell = row.createCell(9);
			task = tasks.stream().filter( item -> item.getTreatment().getPhase().getName().equalsIgnoreCase("coloration")).findAny();
			if(task.isPresent()) {				
				cell.setCellValue(parseLocalDateTime(task.get().getStartTime()));
			}
			
			//DebutResys
			cell = row.createCell(10);
			task = tasks.stream().filter( item -> item.getTreatment().getPhase().getName().equalsIgnoreCase("resys")).findAny();
			if(task.isPresent()) {				
				cell.setCellValue(parseLocalDateTime(task.get().getStartTime()));
			}
			
			//DebutAR
			cell = row.createCell(11);
			task = tasks.stream().filter( item -> item.getTreatment().getPhase().getName().equalsIgnoreCase("anti-reflet")).findAny();
			if(task.isPresent()) {				
				cell.setCellValue(parseLocalDateTime(task.get().getStartTime()));
			}
			
				
			//DateFin
			cell = row.createCell(12);
			cell.setCellValue( parseLocalDateTime(job.getStartDateTime().plus(job.getLeadTime())));
				
			//LeadTime
			cell = row.createCell(13);
			cell.setCellValue(job.getLeadTime().toHoursPart()+ " h "+job.getLeadTime().toMinutesPart()+" min "+job.getLeadTime().toSecondsPart()+ " s ");

			rowCount++;
			System.out.println("row "+rowCount+" added");
		}
		
		for(int i = 0; i<=13; i++ ) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    workbook.write(byteArrayOutputStream);
	    workbook.close();
	    
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	            .header("Content-Disposition", "attachment;filename=filename.xlsx")
	            .body(byteArrayOutputStream.toByteArray());
	}

	private String parseLocalDateTime(LocalDateTime localDateTime) {
		
		DecimalFormat decimalFormat = new DecimalFormat("00");
		return decimalFormat.format(localDateTime.getDayOfMonth()) + "/"+decimalFormat.format(localDateTime.getMonthValue()) + "/"+localDateTime.getYear()+ " "+decimalFormat.format(localDateTime.getHour())+":"+decimalFormat.format(localDateTime.getMinute())+":"+decimalFormat.format(localDateTime.getSecond());
	}
}
