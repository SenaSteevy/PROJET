package com.sivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivo.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	
	
	
}