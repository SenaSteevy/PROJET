package com.sivo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotAuthorizeException extends RuntimeException{
	
	public UserNotAuthorizeException(String message) {
			
		super(message);
		}
}