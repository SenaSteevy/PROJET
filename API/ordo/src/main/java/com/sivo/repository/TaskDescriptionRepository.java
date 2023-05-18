package com.sivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivo.resources.TaskDescription;

public interface TaskDescriptionRepository extends JpaRepository<TaskDescription, Long> {
	

}
