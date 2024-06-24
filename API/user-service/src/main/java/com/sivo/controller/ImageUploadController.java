package com.sivo.controller;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sivo.entity.ImageModel;
import com.sivo.entity.User;
import com.sivo.repository.ImageRepository;
import com.sivo.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping(path = "/images")
public class ImageUploadController {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	UserRepository userRepository;

	@PostMapping(value = {"/upload"}, consumes= {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile file, @RequestParam("username") String username) throws IOException {
	  // Retrieve the user entity
	  Optional<User> optionalUser = userRepository.findByEmail(username);
	  if (optionalUser.isEmpty()) {
	    return ResponseEntity.notFound().build();
	  }
	  
	  User user = optionalUser.get();

	  // Create the ImageModel entity and set the user reference
	  ImageModel imageModel = new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
	  imageModel.setUser(user);
	  
	  // Save the ImageModel entity
	  imageRepository.save(imageModel);
	  return ResponseEntity.ok().build();
	}

	@GetMapping(path = { "/getImage" })	
	public ImageModel getImage(@RequestParam("username") String username) throws IOException {
		
		Optional<User> user  = userRepository.findByEmail(username);
		if(user.isEmpty())
			return null;
		final Optional<ImageModel> retrievedImage = imageRepository.findByUser(user.get());
		if(retrievedImage.isEmpty())
			return null;
		
		return retrievedImage.get();
	}
	
	@PostMapping("/delete")
	@Transactional
	public ResponseEntity<?> deleteImage(@RequestParam("id") Long id){
		
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		else {
			Optional<ImageModel> image = imageRepository.findByUser(user.get());
			
			if(image.isPresent()) {
				 imageRepository.delete(image.get());
				
				return ResponseEntity.ok().build();
			}
			else {
				return ResponseEntity.notFound().build();
			}
		}
	}


	
}

