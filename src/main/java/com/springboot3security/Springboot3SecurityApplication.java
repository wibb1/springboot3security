package com.springboot3security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Springboot3SecurityApplication {
	private static final Logger logger = LoggerFactory.getLogger(Springboot3SecurityApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(Springboot3SecurityApplication.class, args);
		logger.info("SpringBoot3Security started successfully.");
	}

}
