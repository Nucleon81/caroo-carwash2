package com.practice.project_1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Springboot1Application {


		private static final Logger logger = LoggerFactory.getLogger(Springboot1Application.class);
		public static void main(String[] args) {
			SpringApplication.run(Springboot1Application.class, args);
			logger.info("Application started successfully");
		}

}

