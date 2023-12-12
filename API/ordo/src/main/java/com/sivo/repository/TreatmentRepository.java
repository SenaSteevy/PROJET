package com.sivo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sivo.domain.Phase;
import com.sivo.domain.Treatment;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

	List<Treatment> findByPhase(Phase phase);

	@Query("SELECT t FROM Treatment t WHERE t.phase.name = :phaseName")
	List<Treatment> findByPhaseName(@Param("phaseName") String phaseName);

	List<Treatment> findByDescription(String description);

}
