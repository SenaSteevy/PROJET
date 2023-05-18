package com.sivo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivo.entity.Phase;
import com.sivo.entity.Timeslot;


@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

	List<Timeslot> findByPhase(Phase phase);

	void deleteAllByPhase(Phase phase);

}
