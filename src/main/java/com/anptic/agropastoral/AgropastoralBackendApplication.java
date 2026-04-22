package com.anptic.agropastoral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgropastoralBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgropastoralBackendApplication.class, args);
	}

}
