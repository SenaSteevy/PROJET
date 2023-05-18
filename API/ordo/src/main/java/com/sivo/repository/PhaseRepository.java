package com.sivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivo.domain.Phase;

public interface PhaseRepository extends JpaRepository<Phase, Long> {
	
}
