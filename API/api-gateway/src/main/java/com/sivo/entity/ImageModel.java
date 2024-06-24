package com.sivo.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

public class ImageModel {

	

	public ImageModel(String name, String type, byte[] picByte) {
		this.name = name;
		this.type = type;
		this.picByte = picByte;
	}

	
	private Long id;

	private String name;

	private String type;
  
	private byte[] picByte;
	
	private User user;
	
	public ImageModel(String originalFilename, String contentType, byte[] compressBytes, User user2) {
		
		this.name = originalFilename;
		this.type = contentType;
		this.picByte = compressBytes;
		this.user = user2;
	}}
