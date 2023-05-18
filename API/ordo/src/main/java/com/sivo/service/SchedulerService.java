package com.sivo.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sivo.domain.Job;
import com.sivo.domain.Phase;
import com.sivo.domain.Schedule;
import com.sivo.domain.Task;
import com.sivo.proxy.PhaseProxy;
import com.sivo.repository.JobRepository;
import com.sivo.repository.PhaseRepository;
import com.sivo.repository.TaskDescriptionRepository;
import com.sivo.repository.TaskRepository;
import com.sivo.request.JobRequest;
import com.sivo.response.PhaseResponse;

@Service
public class SchedulerService {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	TaskDescriptionRepository taskDescriptionRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	PhaseRepository phaseRepository;

	@Autowired
	PhaseProxy phaseProxy;

	public Schedule solve(Schedule problem) {

		// Submit the problem to start solving
		SolverConfig solverConfig = SolverConfig.createFromXmlResource("solverConfig.xml");
		solverConfig.withTerminationConfig(new TerminationConfig().withMinutesSpentLimit((long) 5));

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

		List<Task> taskList = taskRepository.findAll();

		// solving
		Schedule bestSolution = solve(new Schedule(taskList));

		// les tâches d'impression id = 1
		List<Job> planning = bestSolution.getTasks().stream().filter(task -> task.getPhase().getId() == 1)
				.sorted(Comparator.comparing(Task::getStartTime)).map(task -> task.getJob())
				.collect(Collectors.toList());

		// setStartDateTime
		LocalDateTime startDateTime;
		LocalDateTime endDateTime;

		for (Job job : planning) {
			// startDateTime
			List<Task> tasks = bestSolution.getTasks().stream()
					.filter(task -> task.getJob().equals(job))
					.sorted(Comparator.comparingInt(Task::getPhaseId))
					.collect(Collectors.toList());
			startDateTime = tasks.get(1).getStartTime(); // la tâche après l'impression.

			job.setStartDateTime(startDateTime);

			// endDateTime
			OptionalInt MaxPhaseIdOfJob = bestSolution.getTasks().stream()
					.filter(task -> task.getJob().equals(job))
					.mapToInt(Task::getPhaseId)
					.max();
		   
			Task TaskWithMaxPhaseId = bestSolution.getTasks().stream()
					.filter(task -> task.getJob().equals(job))
					.filter(task -> task.getPhaseId() == MaxPhaseIdOfJob.getAsInt())
					.findFirst()
					.get();
		    endDateTime = TaskWithMaxPhaseId.getStartTime()
		    		.plus(TaskWithMaxPhaseId.getPhase().getDuration());
		    
		    job.setLeadTime( Duration.between(startDateTime, endDateTime));

		}

		return ResponseEntity.ok().body(planning);
	}

