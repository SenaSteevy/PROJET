package com.sivo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



//@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.sivo.controller", "com.sivo.service","com.sivo.util", "com.sivo.configuration"})
@EntityScan("com.sivo.entity")
@EnableJpaRepositories("com.sivo.repository")

public class UserServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	
}

