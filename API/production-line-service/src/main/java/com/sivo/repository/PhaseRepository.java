package com.sivo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivo.entity.Phase;


@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
	

}
