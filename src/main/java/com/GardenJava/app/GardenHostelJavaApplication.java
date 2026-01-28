package com.GardenJava.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GardenHostelJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GardenHostelJavaApplication.class, args);
	}

}
