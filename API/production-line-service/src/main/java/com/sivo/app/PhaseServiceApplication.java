package com.sivo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.sivo.controller", "com.sivo.service"})
@EntityScan("com.sivo.entity")
@EnableJpaRepositories("com.sivo.repository")
public class PhaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhaseServiceApplication.class, args);
	}

}