	public ResponseEntity<?> createJob(JobRequest jobRequest) {

		Optional<Job> optionalJob = jobRepository.findById(jobRequest.getNumOrder());
		if (optionalJob.isPresent()) {
			return ResponseEntity.ok("existe deja !");
		}
		Job job = new Job(jobRequest);
		job = jobRepository.save(job);

		if (job != null)
			return ResponseEntity.ok().body(job);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	public ResponseEntity<?> updateJob(Integer id, JobRequest jobRequest) {

		Optional<Job> job = jobRepository.findById(id);
		if (job.isPresent()) {
			Job updatedJob = new Job(jobRequest);
			updatedJob = jobRepository.save(updatedJob);

			return ResponseEntity.ok().body(updatedJob);
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> updateJobStatus(Integer id, String status) {

		Optional<Job> job = jobRepository.findById(id);
		if (job.isPresent()) {
			job.get().setStatus(status);

			if (status.equalsIgnoreCase("PROCESSING"))
				job.get().setStartDateTime(LocalDateTime.now());

			if (status.equalsIgnoreCase("UNDONE"))
				job.get().setStartDateTime(null);

			Job updatedJob = jobRepository.save(job.get());
			return ResponseEntity.ok().body(updatedJob);
		}
		return ResponseEntity.notFound().build();

	}

	public ResponseEntity<?> deleteById(Integer id) {

		Optional<Job> job = jobRepository.findById(id);
		if (job.isPresent()) {
			jobRepository.deleteById(id);
			return ResponseEntity.ok().body("La tâche d'ID " + id + " a bien été supprimée.");
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

		phaseRepository.deleteAll();
		jobRepository.deleteAll();
		taskRepository.deleteAll();

		List<PhaseResponse> phaseResponseList = phaseProxy.getPhaseList();
		for (PhaseResponse phaseResponse : phaseResponseList)
			phaseRepository.save(new Phase(phaseResponse));

		int maxRows = 500;
		for (int i = 1; i < maxRows; i++) {
			Row row = sheet.getRow(i);
			JobRequest jobRequest = new JobRequest();

			// numOrder
			jobRequest.setNumOrder(Integer.parseInt(row.getCell(1).getStringCellValue()));

			// taskDescription
			jobRequest.setTaskDescription(taskDescriptionRepository.findById((long) jobRequest.getNumOrder()).get());

			// type
			jobRequest.setType(row.getCell(3).getStringCellValue());

			// dueDate
			LocalDateTime dateTime = getLocalDateTime(row.getCell(19));
			jobRequest.setDueDate(dateTime);

			// il n'y a pas de priorité définie dans le fichier excel
			jobRequest.setPriority(1);

			// Status
			jobRequest.setStatus("UNDONE");

			ResponseEntity<?> response = createJob(jobRequest);

			// taskList
			String supplement = row.getCell(7).getStringCellValue();
			List<Task> taskList = getTaskList(row, supplement);

			for (Task task : taskList)
				taskRepository.save(task);
			System.out.println("Job " + (i + 1) + " créé.");
		}

		workbook.close();
		file.close();
		return ResponseEntity.ok("La base de Donnéés a été reinitialisée !");
	}

	private List<Task> getTaskList(Row row, String supplement) {
		List<Task> taskList = new ArrayList<Task>();

		List<String> colorations = new ArrayList<String>();
		colorations.add("MONOCOLORE");
		colorations.add("BICOLORE");
		colorations.add("BRUN");
		colorations.add("TEINTE");
		colorations.add("GRIS");
		colorations.add("BLACK");
		colorations.add("SIVO SUN");
		colorations.add("MARRON");
		colorations.add("BURGUNDY");
		colorations.add("SCOTOVUE");
		colorations.add("QUARTZ");
		colorations.add("LEMON");
		colorations.add("ROSE");
		colorations.add("ORANGE");

		List<String> typeAR = new ArrayList<String>();
		typeAR.add("PREVENCIA");
		typeAR.add("BLUE");
		typeAR.add("CRIZAL");
		typeAR.add("PREMIUM");
		typeAR.add("ALIZE");
		typeAR.add("SAPPHIRE");
		typeAR.add("MIRROR CX");
		typeAR.add("UV");
		typeAR.add("DRIVE");
		typeAR.add("OPTIFOG");

		Phase impression = phaseRepository.findById((long) 1).get();
		Phase surfacage = phaseRepository.findById((long) 2).get();
		Phase coloration = phaseRepository.findById((long) 3).get();
		Phase resys = phaseRepository.findById((long) 4).get();
		Phase antiReflet = phaseRepository.findById((long) 5).get();

		Job job = jobRepository.findById(Integer.parseInt(row.getCell(1).getStringCellValue())).get();

		taskList.add(new Task(job, impression));

		// surfacage
		if (row.getCell(3).getStringCellValue().contains("F"))
			taskList.add(new Task(job, surfacage));

		// coloration
		if (colorations.stream().anyMatch(color -> supplement.toUpperCase().contains(color)))
			taskList.add(new Task(job, coloration));

		// resys
		if (supplement.toUpperCase().contains("SUPRA"))
			taskList.add(new Task(job, resys));

		// AR
		Optional<String> taskType = typeAR.stream().filter(type -> supplement.toUpperCase().contains(type)).findFirst();

		if (taskType.isPresent())
			taskList.add(new Task(job, antiReflet, taskType.get()));

		return taskList;
	}

	private LocalDateTime getLocalDateTime(Cell cell) {

		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			String formatString = cell.getCellStyle().getDataFormatString();

			if (formatString.equals("mm/dd/yyyy\\ hh:mm:ss")) {

				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {

					Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());

					return LocalDateTime.ofInstant(javaDate.toInstant(), ZoneId.systemDefault()); // Conversion en
																									// LocalDateTime

				}
			}
			return null;
		}
		return null;
	}

	public ResponseEntity<?> getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResponseEntity<?> getAllTasks() {

		return ResponseEntity.ok(taskRepository.findAll());
	}

}
