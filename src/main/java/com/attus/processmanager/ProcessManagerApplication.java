package com.attus.processmanager;

import com.attus.processmanager.models.LegalProcess;
import com.attus.processmanager.service.LegalProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ProcessManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessManagerApplication.class, args);
	}

	@Autowired
	private LegalProcessService service;

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			LegalProcess process = new LegalProcess();
			process.setCaseDescription("Caso 1");
			process.setCaseNumber(Long.valueOf(1111));
			process.setOpeningDate(LocalDateTime.now());
			service.save(process);
			System.out.println("Running.");
		};
	}

}
