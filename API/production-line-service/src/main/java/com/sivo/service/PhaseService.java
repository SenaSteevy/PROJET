package com.sivo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sivo.entity.Phase;
import com.sivo.entity.Timeslot;
import com.sivo.repository.PhaseRepository;
import com.sivo.repository.TimeslotRepository;
import com.sivo.request.PhaseRequest;
import com.sivo.request.TimeslotRequest;
import com.sivo.response.PhaseResponse;
import com.sivo.response.TimeslotResponse;

@Service
public class PhaseService {

	@Autowired
	PhaseRepository phaseRepository;

	@Autowired
	TimeslotRepository timeslotRepository;

	public ResponseEntity<?> createPhase(PhaseRequest phaseRequest) {
		Phase phase = new Phase(phaseRequest);
		 phase = phaseRepository.save(phase);
		PhaseResponse phaseResponse = new PhaseResponse(phase);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(phaseResponse);

	}

	public ResponseEntity<?> createTimeslot(long phase_id, TimeslotRequest timeslotRequest) {
		Timeslot timeslot = new Timeslot();
		timeslot.setDayOfWeek(timeslotRequest.getDayOfWeek());
		timeslot.setStartTime(timeslotRequest.getStartTime());
		timeslot.setEndTime(timeslotRequest.getEndTime());

		Optional<Phase> phase = phaseRepository.findById(phase_id);
		if (phase.isPresent()) {

			timeslot.setPhase(phase.get());
			PhaseResponse phaseResponse = new PhaseResponse(phase.get());

			timeslot = timeslotRepository.save(timeslot);
			TimeslotResponse timeslotResponse = new TimeslotResponse(timeslot, phaseResponse);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(timeslotResponse);

		} else
			return ResponseEntity.notFound().build();

	}

	public ResponseEntity<?> deletePhaseById(long id) {
		Optional<Phase> phase = phaseRepository.findById(id);
		if (phase.isPresent()) {
			
			List<Timeslot> timeslotList = timeslotRepository.findAll().stream()
			.filter(timeslot -> timeslot.getPhase().equals(phase.get()) )
			.collect(Collectors.toList());
			
			for( Timeslot timeslot : timeslotList)
				timeslotRepository.deleteById(timeslot.getId());
			
			phaseRepository.deleteById(id);
			return ResponseEntity.ok().body("La phase d'ID " + id + " a bien été supprimée.");
		} else
			return ResponseEntity.notFound().build();

	}

	public ResponseEntity<?> updatePhaseById(long id, PhaseRequest phaseRequest) {
		Optional<Phase> phase = phaseRepository.findById(id);
		if (phase.isPresent()) {

			Phase updatedPhase = new Phase( phaseRequest);
			updatedPhase.setId(id);

			updatedPhase = phaseRepository.save(updatedPhase);
			PhaseResponse phaseResponse = new PhaseResponse(updatedPhase);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(phaseResponse);
		} else
			return ResponseEntity.notFound().build();

	}

	public PhaseResponse getPhaseByid(long id) {
		Optional<Phase> phase = phaseRepository.findById(id);
		if (phase.isPresent()) {
			PhaseResponse phaseResponse = new PhaseResponse(phase.get());
			return phaseResponse;
		} else
			return null;
	}

	public ResponseEntity<?> updateTimeslotById(long id, long timeslot_id, TimeslotRequest timeslotRequest) {
		Optional<Timeslot> timeslot = timeslotRepository.findById(timeslot_id);
		Optional<Phase> phase = phaseRepository.findById(id);

		if (timeslot.isPresent() && phase.isPresent()) {
			Timeslot updatedTimeslot = new Timeslot();
			updatedTimeslot.setDayOfWeek(timeslotRequest.getDayOfWeek());
			updatedTimeslot.setStartTime(timeslotRequest.getStartTime());
			updatedTimeslot.setEndTime(timeslotRequest.getEndTime());
			updatedTimeslot.setId(timeslot_id);
			updatedTimeslot.setPhase(phase.get());

			updatedTimeslot = timeslotRepository.save(updatedTimeslot);
			PhaseResponse phaseResponse = new PhaseResponse(phase.get());
			TimeslotResponse timeslotResponse = new TimeslotResponse(updatedTimeslot, phaseResponse);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(timeslotResponse);
		} else
			return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> deleteTimeslotById(long timeslot_id) {
		Optional<Timeslot> timeslot = timeslotRepository.findById(timeslot_id);
		if (timeslot.isPresent()) {
			timeslotRepository.deleteById(timeslot_id);
			return ResponseEntity.ok().body("Le Créneau horaire d'ID " + timeslot_id + " a bien été supprimé.");
		} else
			return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> getTimeslotById(long timeslot_id) {
		Optional<Timeslot> timeslot = timeslotRepository.findById(timeslot_id);
		if (timeslot.isPresent()) {
			Optional<Phase> phase = phaseRepository.findById(timeslot.get().getPhase().getId());
			if (phase.isPresent()) {
				PhaseResponse phaseResponse = new PhaseResponse(phase.get());
				TimeslotResponse timeslotResponse = new TimeslotResponse(timeslot.get(), phaseResponse);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(timeslotResponse);
			} else
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(timeslot.get());
		} else
			return ResponseEntity.notFound().build();
	}

	public List<PhaseResponse> getAllPhase() {
		List<PhaseResponse> phaseList = new ArrayList<PhaseResponse>();
		for (Phase phase : phaseRepository.findAll())
			phaseList.add(new PhaseResponse(phase));
		return phaseList;
	}

	public List<TimeslotResponse> getAllTimeslotByPhaseId(long id) {
		Optional<Phase> phase = phaseRepository.findById(id);
		List<TimeslotResponse> timeslotResponseList = new ArrayList<TimeslotResponse>();

		if (phase.isPresent()) {
			PhaseResponse phaseResponse = new PhaseResponse(phase.get());
			List<Timeslot> timeslotList = timeslotRepository.findAll().stream()
					.filter(timeslot -> timeslot.getPhase().equals(phase.get()))
					.collect(Collectors.toList());

			for (Timeslot timeslot : timeslotList)
				timeslotResponseList.add(new TimeslotResponse(timeslot, phaseResponse));
			
			return timeslotResponseList;
		}
		return null;
	}

}
