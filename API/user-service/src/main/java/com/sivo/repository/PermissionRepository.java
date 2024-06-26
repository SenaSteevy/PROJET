package com.sivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivo.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {



}
