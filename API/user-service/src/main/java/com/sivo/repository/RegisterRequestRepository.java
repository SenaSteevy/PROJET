package com.sivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivo.entity.RegisterRequest;

@Repository
public interface RegisterRequestRepository extends JpaRepository<RegisterRequest, Long> {

	
	
	
}
