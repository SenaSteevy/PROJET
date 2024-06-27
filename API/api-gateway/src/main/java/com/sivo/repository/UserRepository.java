package com.sivo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
	Optional<User> findByEmail(String username);
}
