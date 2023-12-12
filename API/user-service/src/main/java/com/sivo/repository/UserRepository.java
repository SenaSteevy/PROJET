package com.sivo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sivo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	 @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName")
	    List<User> findByRoleName(@Param("roleName") String roleName);

	Optional<User> findByEmail(String username);
		
	
}
