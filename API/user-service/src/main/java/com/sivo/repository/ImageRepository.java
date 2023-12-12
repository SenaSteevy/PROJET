package com.sivo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivo.entity.ImageModel;
import com.sivo.entity.User;


public interface ImageRepository extends JpaRepository<ImageModel, Long> {

	Optional<ImageModel> findByUser(User user);

}